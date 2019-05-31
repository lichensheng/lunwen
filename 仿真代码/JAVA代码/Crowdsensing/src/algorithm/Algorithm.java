package algorithm;
import vo.Position;
import vo.Vehicle;
import java.util.*;

public class Algorithm {

    protected Random random = new Random();
    public float maxGlobalSTC;
    public volatile List<Vehicle> bestParticipantsOfEnumeration;

    /**
     * compute the coverage of STC
     * @param participants
     * @param locationMatrix
     * @param interestOfRegion
     * @return
     */
    public float coverageOfSTC(List<Vehicle> participants,
                               List<List<Position>> locationMatrix,
                               List<Integer> interestOfRegion){

        Map<Integer,Set<Integer>> stcMap = new LinkedHashMap<>();
        for(Vehicle vehicle:participants){
            List<Position> positions = locationMatrix.get(vehicle.getVehicleNum());
            for(int i = 0;i < Constants.SENSING_TIME;i++){
               int regionNum = positions.get(i).getRegionNum();
               if(!stcMap.containsKey(i)){
                   stcMap.put(i,new LinkedHashSet<>());
               }
               stcMap.get(i).add(regionNum);
            }
        }
        float stc = 0;
        for(Integer key : stcMap.keySet()){
            Set<Integer> tempSet = stcMap.get(key);
             for(Integer regionNum:tempSet){
                if(interestOfRegion.contains(regionNum)){
                    stc++;
                }
            }
        }
        return stc;
    }

    /**
     * override the method of coverageOfSTC
     * @param participants
     * @param locationMatrix
     * @param interestOfRegion
     * @param singleVehicle
     * @return
     */
    public  float coverageOfSTC(List<Vehicle> participants,
                                List<List<Position>> locationMatrix,
                                List<Integer> interestOfRegion,
                                Vehicle singleVehicle) {
        if (singleVehicle != null) {
            participants.add(singleVehicle);
        }
        return  coverageOfSTC(participants,locationMatrix,interestOfRegion);
    }
    /**
     * random algorithm to select participants
     * @return
     */
    public List<Object> randomSelectParticipants(){

        float totalCost = 0;
        List<Vehicle> participants = new ArrayList<>();
        List<Vehicle> copyVehicleList = new ArrayList<>(Constants.vehicleList);
        do{
            if(copyVehicleList.size() == 0)
                break;
            int index = random.nextInt(copyVehicleList.size());
            if(totalCost + copyVehicleList.get(index).getCost() > Constants.TOTAL_BUDGET){
                copyVehicleList.remove(copyVehicleList.get(index));
                continue;
            }
            totalCost = totalCost +  copyVehicleList.get(index).getCost();
            participants.add(copyVehicleList.get(index));
            copyVehicleList.remove(copyVehicleList.get(index));

        } while (totalCost < Constants.TOTAL_BUDGET);
        List<Object> retList = new ArrayList<>();
        retList.add(participants);
        retList.add(totalCost);
        return retList;
    }

    /***
     * select participants by EGCA, the performance guarantee is (1-e^-1)
     * @param locationMatrix
     * @param interestOfRegion
     * @return
     */
    public List<Object> EGCASelectParticipants(List<List<Position>> locationMatrix,
                                               List<Integer> interestOfRegion){
        List<Object> result = new ArrayList<>();
        List<List<Vehicle>> vehicleListOf_3 = getListByEnumeration();
        float maxSTC = 0;
        List<Vehicle> bestParticipates = new ArrayList<>();
        float totalCost = 0;
        for(List<Vehicle> participants : vehicleListOf_3){
            List<Vehicle> copyVehicleList = new ArrayList<>(Constants.vehicleList);
            totalCost = 0;
            for(Vehicle v: participants){
                totalCost += v.getCost();
                copyVehicleList.remove(v);
            }
            List<Vehicle> tempParticipants = new ArrayList<>(participants);
            float marginSTC = 0;
            float maxMarginSTC = 0;
            Vehicle vehicleTemp = null;
            Vehicle vehicle = null;
            do {
                if(copyVehicleList.size() == 0 || participants.size() == Constants.PARTICIPANTS_NUM)
                    break;
                for (int i = 0; i < copyVehicleList.size(); i++) {
                    vehicleTemp = copyVehicleList.get(i);
                    if (participants.contains(vehicleTemp))
                        continue;
                    if (computeTotalCost(tempParticipants, vehicleTemp) > Constants.TOTAL_BUDGET) {
                        copyVehicleList.remove(vehicleTemp);
                        continue;
                    }
                    float stc1 = coverageOfSTC(tempParticipants, locationMatrix, interestOfRegion, null);
                    float stc2 = coverageOfSTC(tempParticipants, locationMatrix, interestOfRegion, vehicleTemp);
                    tempParticipants.remove(vehicleTemp);/** remove the vehicle which was added into tempParticipants during invoke the method of coverageOfSTC*/
                    marginSTC = (stc2 - stc1) / vehicleTemp.getCost();
                    if (marginSTC > maxMarginSTC) {
                        maxMarginSTC = marginSTC;
                        vehicle = vehicleTemp;
                    }
                }
                if(vehicle != null){
                    copyVehicleList.remove(vehicle);
                    tempParticipants.add(vehicle);
                    participants.add(vehicle);
                    maxMarginSTC = 0;
                }
            } while (computeTotalCost(tempParticipants,null) < Constants.TOTAL_BUDGET);
            float tempSTC = coverageOfSTC(participants,locationMatrix,interestOfRegion,null);
            if(tempSTC > maxSTC){
                maxSTC = tempSTC;
                bestParticipates = participants;
            }

        }
        if(bestParticipates.size() != 0)
             bestParticipates.remove(bestParticipates.size()-1);
        totalCost = computeTotalCost(bestParticipates,null);
        result.add(bestParticipates);
        result.add(totalCost);
        return result;
    }

