import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split(" ");
        int num = Integer.parseInt(input[0]);
        ArrayList<String> strings = new ArrayList<>();
        for(int i = 0;i < num;i++){
            int len = input[i+1].length();
            if(len < 8){
                for(int j=0;j < 8-len;j++){
                    input[i+1] += "0";
                }
                strings.add(input[i+1]);
            }else if(len > 8){
                int div = len/8;
                int mod = len%8;
                int j = 0;
                for(;j < div;j++){
                    String s = input[i+1].substring(8*j,8*j + 8);
                    strings.add(s);
                }
                String left = input[i+1].substring(8*j);
                int leftLen = left.length();
                for(int length = 0;length < 8 - leftLen; length++){
                    left += "0";
                }
                strings.add(left);
            }else {
                strings.add(input[i+1]);
            }
        }
        Collections.sort(strings);
        System.out.println(strings);
    }

    public static void main1(String[] args){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        //"3(2(A))" == 3(AA) = AAAAAA
        ArrayList<Character> output = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        for(int i = 0;i < input.length();i++){
            char temp = input.charAt(i);
            if((temp >= 'a'&& temp <= 'z')|| (temp >= 'A' && temp <= 'Z')){
                output.add(temp);
                continue;
            }else{

            }
        }
    }
}
