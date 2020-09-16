clear all 
close all

data = load('ex2data1.txt');
X = data(:, [1, 2]); y = data(:, 3);


accepted = find(y==1); 
rejected = find(y == 0);

plot(X(accepted,1), X(accepted,2),'b+');
hold on;
plot(X(rejected,1), X(rejected,2),'ro');

xlabel('Exam 1 score')
ylabel('Exam 2 score')

legend('Admitted', 'Not admitted')

theta = zeros(2, 1);

z=X*theta;

h=sigmoid(z);


for i=1:length(h)
if h(i) >= 0.5 
    p(i)=1;
elseif h(i) < 0.5
    p(i)=0;
end
end


p=p';








