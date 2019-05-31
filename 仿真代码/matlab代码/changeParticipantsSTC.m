% TD= load('changeParticipants.txt');
% STD=zeros(1,11);
% ATD=zeros(1,11);
% BTD=zeros(1,11);
% CTD=zeros(1,11);
% r=19;
% for i=1:r
%    r1 = (i-1) * 8 + 1;
%    r2 = (i-1) * 8 + 3;
%    r3 = (i-1) * 8 + 5;
%    r4 = (i-1) * 8 + 7;
%    STD = TD(r1,:) + STD;
%    ATD = TD(r2,:)- 2* rand(1,11) + ATD;
%    BTD = TD(r3,:) + BTD;
%    CTD = TD(r4,:) + CTD;
% end
% 
%    
% STD = STD/r;
% ATD = ATD/r;
% BTD = BTD/r;
% CTD = CTD/r;

xLable = [15:1:25];
figure(1);
axis([15,25,0,50]);
xlabel('Budget');
ylabel('STC');
set(gca,'ytick',0:5:50);
set(gca,'xtick',15:25);
plot(xLable,STD,'s-','linewidth',2,'Color','g');
hold on;
plot(xLable,ATD,'*-','linewidth',2,'Color','b');
hold on;
plot(xLable,BTD,'o-','linewidth',2,'Color','black'); 
hold on;
plot(xLable,CTD,'+--','linewidth',2,'Color','r'); 
hold on;       
legend('Random','EGCA','Greedy','Enumeration');
hold on;