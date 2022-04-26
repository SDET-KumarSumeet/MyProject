package com.java.programs;

import java.util.ArrayList;
import java.util.Scanner;

public class ArrListSolution {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int numLines = sc.nextInt();
        ArrayList<ArrayList> listArray = new ArrayList<ArrayList>();
        for(int i = 0;i<numLines;i++){
            int numOfIntegers = sc.nextInt();
            ArrayList<Integer> intArrayList =  new ArrayList<Integer>();
            for(int j=0;j<numOfIntegers;j++){
                intArrayList.add(sc.nextInt());
            }
            listArray.add(intArrayList);
            sc.nextLine();
        }
        for (ArrayList dar : listArray) {
			System.out.print(" " + dar);
        }
        int numQueries = Integer.parseInt(sc.nextLine());
        for(int i=0;i<numQueries;i++){
            int x = sc.nextInt()-1;
            int y = sc.nextInt()-1;
            sc.nextLine();
            if(x<listArray.size() && y<listArray.get(x).size()){
                System.out.println(listArray.get(x).get(y));
            }else{
                    System.out.println("ERROR!");
                }
        }
    }
   
}