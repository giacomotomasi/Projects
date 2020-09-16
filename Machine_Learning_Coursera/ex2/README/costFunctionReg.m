function [J, grad] = costFunctionReg(theta, X, y, lambda)
%COSTFUNCTIONREG Compute cost and gradient for logistic regression with regularization
%   J = COSTFUNCTIONREG(theta, X, y, lambda) computes the cost of using
%   theta as the parameter for regularized logistic regression and the
%   gradient of the cost w.r.t. to the parameters. 

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost of a particular choice of theta.
%               You should set J to the cost.
%               Compute the partial derivatives and set grad to the partial
%               derivatives of the cost w.r.t. each parameter in theta

div1= 1/m;
div2= lambda/(2*m);

z=X*theta;

h=sigmoid(z);

%theta0=theta(1);
theta_n=theta(2:end);

J=div1*(-y'*log(h)-(1-y)'*log(1-h))+div2*(theta_n'*theta_n);

grad_tot=div1*(X'*(h-y)); % gradient considering all parameters

grad0=grad_tot(1:1); % gradient of element x0 (it is a 1x1 vector)
grad_n=grad_tot(2:end,1)+(2*div2*(theta_n)); % gradient penalizing x_j parameters (with j>=1); (it is a 27x1 vector)

grad=[grad0;grad_n]; % final gradient (it is a 28x1 vector)


% =============================================================

end