    /**
     * select participants with a maximum STC by greedy algorithm
     * @param locationMatrix
     * @param interestOfRegion
     * @return
     */
    public List<Vehicle> greedySelectParticipants(List<List<Position>> locationMatrix,
                                         List<Integer> interestOfRegion){
        List<Vehicle> participants = new ArrayList<>();
        List<Vehicle> copyVehicleList = new ArrayList<>(Constants.vehicleList);
        do{
            if(computeTotalCost(participants,null) > Constants.TOTAL_BUDGET ||
               copyVehicleList.size() == 0){
                break;
            }
            float maxSTC = 0;
            Vehicle vehicle = null;
            for(int i = 0; i < copyVehicleList.size();i++){
                List<Vehicle> temp = new ArrayList<>();
                temp.add(copyVehicleList.get(i));
                float tempSTC = coverageOfSTC(temp,locationMatrix,interestOfRegion);
                if(tempSTC > maxSTC){
                    maxSTC = tempSTC;
                    vehicle = temp.get(0);
                }
            }
            if(computeTotalCost(participants,vehicle) > Constants.TOTAL_BUDGET){
                copyVehicleList.remove(vehicle);
                continue;
            }
            if(vehicle == null)
                break;
            participants.add(vehicle);
            copyVehicleList.remove(vehicle);
        } while (computeTotalCost(participants,null) < Constants.TOTAL_BUDGET);

        return participants;
    }

    /**
     * select participants with maximum STC by enumerating
     * @param locationMatrix
     * @param interestOfRegion
     * @return
     */
    public List<Vehicle>  enumerationSelectParticipants(List<List<Position>> locationMatrix,
                                                         List<Integer> interestOfRegion){

        float[] minAndMax = findMinAndMaxSensingReward();
        int minParticipants = (int)(Constants.TOTAL_BUDGET/minAndMax[1]);
        int maxParticipants = (int)(Constants.TOTAL_BUDGET/minAndMax[0]) + 1;
        for(int participantsNum = minParticipants;participantsNum <= maxParticipants;participantsNum++){
            List<Integer> vehicleNum = new ArrayList<>(participantsNum);
            for(int i = 0;i < participantsNum;i++) {
                vehicleNum.add(0);
            }
            if(participantsNum != 0)
                enumeration(Constants.PARTICIPANTS_NUM,participantsNum,vehicleNum,participantsNum,locationMatrix,interestOfRegion);
        }
        return bestParticipantsOfEnumeration;
    }

    /**
     * enumerate all possible solutions satisfied with total cost less than totalBudget
     * @param n
     * @param k
     * @param vehicleNums
     * @param num
     * @param locationMatrix
     * @param interestOfRegion
     */
    public void enumeration(int n,
                            int k,
                            List<Integer> vehicleNums,
                            int num,
                            List<List<Position>> locationMatrix,
                            List<Integer> interestOfRegion) {
        int i = n;
        if(k > n) return;
        while(i >= k){
            vehicleNums.set(num-k, i);
            if(k>1)
                enumeration(i-1,k-1,vehicleNums,num,locationMatrix,interestOfRegion);
            else {
                int j = 0;
                while( j<num ){
                    j++;
                }
                float totalCost = 0;
                List<Vehicle> temp = new ArrayList<>();
                for(Integer index : vehicleNums){
                    totalCost += Constants.vehicleList.get(index - 1).getCost();
                    temp.add(Constants.vehicleList.get(index - 1));
                }
                float stc = coverageOfSTC(temp,locationMatrix,interestOfRegion);
                if(stc > maxGlobalSTC && totalCost < Constants.TOTAL_BUDGET){
                    maxGlobalSTC = stc;
                    bestParticipantsOfEnumeration = new ArrayList<>(temp);
                }
            }
            i--;
        }
    }

    /**
     * to find the minimum cost and maximum cost from all of vehicles
     * @return
     */
    public float[] findMinAndMaxSensingReward(){
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        for(Vehicle vehicle : Constants.vehicleList){
            float temp = vehicle.getCost();
            if(temp < min){
                min = temp;
            }
            if(temp > max){
                max = temp;
            }
        }
        return new float[]{min,max};
    }
    /**
     * compute the total cost of vehicle and the list of vehicles
     * @param vehicles
     * @param vehicle
     * @return
     */
    public float computeTotalCost(List<Vehicle> vehicles,Vehicle vehicle){
        float totalCost = 0;
        if(vehicle != null){
            totalCost = vehicle.getCost();
        }
        if(vehicles.size() == 0)
            return totalCost;

        for(Vehicle v : vehicles){
            totalCost += v.getCost();
        }
        return totalCost;
    }
    /**
     * get all possible  list of vehicle when the initial cardinal is 3
     * @return
     */
    public List<List<Vehicle>> getListByEnumeration(){

        List<List<Vehicle>> vehicleListOf_3 = new ArrayList<>();
        for(int i = 0; i < Constants.vehicleList.size(); i++){
            for(int j = 0; j < Constants.vehicleList.size(); j++){
                if(i == j)
                    continue;
                for(int k = 0; k < Constants.vehicleList.size(); k++){
                    if(j == k || i == k)
                        continue;
                    List<Vehicle> temp = new ArrayList<>();
                    temp.add(Constants.vehicleList.get(i));
                    temp.add(Constants.vehicleList.get(j));
                    temp.add(Constants.vehicleList.get(k));
                    if(computeTotalCost(temp,null) < Constants.TOTAL_BUDGET)
                        vehicleListOf_3.add(temp);
                }
            }
        }
        return vehicleListOf_3;
    }

    public void geneticAlgorithm(){

    }
}
