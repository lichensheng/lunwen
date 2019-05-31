%Ãı–ŒÕº%
STC = load('changeParticpantsSTC.txt');
E = (STC(4,:)./STC(2,:))';
G = (STC(3,:)./STC(2,:))';
R = (STC(1,:)./STC(2,:))';
L = zeros(11,1);
for i=1:11
    L(i) = 1-1/exp(1);
end

X = [15:1:25];
Y = [R E G  L];
set(gca,'xtick',[15,16,17,18,19,20,21,22,23,24,25]);
bar(X,Y);
grid on;
legend('Random','EGCA','Greedy','LowerBound');



xLable = [15:1:25];
figure(1);
axis([15,25,0,1]);
set(gca,'ytick',0:1);
set(gca,'xtick',15:25);
plot(xLable,E,'s-','linewidth',2,'Color','g');
hold on;
plot(xLable,G,'*-','linewidth',2,'Color','b');
hold on;
plot(xLable,R,'o-','linewidth',2,'Color','black'); 
hold on;
plot(xLable,L,'+--','linewidth',2,'Color','r'); 
hold on;       
legend('Random','EGCA','Greedy','Enumeration');

