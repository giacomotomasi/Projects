clear all
close all
clc

% INVERTED PENDULUM LQR

%% System of equations

% F = tau_m/Rr; --> tau_m = Ke*(v - Kc*w)/Ra; with w = x_d/Rr;

syms M m L x x_d Ke Kc Rr Ra v th th_d th_dd x_dd g b I  % define parameters

% define the equations of motion (using above parameters)
equations = [(M+m)*x_dd + b*x_d + m*L*th_dd*cos(th) - m*L*th_d^2*sin(th) == (Ke*v)/(Rr*Ra) - (Ke*Kc*x_d)/(Ra*Rr^2) , (I + m*L^2)*th_dd + m*L*g*sin(th) + m*L*x_dd*cos(th) == 0];
% solve equations for the indicated parameters (in this case: [x_dd, th_dd])
[sol_x_dd, sol_th_dd] = solve(equations, [x_dd, th_dd]);


%% Linearization

functs=[x_d, sol_x_dd, th_d, sol_th_dd]; % define state functions
jac = simplify(jacobian(functs, [x; x_d; th; th_d])); % compute jacobians wrt x, x_d, th, th_d
jac_u = simplify(jacobian(functs, v)); % compute jacobians wrt v

%% Model Parameters

M = 0.5;    % mass of the cart            [kg]
m = 0.2;     % mass of the pendulum        [kg]
L = 0.3;     % lenght of the pendulum      [m]
I = 0.006; % pendulum moment of inertia  [kg*m^2]
b = 0.1;   % cart friction coefficient   []
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

Q = [4 0 0 0; 
     0 1 0 0; 
     0 0 10 0; 
     0 0 0 2;];
 
R = 0.001;

K = lqr(A,B,Q,R);

sys = ss((A - B*K), B, C, D);

co = ctrb(sys); % controllability matrix
controllability = rank(co); % check rank of controllability matrix


t = 0:0.1:4; % simulation duration

[y,t,x] = initial(sys, x0, t);

h = L/2; % rectangle hight
w = L; % rectangle width


%% Animation: cart + pendulum

% Plot borders definition
cart_min_pos = min(y(:,1));
cart_min_vel = min(x(:,2));
vec1 = [cart_min_pos cart_min_vel];
min_vp_cart = min(vec1);

cart_max_pos = max(y(:,1));
cart_max_vel = max(x(:,2));
vec2 = [cart_max_pos cart_max_vel];
max_vp_cart = max(vec2);

pend_min_pos = min(y(:,2));
pend_min_vel = min(x(:,4));
vec3 = [pend_min_pos pend_min_vel];
min_vp_pend = min(vec3);

pend_max_pos = max(y(:,2));
pend_max_vel = max(x(:,4));
vec4 = [pend_max_pos pend_max_vel];
max_vp_pend = max(vec4);

vec = [abs(cart_min_pos) cart_max_pos];
abs_max = max(vec);

% figure size definition
figure('units','normalized','outerposition',[0.3 0.6 0.5 0.8]) % figure size and position

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
    plot([x_cart x_pend],[h+y_cart h+y_pend],'k','LineWidth',2) % beam
    title(['Pendulum at t = ', num2str(t_k), ' seconds'])
    
    xlim([-abs_max-1 abs_max+1]);
    ylim([-1 h+L+1]);
    
    subplot(4,1,3);
    hold on
    plot(t(1:k), y(1:k,1),'LineWidth', 2);
    plot(t(1:k), x(1:k,2), 'c', 'LineWidth', 2);
    title('Cart position (m) and speed (m/s^2)');
    xlim([0 t(end)]);
    ylim([min_vp_cart-1 max_vp_cart+1]);
    grid on
    
    subplot(4,1,4);
    hold on
    plot(t(1:k), y(1:k,2), 'r','LineWidth', 2);
    plot(t(1:k), x(1:k,4), 'm','LineWidth', 2);
    title('Pendulum angle (rad) and speed (rad/s^2)');
    xlim([0 t(end)]);
    ylim([min_vp_pend-1 max_vp_pend+1]);
    grid on
    
    drawnow
    
end






