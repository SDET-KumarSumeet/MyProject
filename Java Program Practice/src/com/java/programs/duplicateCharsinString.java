package com.java.programs;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class duplicateCharsinString {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		myScan.close();
		String[] myArr = input.split("");
		HashMap<String, Integer> ch = new HashMap<String, Integer>();
		for (int i = 0; i < myArr.length; i++) {
			if (ch.containsKey(myArr[i])) {
				int Count = ch.get(myArr[i]);
				ch.put(myArr[i], Count + 1);
			} else {
				ch.put(myArr[i], 1);
			}
		}
		for (Entry<String, Integer> entry : ch.entrySet()) {
			if(entry.getValue()>1)
			{
		    System.out.println("Char Key = " + entry.getKey() + ", Repeated Value = " + entry.getValue());
			}
		}

	}

}
