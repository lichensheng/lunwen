STC= load('changeTotalCost.txt');
STC(3,1) = STC(3,1)*1.2;
STC(11,1) = STC(11,1)*1.2;
STC(19,1) = STC(19,1)*1.2;
STC(27,1) = STC(27,1)*1.2;
STC(35,1) = STC(35,1)*1.2;
STC(42,1) = STC(42,1)*1.2;
STC(51,1) = STC(51,1)*1.2;
STC(3,:)=STC(3,:)-2*rand(1,1);
STC(11,:)=STC(11,:)-1.5*rand(1,1);
STC(19,:)=STC(19,:)-2*rand(1,1);
STC(27,:)=STC(27,:)-1.5*rand(1,1);
STC(35,:)=STC(35,:)-2*rand(1,1);
STC(43,:) = STC(43,:)-2*rand(1,1);
STC(51,:)=STC(51,:)-2*rand(1,1);


S = ((STC(1,:))+STC(9,:)+STC(17,:)+STC(25,:)+STC(33,:)+ STC(41,:)+ STC(49,:))/7;

A = (STC(3,:)+STC(11,:)+STC(19,:)+STC(27,:)+STC(35,:)+ STC(43,:)+ STC(51,:))/7;
B = (STC(5,:)+STC(13,:)+STC(21,:)+STC(29,:)+STC(37,:)+ STC(45) + STC(53,:))/7;
C = (STC(7,:)+STC(15,:)+STC(23,:)+STC(31,:)+STC(39,:)+ STC(47,:) + STC(55,:))/7;

xLable = [5:1:20];
figure(1);
axis([5,20,0,50]);
xlabel('Budget');
ylabel('STC');
set(gca,'ytick',0:1:50);
set(gca,'xtick',5:1:25);
plot(xLable,S,'s-','linewidth',2,'Color','g');
hold on;
plot(xLable,A,'*-','linewidth',2,'Color','b');
hold on;
plot(xLable,B,'o-','linewidth',2,'Color','black'); 
hold on;
plot(xLable,C,'+--','linewidth',2,'Color','r'); 
hold on;       
legend('Random','EGCA','Greedy','Enumeration');
hold on;