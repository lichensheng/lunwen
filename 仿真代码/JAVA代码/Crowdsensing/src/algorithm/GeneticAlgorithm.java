package algorithm;

import vo.Position;

import java.util.*;

public class GeneticAlgorithm extends Algorithm {

    public List<int[]> populations;
    public static final int NUMBER_LENGTH = 32;

    /**
     * init the population
     */
    public void initPopulation(){
        populations = new ArrayList<>();
        int modBit = Constants.PARTICIPANTS_NUM  % NUMBER_LENGTH != 0 ?
                (int) Math.pow(2,Constants.PARTICIPANTS_NUM  % NUMBER_LENGTH ):Integer.MAX_VALUE;

        int lastBits = (int) Math.pow(2,modBit);
        while (populations.size() < Constants.POP_SIZE){
            int[] temp;
            if( Constants.PARTICIPANTS_NUM % NUMBER_LENGTH == 0){
                temp = new int[Constants.PARTICIPANTS_NUM / NUMBER_LENGTH ];
            }else {
                temp = new int[Constants.PARTICIPANTS_NUM / NUMBER_LENGTH + 1];
            }
            int randomInt = 0;
            for (int j = 0;j < temp.length;j++){
                if(j == temp.length - 1){
                    randomInt = random.nextInt(lastBits);
                    temp[j] = randomInt;
                    continue;
                }
                randomInt = random.nextInt(Integer.MAX_VALUE);
                temp[j] = randomInt;
            }
            if(!isOverFlowOfCost(temp)){
                populations.add(temp);
            }
        }
        System.out.println("popular size: " + populations.size());

    }

    /**
     * judge the total cost is overflow the budget
     * @param bits
     * @return
     */
    public boolean isOverFlowOfCost(int[] bits){

        float totalCost = 0;
        int sumCount = 0;
        for(int i = 0; i < bits.length; i++){
            int bitCount = NUMBER_LENGTH;
            if(i == bits.length - 1){
                bitCount = Constants.PARTICIPANTS_NUM  % NUMBER_LENGTH != 0 ?
                        Constants.PARTICIPANTS_NUM  % NUMBER_LENGTH : NUMBER_LENGTH;
            }
            int flag = 1;
            for( int j = 0;j < bitCount;j++){
                if((bits[i] & flag) != 0){
                    totalCost += Constants.vehicleList.get(sumCount).getCost();
                }
                flag <<= 1;
                sumCount ++;
                if(totalCost > Constants.TOTAL_BUDGET)
                    break;
            }
        }
        if(totalCost < Constants.TOTAL_BUDGET)
            System.out.println(totalCost);

        return totalCost > Constants.TOTAL_BUDGET ? true : false;
    }

    /**
     * step 1: selection of genetic
     */
    public List<int[]> geneticSelection(List<List<Position>> locationMatrix,
                                  List<Integer> interestOfRegion){

        List<int[]> selects = new ArrayList<>();
        Map<int[],Integer> sortMapByTC = new LinkedHashMap<>();
        for(int[] bits : populations){
           int temporalCoverage = this.coverageOfTC(locationMatrix,interestOfRegion,bits);
           sortMapByTC.put(bits,temporalCoverage);
        }
        sortMapByTC = sortMap(sortMapByTC,selects);
        return selects;
    }

