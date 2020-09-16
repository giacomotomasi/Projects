clear all
close all

data = load('ex1data1.txt');
X = data(:, 1); y = data(:, 2);
m = length(y);

X = [ones(m, 1), data(:,1)];
theta = zeros(2, 1);

div= 1/(2*m);
%J=div*(y'*y);

h=X*theta; % it computes all the hypothesis for each training example given theta;

J=div*((h-y)'*(h-y));

alpha=0.01;
num_iter=1:1500;


for iter = 1:1500

    % ====================== YOUR CODE HERE ======================
    % Instructions: Perform a single gradient step on the parameter vector
    %               theta. 
    %
    % Hint: While debugging, it can be useful to print out the values
    %       of the cost function (computeCost) and gradient here.
    %

div=1/m;

h=X*theta;

delta0=div*(sum((h-y)'*X(:,1)));
delta1=div*(sum((h-y)'*X(:,2)));
delta=[delta0;delta1];

theta=theta-alpha*delta;

    % ============================================================

    % Save the cost J in every iteration    
    J_history(iter) = computeCost(X, y, theta);
   

end


plot(num_iter,J_history);
