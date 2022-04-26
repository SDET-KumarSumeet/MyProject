package com.java.programs;

import java.io.*;
import java.util.*;

public class JavaArrayList {
	public static void main(String[] args) {
		/*
		 * Enter your code here. Read input from STDIN. Print output to STDOUT. Your
		 * class should be named Solution.
		 */
		Scanner myScan = new Scanner(System.in);
		int numLines = myScan.nextInt();
		ArrayList<ArrayList> myList = new ArrayList<ArrayList>();
		for (int i = 0; i < numLines; i++) {
			int numOfint = myScan.nextInt();
			ArrayList<Integer> intArrList = new ArrayList<Integer>();
			for (int j = 0; j < numOfint; j++) {
				intArrList.add(myScan.nextInt());
			}
			myList.add(intArrList);
			myScan.nextLine();
		}
		System.out.println();
		int numQueries = myScan.nextInt();
        for(int i=0;i<numQueries;i++){
            int x = myScan.nextInt()-1;
            int y = myScan.nextInt()-1;
            myScan.nextLine();
            if(x<myList.size() && y<myList.get(x).size()){
                System.out.println(myList.get(x).get(y));
            }else{
                    System.out.println("ERROR!");
                }

		}
		myScan.close();
	}
}