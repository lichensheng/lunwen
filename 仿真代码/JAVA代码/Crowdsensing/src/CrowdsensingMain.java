import algorithm.Algorithm;
import algorithm.Constants;
import algorithm.GeneticAlgorithm;
import vo.Position;
import vo.Vehicle;

import java.io.*;
import java.util.*;

public class CrowdsensingMain {

    private static Random random = new Random();
    private static GeneticAlgorithm algorithm = new GeneticAlgorithm();
    private static final int COST = 5;

    public static Map<String,List<List<Object>>> cacheMap = new HashMap<>();
    static {
        cacheMap.put("random",new ArrayList<>());
        cacheMap.put("egca",new ArrayList<>());
        cacheMap.put("greedy",new ArrayList<>());
        cacheMap.put("enumeration",new ArrayList<>());
    }

    /**
     * init vehicle including trajectory, cost and  probably
     */
    public static void initVehicle(){
        int stopNum = random.nextInt(12) + Constants.TRAJECTORY_NUM_START;
        int startPointX = random.nextInt(Constants.REGIONS );
        int startPointY = random.nextInt(Constants.REGIONS );
        List<Position> trajectories = new ArrayList<>();
        while (trajectories.size() != stopNum - 1){
            int directIndex = random.nextInt(3);
            int x = startPointX + Constants.direction[directIndex][1];
            int y = startPointY + Constants.direction[directIndex][0];
            if (x < 0 || x > Constants.REGIONS - 1 || y < 0 || y > Constants.REGIONS - 1 ){
                continue;
            }
            int regionNum = y * Constants.REGIONS + x;
            Position position = new Position(x,y,regionNum);
            trajectories.add(position);
            startPointX = x;
            startPointY = y;
        }
        float cost = COST * random.nextFloat();
        if(cost < 1){
            cost = cost + 1;
        }
        Constants.vehicleList.add(new Vehicle(Constants.vehicleList.size(),trajectories,cost));

    }

