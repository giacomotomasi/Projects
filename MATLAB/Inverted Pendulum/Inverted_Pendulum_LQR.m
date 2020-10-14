close all
clear all

% INVERTED PENDULUM 

%% System of equations

% F = tau_m/Rr; --> tau_m = Ke*(v - Kc*w)/Ra; with w = x_d/Rr;

syms M m L x x_d Ke Kc Rr Ra v th th_d th_dd x_dd g  % define parameters

% define the equations of motion (using above parameters)
equations = [(M+m)*x_dd + m*L*th_dd*cos(th) - m*L^2*th_d^2*sin(th) == (Ke*v)/(Rr*Ra) - (Ke*Kc*x_d)/(Ra*Rr^2) , m*L^2*th_dd - m*L*g*sin(th) + m*L*x_dd*cos(th) == 0];
% solve equations for the indicated parameters (in this case: [x_dd, th_dd])
[sol_x_dd, sol_th_dd] = solve(equations, [x_dd, th_dd]);


%% Linearization
%x0 = [0 0 0 0];
%v0 = 0;

functs=[x_d, sol_x_dd, th_d, sol_th_dd]; % define state functions
jac = simplify(jacobian(functs, [x; x_d; th; th_d])); % compute jacobians wrt x, x_d, th, th_d
jac_u = simplify(jacobian(functs, v)); % compute jacobians wrt v


%% Model Parameters

M = 10;    % mass of the cart            [kg]
m = 2;     % mass of the pendulum        [kg]
L = 1;     % lenght of the pendulum      [m]
Rr = 0.1;  % radius of the wheels        [m]
Ra = 10;   % armature resistance         [ohm]
Kc = 2;    % constant                    [V*s/rad]
Ke = 2;    % constant                    [N*m/A]
g = 9.81;  % gravitational acceleration  [m/s^2]

% equilibrium values
%x = 0; % not necessary
x_d = 0;
th = 0;
th_d = 0;
v = 0;

A = subs(jac); % replaces all the variables in the symbolic expression jac with values obtained from the MATLAB workspace
B = subs(jac_u); % replaces all the variables in the symbolic expression jac_u with values obtained from the MATLAB workspace


%% State-space definition

A = double(A);
B = double(B);
%C = eye(4);
C = [1 0 0 0; 
    0 0 1 0];
D = zeros(2,1);

% initial conditions
p = -1; % % pendulum up (p=1)

if (p == 1)
    x0 = [-5,0,0,0]; % pendulum up
elseif (p==-1)
    x0 = [0,0,pi,0]; % pendulum down
    
end


%% LQR controller

Q = [10 0 0 0; 
     0 0.1 0 0; 
     0 0 10 0; 
     0 0 0 10;];
 
R = 0.001;

K = lqr(A,B,Q,R);

%sys_cl = ss((A - B*K), B, C, D,'statename',states,'inputname',inputs,'outputname',outputs);

sys = ss((A - B*K), B, C, D);

co = ctrb(sys); % controllability matrix
controllability = rank(co); % check rank of controllability matrix

t = 0:0.1:7;

[y,t,x] = initial(sys, x0, t);

h = 1; % rectangle hight
w = 2; % rectangle width


%% Animation: cart + pendulum

cart_min = min(y(:,1));
cart_max = max(y(:,1));
pend_min = min(y(:,2));
pend_max = max(y(:,2));

figure('units','normalized','outerposition',[0.3 0.6 0.4 0.7]) % figure size and position

for k=1:length(t)
    
    clf
    
    t_k = t(k);
    x_cart = y(k,1);
    y_cart = 0;
    theta_k = y(k,2);
    
    x_pend = x_cart - L*sin(theta_k);
    y_pend = L*cos(theta_k);
    
    subplot(4,1, [1 2]);
    plot([-20 20],[0 0],'k','LineWidth',2) % plot line with [-10 10],[0 0] coordinates
    hold on
    grid on
    
    rectangle('Position',[x_cart-(w/2), y_cart, w, h],'Curvature',.1,'FaceColor',[1 0.1 0.1]) % plot rectangle shape
    
    plot(x_pend, h+y_pend, 'Marker','o','MarkerSize',10,'MarkerFaceColor','b');
    plot([x_cart x_pend],[h+y_cart h+y_pend],'k','LineWidth',2)
    title(['Pendulum at t = ', num2str(t_k), ' seconds'])
    
    xlim([-10 12]);
    ylim([-2 6]);
    
    subplot(4,1,3);
    plot(t(1:k), y(1:k,1),'LineWidth', 2);
    title('Cart position (m)');
    xlim([0 7]);
    ylim([cart_min-1 cart_max+1]);
    grid on
    
    subplot(4,1,4);
    plot(t(1:k), y(1:k,2), 'r','LineWidth', 2);
    title('Pendulum angle (rad)');
    xlim([0 7]);
    ylim([pend_min-1 pend_max+1]);
    grid on
    
    drawnow
    
end


% %% Plot Cart position and Pendulum angle

% figure
% subplot(3,1,2);
% plot(t,y(:,1),'LineWidth', 2);
% title('Cart position (m)');
% grid on
% 
% subplot(3,1,3);
% plot(t,y(:,2), 'r', 'LineWidth', 2);
% title('Pendulum angle (rad)');
% grid on

