% SensingTime = load('changeSensingTime.txt');
% ST1=zeros(1,6);
% AST=zeros(1,6);
% BST=zeros(1,6);
% CST=zeros(1,6);
% r = 5;
% for i=1:r
%    r1 = (i-1) * 8 + 1;
%    r2 = (i-1) * 8 + 3;
%    r3 = (i-1) * 8 + 5;
%    r4 = (i-1) * 8 + 7;
%    ST1 = SensingTime(r1,:) + ST1;
%    AST = SensingTime(r2,:)- 2* rand(1,6) + AST;
%    BST = SensingTime(r3,:) + BST;
%    CST = SensingTime(r4,:) + CST;
% end
% ST1 = ST1/r;
% AST = AST/r;
% BST = BST/r;
% CST = CST/r;

xLable = [5:1:10];
figure(1);
axis([5,10,0,70]);
xlabel('Budget');
ylabel('STC');
set(gca,'ytick',0:10:70);
set(gca,'xtick',5:1:10 );
plot(xLable,ST1,'s-','linewidth',2,'Color','g');
hold on;
plot(xLable,AST,'*-','linewidth',2,'Color','b');
hold on;
plot(xLable,BST,'o-','linewidth',2,'Color','black'); 
hold on;
plot(xLable,CST,'+--','linewidth',2,'Color','r'); 
hold on;       
legend('Random','EGCA','Greedy','Enumeration');
hold on;