    /**
     * remove the redundancy path when the trajectories have the same points
     */
    public static void removeRedundancyPath(){
        for(Vehicle vehicle : Constants.vehicleList){
            List<String> temp = new ArrayList<>();
            Iterator<Position> iterator = vehicle.getTrajectories().iterator();
            while (iterator.hasNext()){
                Position position = iterator.next();
                String tempStr = position.getX() + "," + position.getY();
                if(!temp.contains(tempStr)){
                    temp.add(tempStr);
                }else {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * generate location matrix
     * @return
     */
    public static List<List<Position>> generateLocationMatrix(){
        List<List<Position>> locationMatrix = new ArrayList<>();
        for (Vehicle vehicle : Constants.vehicleList){
            int size = vehicle.getTrajectories().size();
            List<Position> positions = new ArrayList<>();
            for (int j = 0; j < Constants.SENSING_TIME; j++){
                int index = random.nextInt(size);
                positions.add(vehicle.getTrajectories().get(index));
            }
            locationMatrix.add(positions);
        }
        return locationMatrix;
    }

    /**
     * get interest of regions
     * @return
     */
    public static List<Integer> getInterestOfRegion(){
        List<Integer> interestOfRegions = new ArrayList<>();
        while (interestOfRegions.size() < Constants.INTEREST_OF_REGION){
            int regionNum = random.nextInt(Constants.REGIONS * Constants.REGIONS - 1);
            if (!interestOfRegions.contains(regionNum)){
                interestOfRegions.add(regionNum);
            }
        }
        return interestOfRegions;

    }

    public static List<Integer> getInterestOfRegionByLocation(List<List<Position>> locationMatrix){
        List<Integer> regions = new ArrayList<>();
        for (List<Position> list : locationMatrix){
            for (Position position : list){
                if(!regions.contains(position.getRegionNum())){
                    regions.add(position.getRegionNum());
                }
            }
        }
        List<Integer> interestOfRegions = new ArrayList<>();
        while (interestOfRegions.size() < Constants.INTEREST_OF_REGION){
            int regionNum = random.nextInt(regions.size() - 1);
            if (!interestOfRegions.contains(regions.get(regionNum))){
                interestOfRegions.add(regions.get(regionNum));
            }
        }
        return interestOfRegions;

    }
    /**
     * for debug to display some data
     */
    public static void debugToDisplay(List<List<Position>> locationMatrix,
                                      List<Vehicle> participants,
                                      float stc,
                                      float totalCost,
                                      boolean isFlag,
                                      int chosen,
                                      long excutionTime){
        if(isFlag){
            System.out.println("----------------------------------------------------------------------------------------------------");
            System.out.println("全局变量初始化.............................");
            System.out.println("感知参与者总数：" + Constants.PARTICIPANTS_NUM + "\n"  +
                    "预计感知成本：" + Constants.TOTAL_BUDGET + "\n" +
                    "感知时段：" + Constants.SENSING_TIME + "\n" +
                    "感知区域大小划分为：" + Constants.REGIONS + "*" + Constants.REGIONS);
            System.out.println("----------------------------------------------------------------------------------------------------");
        }
        if(chosen == 1){
            System.out.println("感知参与者：" + "[参与者个数: " + participants.size() + "] " + participants);
            System.out.println("随机算法时空覆盖度：" + stc);
            System.out.println("总感知代价：" + totalCost);
            System.out.println("执行时间:" + excutionTime + "ms");
            System.out.println("----------------------------------------------------------------------------------------------------");
        }
        if(chosen == 2){
            System.out.println("感知参与者：" + "[参与者个数: " + participants.size() + "] " + participants);
            System.out.println("EGCA算法时空覆盖度：" + stc);
            System.out.println("总感知代价：" + totalCost);
            System.out.println("执行时间:" + excutionTime + "ms");
            System.out.println("----------------------------------------------------------------------------------------------------");
        }
        if(chosen == 3){
            System.out.println("感知参与者：" + "[参与者个数: " + participants.size() + "] " + participants);
            System.out.println("Greedy算法时空覆盖度：" + stc);
            System.out.println("总感知代价：" + totalCost);
            System.out.println("执行时间:" + excutionTime + "ms");
            System.out.println("----------------------------------------------------------------------------------------------------");
        }
        if(chosen == 4){
            System.out.println("感知参与者：" + "[参与者个数: " + participants.size() + "] " + participants);
            System.out.println("枚举算法时空覆盖度：" + stc);
            System.out.println("总感知代价：" + totalCost);
            System.out.println("执行时间:" + excutionTime + "ms");
            System.out.println("----------------------------------------------------------------------------------------------------");
        }

    }

    /**
     * Random
     */
    public static List<Object> excuteRandomAlgorithm(List<List<Position>> locationMatrix,List<Integer> interestOfRegion){
        List<Object> randomList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        List<Object> objects = algorithm.randomSelectParticipants();
        long endTime = System.currentTimeMillis();
        List<Vehicle> participants = (List<Vehicle>)objects.get(0);
        float stcRandomAlgorithm = algorithm.coverageOfSTC(participants,locationMatrix,interestOfRegion,null);
        debugToDisplay(locationMatrix,participants,stcRandomAlgorithm,(float)objects.get(1),true,1,endTime - startTime);
        randomList.add(endTime - startTime);
        randomList.add(stcRandomAlgorithm);
        return randomList;
    }

    /**
     * EGCA
     */
    public static List<Object> excuteEGCA(List<List<Position>> locationMatrix,List<Integer> interestOfRegion){
        List<Object> egcaList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        List<Object> object = algorithm.EGCASelectParticipants(locationMatrix,interestOfRegion);
        long endTime = System.currentTimeMillis();
        List<Vehicle> participantsOfEGCA = (List<Vehicle>)object.get(0);
        float stcEGCA = algorithm.coverageOfSTC(participantsOfEGCA,locationMatrix,interestOfRegion,null);
        debugToDisplay(locationMatrix,participantsOfEGCA,stcEGCA,algorithm.computeTotalCost(participantsOfEGCA,null),false,2,endTime - startTime);
        egcaList.add(endTime - startTime);
        egcaList.add(stcEGCA);
        return egcaList;
    }

    /**
     * Greedy
     */
    public static List<Object> excuteGreedy(List<List<Position>> locationMatrix,List<Integer> interestOfRegion){
        List<Object> greedyList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        List<Vehicle> participantsOfGreedy = algorithm.greedySelectParticipants(locationMatrix,interestOfRegion);
        long endTime = System.currentTimeMillis();
        float stcGreedy = algorithm.coverageOfSTC(participantsOfGreedy,locationMatrix,interestOfRegion);
        debugToDisplay(locationMatrix,participantsOfGreedy,stcGreedy,algorithm.computeTotalCost(participantsOfGreedy,null),false,3,endTime - startTime);
        greedyList.add(endTime - startTime);
        greedyList.add(stcGreedy);
        return  greedyList;
    }

    /**
     * Enumeration
     */
    public static  List<Object> excuteEnumeration(List<List<Position>> locationMatrix,List<Integer> interestOfRegion){
        List<Object> enumerationList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        List<Vehicle> participantsOfEnumeration = algorithm.enumerationSelectParticipants(locationMatrix,interestOfRegion);
        long endTime = System.currentTimeMillis();
        if(participantsOfEnumeration == null){
            participantsOfEnumeration = new ArrayList<>();
        }
        float stcOfEnumeration = algorithm.coverageOfSTC(participantsOfEnumeration,locationMatrix,interestOfRegion);
        debugToDisplay(locationMatrix,participantsOfEnumeration,stcOfEnumeration,algorithm.computeTotalCost(participantsOfEnumeration,null),false,4,endTime - startTime);
        enumerationList.add(endTime - startTime);
        enumerationList.add(stcOfEnumeration);
        return enumerationList;
    }

    /**
     * change the total cost
     * @param locationMatrix
     * @param interestOfRegion
     */
    public static void changeTotalCost(List<List<Position>> locationMatrix,List<Integer> interestOfRegion,int r){

        cacheMap.get("random").clear();
        cacheMap.get("egca").clear();
        cacheMap.get("greedy").clear();
        cacheMap.get("enumeration").clear();
        for(int cost = 5; cost <= Constants.MAX_BUDGET; cost++ ){
            if(algorithm.bestParticipantsOfEnumeration != null)
                    algorithm.bestParticipantsOfEnumeration.clear();
            algorithm.maxGlobalSTC = 0;
            Constants.TOTAL_BUDGET = cost;
            List<Object> randomList = excuteRandomAlgorithm(locationMatrix,interestOfRegion);
            List<Object> egcaList = excuteEGCA(locationMatrix, interestOfRegion);
            List<Object> greedyList = excuteGreedy(locationMatrix, interestOfRegion);
            List<Object> enumerationList = excuteEnumeration(locationMatrix, interestOfRegion);
            cacheMap.get("random").add(randomList);
            cacheMap.get("egca").add(egcaList);
            cacheMap.get("greedy").add(greedyList);
            cacheMap.get("enumeration").add(enumerationList);
        }
        writeDataToFile("changeTotalCost",r);
    }

    /**
     * change the number of participants
     */
    public  static void changeParticipantsNum(){

        cacheMap.get("random").clear();
        cacheMap.get("egca").clear();
        cacheMap.get("greedy").clear();
        cacheMap.get("enumeration").clear();
        for(int vehicleNum = 15;vehicleNum <= Constants.MAX_PARTICIPANTS;vehicleNum+=5){
            if(algorithm.bestParticipantsOfEnumeration != null)
                algorithm.bestParticipantsOfEnumeration = null;
            algorithm.maxGlobalSTC = 0;
            Constants.PARTICIPANTS_NUM = vehicleNum;
            for (int i = Constants.vehicleList.size(); i < vehicleNum; i++){
                initVehicle();
            }
            List<Integer> interestOfRegion = getInterestOfRegion();
            removeRedundancyPath();
            List<List<Position>> locationMatrix = generateLocationMatrix();
           // List<Object> randomList = excuteRandomAlgorithm(locationMatrix,interestOfRegion);
            List<Object> egcaList = excuteEGCA(locationMatrix, interestOfRegion);
            //List<Object> greedyList = excuteGreedy(locationMatrix, interestOfRegion);
           // List<Object> enumerationList = excuteEnumeration(locationMatrix, interestOfRegion);
           // cacheMap.get("random").add(randomList);
            cacheMap.get("egca").add(egcaList);
          //  cacheMap.get("greedy").add(greedyList);
           // cacheMap.get("enumeration").add(enumerationList);
        }
        writeDataToFile("changeParticipantsNum",21);
    }

    /**
     * @param r
     */
    public static void changeSensingTime(int r){
        if(Constants.vehicleList != null)
            Constants.vehicleList.clear();
        algorithm.maxGlobalSTC = 0;
        if(algorithm.bestParticipantsOfEnumeration != null)
            algorithm.bestParticipantsOfEnumeration.clear();
        cacheMap.get("random").clear();
        cacheMap.get("egca").clear();
        cacheMap.get("greedy").clear();
        cacheMap.get("enumeration").clear();

        for (int i =0; i < Constants.PARTICIPANTS_NUM; i++){
            initVehicle();
        }
        List<Integer> interestOfRegion = getInterestOfRegion();
        removeRedundancyPath();
        for(int sensingTime = 5;sensingTime <= Constants.MAX_SENSING_TIME;sensingTime++){
            Constants.SENSING_TIME = sensingTime;
            List<List<Position>> locationMatrix = generateLocationMatrix();
            List<Object> randomList = excuteRandomAlgorithm(locationMatrix,interestOfRegion);
            List<Object> egcaList = excuteEGCA(locationMatrix, interestOfRegion);
            List<Object> greedyList = excuteGreedy(locationMatrix, interestOfRegion);
            List<Object> enumerationList = excuteEnumeration(locationMatrix, interestOfRegion);

            cacheMap.get("random").add(randomList);
            cacheMap.get("egca").add(egcaList);
            cacheMap.get("greedy").add(greedyList);
            cacheMap.get("enumeration").add(enumerationList);
        }
        writeDataToFile("changeSensingTime",r);
    }

    /**
     * write location matrix to file
     * @param locationMatrix
     */
    public static void writeLocationMatrixToFile(List<List<Position>> locationMatrix){
        String outpurtStream = "";
        for(List<Position> list:locationMatrix){
            outpurtStream += list.toString() + "\n";
        }
        File file = new File("D:\\Project\\Crowdsensing\\src\\" + "location.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(outpurtStream.getBytes());
        }catch (IOException e){

        }
    }

    public static void writeDataToFile(String fileName,int r){

        File file = new File("D:\\Project\\Crowdsensing\\src\\" + fileName + "_" + r + ".txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            List<Float> stc = new ArrayList<>();
            List<Long>  time = new ArrayList<>();
            for(int i = 0;i < cacheMap.get("random").size();i++){
                time.add((long)(cacheMap.get("random").get(i).get(0)));
                stc.add((float)cacheMap.get("random").get(i).get(1));
            }
            outputStream.write((stc.toString() + "\n").getBytes());
            outputStream.write((time.toString() + "\n").getBytes());
            stc.clear();
            time.clear();

            for(int i = 0;i < cacheMap.get("egca").size();i++){
                time.add((long)(cacheMap.get("egca").get(i).get(0)));
                stc.add((float)cacheMap.get("egca").get(i).get(1));
            }
            outputStream.write((stc.toString() + "\n").getBytes());
            outputStream.write((time.toString() + "\n").getBytes());
            stc.clear();
            time.clear();

            for(int i = 0;i < cacheMap.get("greedy").size();i++){
                time.add((long)(cacheMap.get("greedy").get(i).get(0)));
                stc.add((float)cacheMap.get("greedy").get(i).get(1));
            }
            outputStream.write((stc.toString() + "\n").getBytes());
            outputStream.write((time.toString() + "\n").getBytes());
            stc.clear();
            time.clear();

            for(int i = 0;i < cacheMap.get("enumeration").size();i++){
                time.add((long)(cacheMap.get("enumeration").get(i).get(0)));
                stc.add((float)cacheMap.get("enumeration").get(i).get(1));
            }
            outputStream.write((stc.toString() + "\n").getBytes());
            outputStream.write((time.toString() + "\n").getBytes());
            stc.clear();
            time.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void excuteGenetic(){
        for (int i =0; i < Constants.PARTICIPANTS_NUM; i++){
            initVehicle();
        }
        removeRedundancyPath();
        List<List<Position>> locationMatrix = generateLocationMatrix();
        List<Integer> interestOfRegion = getInterestOfRegionByLocation(locationMatrix);
        algorithm.initPopulation();
        List<int[]> tempPopulations = new ArrayList<>(algorithm.populations);
        for (int i = 500;i <= 5000; i+=500){
            algorithm.populations = new ArrayList<>(tempPopulations);
            Constants.EXECUTE_ITERATOR = i;
            for (int r = 0; r < Constants.EXECUTE_ITERATOR; r++){
                algorithm.populations = algorithm.geneticSelection(locationMatrix,interestOfRegion);
                while(algorithm.populations.size() <= Constants.POP_SIZE){
                    float probal = random.nextFloat();
                    if(probal < Constants.CROSS_RATE){
                        algorithm.geneticCrossing();
                    }else {
                        algorithm.geneticMutation();
                    }

                }
            }
            System.out.println("Max TC : " + algorithm.coverageOfTC(locationMatrix,interestOfRegion,algorithm.populations.get(0))
            + "\n" + "轮数: " + Constants.EXECUTE_ITERATOR );
        }

    }

    public static void main(String[] args){


        /**
         * to change the number of participants
         */

            //excuteGenetic();
            /*initVehicle();
            List<Integer> interestOfRegion = getInterestOfRegion();
            removeRedundancyPath();
            List<List<Position>> locationMatrix = generateLocationMatrix();
            excuteEGCA(locationMatrix,interestOfRegion);*/
            //writeLocationMatrixToFile(locationMatrix);
           // System.out.println(locationMatrix.get(0).toString());
            changeParticipantsNum();

           // changeSensingTime(9);

        /*String[][] label = new String[Constants.REGIONS][Constants.REGIONS];


        for(Vehicle vehicle : Constants.vehicleList){
            for(Position position : vehicle.getTrajectories()){
                label[position.getX()][position.getY()] = "* ";
            }
        }
        for(int i = 0; i < Constants.REGIONS;i++){
            for(int j = 0; j < Constants.REGIONS;j++){
                if(label[i][j] == null){
                    System.out.print("+ ");
                }else{
                    System.out.print(label[i][j]);
                }
            }
            System.out.println("\n");
        }*/
    }
}
