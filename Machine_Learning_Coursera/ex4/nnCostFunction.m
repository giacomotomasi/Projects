function [J grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   X, y, lambda)
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices. 
% 
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

% Setup some useful variables
m = size(X, 1);
         
% You need to return the following variables correctly 
J = 0;
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

% ====================== YOUR CODE HERE ======================
% Instructions: You should complete the code by working through the
%               following parts.
%
% Part 1: Feedforward the neural network and return the cost in the
%         variable J. After implementing Part 1, you can verify that your
%         cost function computation is correct by verifying the cost
%         computed in ex4.m
%
% Part 2: Implement the backpropagation algorithm to compute the gradients
%         Theta1_grad and Theta2_grad. You should return the partial derivatives of
%         the cost function with respect to Theta1 and Theta2 in Theta1_grad and
%         Theta2_grad, respectively. After implementing Part 2, you can check
%         that your implementation is correct by running checkNNGradients
%
%         Note: The vector y passed into the function is a vector of labels
%               containing values from 1..K. You need to map this vector into a 
%               binary vector of 1's and 0's to be used with the neural network
%               cost function.
%
%         Hint: We recommend implementing backpropagation using a for-loop
%               over the training examples if you are implementing it for the 
%               first time.
%
% Part 3: Implement regularization with the cost function and gradients.
%
%         Hint: You can implement this around the code for
%               backpropagation. That is, you can compute the gradients for
%               the regularization separately and then add them to Theta1_grad
%               and Theta2_grad from Part 2.
%

X = [ones(m, 1) X];

eye_matrix = eye(num_labels);
Y = eye_matrix(y,:);

%-------------------------ALTERNATIVE WAY---------------------------------------------------------------

% Y=zeros(m,num_labels); % matrix 5000x10
% for i=1:m
%     y_vect=zeros(1,num_labels);
%     position=y(i);
%     y_vect(position)=1;
%     Y(i,:)=y_vect;
% end

%Y = circshift(Y,1,2) % to shift the rows of Y of 1 position to the right
%------------------------------------------------------------------------------
% Y = circshift(A,K,dim) circularly shifts the values in array A by K positions 
% along dimension dim. Inputs K and dim must be scalars.
    
%------------------------------------------------------------------------------------------------------


z2=Theta1*X';
a2=[ones(1,m); sigmoid(z2)];

z3=Theta2*a2;
a3=[sigmoid(z3)];
h=a3';

div=1/m;

J=div*sum(sum((-Y.*(log(h)))-(1-Y).*log(1-h)));

div2=lambda/(2*m);

reg1=sum(sum(Theta1(:,2:end).^2));
reg2=sum(sum(Theta2(:,2:end).^2));

reg=div2*(reg1+reg2);

J=J+reg;

%%


delta3=a3'-Y; % matrix 5000x10

delta2=(delta3*Theta2(:,2:end))'.*sigmoidGradient(z2); % matrix 25x5000

Delta1=delta2*X; % X==a1 --> (25x5000)x(5000x401) --> 25x401

Delta2=delta3'*a2'; % (10x5000)x(5000x26) --> 10x26

Theta1_grad=div*Delta1;
Theta2_grad=div*Delta2;

Theta1(:,1) = 0;
Theta2(:,1) = 0;

Theta1=2*div2*Theta1;
Theta2=2*div2*Theta2;

Theta1_grad=Theta1_grad+Theta1;
Theta2_grad=Theta2_grad+Theta2;



% -------------------------------------------------------------

% =========================================================================

% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];


end
