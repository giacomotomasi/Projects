function [J, grad] = linearRegCostFunction(X, y, theta, lambda)
%LINEARREGCOSTFUNCTION Compute cost and gradient for regularized linear 
%regression with multiple variables
%   [J, grad] = LINEARREGCOSTFUNCTION(X, y, theta, lambda) computes the 
%   cost of using theta as the parameter for linear regression to fit the 
%   data points in X and y. Returns the cost in J and the gradient in grad

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost and gradient of regularized linear 
%               regression for a particular choice of theta.
%
%               You should set J to the cost and grad to the gradient.
%

%X=[ones(m,1) X]; % done when calling the function

div=1/(2*m);
div2=lambda/(2*m);

h=X*theta;

J=div*(h-y)'*(h-y);

reg=div2*(theta(2:end,:)'*theta(2:end,:));

J=J+reg;

% =========================================================================

% div2=lambda/m;
% 
% grad_0=2*div*((h-y)'*X(:,1));
% grad_n=2*div*((h-y)'*X(:,2:end))+div2*sum(theta(2:end,:));
% grad=[grad_0;grad_n];


grad_tot=2*div*(X'*(h-y)); % dimension 2x1 since X has been augmented with 1 column vector (bias)
theta_n=theta(2:end); % exclude theta_0

div2=lambda/m; % updating value
grad0=grad_tot(1:1); 
grad_n=grad_tot(2:end,1)+(div2*(theta_n)); 

grad=[grad0;grad_n]; 


% =========================================================================

grad = grad(:);



end
