package com.java.programs;

import java.util.HashMap;
import java.util.Scanner;

public class numberOfWordsinString {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		myScan.close();
		HashMap<String, Integer> myMap = new HashMap<String, Integer>();
		String[] myArr = input.split(" ");
		for (int i = 0; i < myArr.length; i++) {
			if (myMap.containsKey(myArr[i])) {
				int count = myMap.get(myArr[i]);
				myMap.put(myArr[i], count + 1);
			} else {
				myMap.put(myArr[i], 1);
			}
		}
		System.out.println(myMap);

	}

}
