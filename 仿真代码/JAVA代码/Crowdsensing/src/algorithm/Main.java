package algorithm;

import java.util.*;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        String[] a = scanner.nextLine().split(" ");
        Map<String,Integer> map = new HashMap<>();
        for(String str : a){
            if(map.containsKey(str)){
                map.put(str,map.get(str)+1);
            }else{
                map.put(str,1);
            }
        }
        Object[] values = map.values().toArray();
        boolean isTrue = true;
        int[] val = new int[values.length];
        for(int i=0;i<values.length;i++){
            int temp = (Integer)values[i];
            if(temp < 2){
                System.out.println(" ：" + 0);
                break;
            }
            val[i] = temp;
        }
        Arrays.sort(val);
        int base = val[0];
        int num = 1;
        for(int i = 1;i<val.length;i++){
            if(val[i]%base == 0){
                num = num + val[i]/base;
            }else{
                System.out.println(0);
                isTrue = false;
                break;
            }
        }
        if(isTrue){
            System.out.println(num);
        }

    }

}