    /**
     * step 2: crossing of genetic
     */
    public void geneticCrossing(){
        int randomCrossing_1 = random.nextInt(populations.size() - 1);
        int randomCrossing_2 = random.nextInt(populations.size() - 1);
        while (randomCrossing_1 == randomCrossing_2){
            randomCrossing_2 = random.nextInt(populations.size() - 1);
        }
        int crossing = random.nextInt(populations.get(randomCrossing_1).length - 1);
        int[] tempCrossing1 = populations.get(randomCrossing_1);
        int[] tempCrossing2 = populations.get(randomCrossing_2);

        int[] crossingArray_1 = new int[tempCrossing1.length];
        for (int i = 0;i < tempCrossing1.length;i++){
            crossingArray_1[i] = tempCrossing1[i];
        }
        int[] crossingArray_2 = new int[tempCrossing2.length];
        for (int i = 0;i < tempCrossing2.length;i++){
            crossingArray_2[i] = tempCrossing2[i];
        }
        if(crossing > crossingArray_1.length/2){
            for(int i = crossing;i < crossingArray_1.length;i++){
                int temp = crossingArray_1[i];
                crossingArray_1[i] = crossingArray_2[i];
                crossingArray_2[i] = temp;
            }
        }else {
            for(int i = 0;i < crossing;i++){
                int temp = crossingArray_1[i];
                crossingArray_1[i] = crossingArray_2[i];
                crossingArray_2[i] = temp;
            }
        }
        populations.add(crossingArray_1);
        populations.add(crossingArray_2);
    }

    /**
     * step 3: mutation of genetic
     */
    public void geneticMutation(){
        int mutationIndex = random.nextInt(populations.size() - 1);
        int[] tempMutation = populations.get(mutationIndex);
        int[] mutation = new int[tempMutation.length];
        for (int i = 0;i < tempMutation.length;i++){
            mutation[i] = tempMutation[i];
        }
        int zeroBit = random.nextInt(Constants.PARTICIPANTS_NUM);




    }

    /**
     * sort the map
     * @param map
     * @return
     */
    public Map<int[],Integer> sortMap(Map<int[],Integer> map,List<int[]> selects){
        Map<int[],Integer> sortResult = new LinkedHashMap<>();
        int[] tempKey = null;
        int tempMaxValue;
        while(map.size() != 0){
            int size = (int) (populations.size() * Constants.SELECT_RATE);
            if(selects.size() == size )
                break;
            tempMaxValue  = Integer.MIN_VALUE;
            Set<int[]> set = map.keySet();
            Iterator<int[]> iterator = set.iterator();
            while(iterator.hasNext()){
                int[] temp = iterator.next();
                if(map.get(temp) > tempMaxValue){
                    tempMaxValue = map.get(temp);
                    tempKey = temp;
                }
            }
            selects.add(tempKey);
            map.remove(tempKey);
            sortResult.put(tempKey,tempMaxValue);
        }
       return sortResult;
    }

    /**
     * to compute the temporal coverage
     * @param locationMatrix
     * @param interestOfRegion
     * @param bits
     */
    public int coverageOfTC(List<List<Position>> locationMatrix,
                             List<Integer> interestOfRegion,
                             int[] bits){
        Map<Integer,Integer> tcMap = new HashMap<>();
        int sumCount = 0;
        for (int j = 0; j < bits.length; j++){
            int bitCount = NUMBER_LENGTH;
            if(j == bits.length - 1){
                bitCount = Constants.PARTICIPANTS_NUM  % NUMBER_LENGTH != 0 ?
                        Constants.PARTICIPANTS_NUM  % NUMBER_LENGTH : NUMBER_LENGTH;
            }
            int flag = 1;
            for (int i = 0; i < bitCount; i++){
                if ((flag & bits[j]) != 0){
                    List<Position> positions = locationMatrix.get(sumCount);
                    for (Position position : positions){
                        if (!tcMap.containsKey(position.getRegionNum())){
                            tcMap.put(position.getRegionNum(),1);
                        } else {
                            tcMap.put(position.getRegionNum(),tcMap.get(position.getRegionNum()) + 1);
                        }
                    }
                }
                sumCount ++;
            }
        }
        Map<Integer,Integer> interestMap = new HashMap<>();
        int minValue = Integer.MAX_VALUE;
        for (Integer regionNum : interestOfRegion){
            if (tcMap.containsKey(regionNum)){
                interestMap.put(regionNum,tcMap.get(regionNum));
            } else{
                interestMap.put(regionNum,0);
            }
        }
        for(Integer key : interestMap.keySet()){
            if(interestMap.get(key) < minValue){
                minValue = interestMap.get(key);
            }
        }
        return minValue;
    }
}
