package algorithm;

import vo.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    /**
     * definite global variable
     */
    public static int PARTICIPANTS_NUM = 25;/**感知参与者数目*/
    public final static int MAX_PARTICIPANTS = 100;/**感知参与者上限*/
    public static int TOTAL_BUDGET = 50;/**感知成本预算*/
    public final static int MAX_BUDGET = 50;/**感知成本预算上限*/
    public static int SENSING_TIME = 6;/**感知时段*/
    public final static int MAX_SENSING_TIME = 10;/**感知时段上限*/
    public final static int REGIONS = 10;/**感知区域划分为：10*10 */
    public final static int TRAJECTORY_NUM_START = 8;
    public final static int INTEREST_OF_REGION = 50;/**感兴趣区域个数*/
    public final static int[][] direction = {{-1,0},{0,1},{1,0},{0,-1}};/**车辆运行方向*/
    public static List<Vehicle> vehicleList = new ArrayList<>();/**车辆集合*/

    /**
     * definite variables of genetic algorithm
     */
    public static final int POP_SIZE = 100; /**种群大小*/
    public static final float SELECT_RATE = 0.8f;/**种群选择比率*/
    public static final float CROSS_RATE = 0.6f;/**交叉概率*/
    public static final float MUTATION_RATE = 1 - CROSS_RATE;/**变异概率*/
    public static int EXECUTE_ITERATOR = 1000;
}
