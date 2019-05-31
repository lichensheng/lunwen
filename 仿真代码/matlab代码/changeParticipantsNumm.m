T= load('changeParticipants.txt');
ST=zeros(1,11);
AT=zeros(1,11);
BT=zeros(1,11);
CT=zeros(1,11);
for i=1:8
   r1 = (i-1) * 8 + 2;
   r2 = (i-1) * 8 + 4;
   r3 = (i-1) * 8 + 6;
   r4 = (i-1) * 8 + 8;
   ST = T(r1,:) + ST;
   AT = T(r2,:) + AT;
   BT = T(r3,:) + BT;
   CT = T(r4,:) + CT;
end

   
ST = ST/8;
AT = AT/8;
BT = BT/8;
CT = CT/8;

xLable = [15:1:25];
figure(1);
axis([15,25,0,150000]);
xlabel('Budget');
ylabel('STC');
set(gca,'ytick',0:10000:150000);
set(gca,'xtick',15:25);
plot(xLable,ST,'s-','linewidth',2,'Color','g');
hold on;
plot(xLable,AT,'*-','linewidth',2,'Color','b');
hold on;
plot(xLable,BT,'o-','linewidth',2,'Color','black'); 
hold on;
plot(xLable,CT,'+--','linewidth',2,'Color','r'); 
hold on;       
legend('Random','EGCA','Greedy','Enumeration');
hold on;