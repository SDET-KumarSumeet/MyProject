package com.java.programs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class largestWordinaString {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		myScan.close();
		HashMap<String, Integer> myString = new HashMap<String, Integer>();
		String nstr = "";
		char a;
		int Count = 0;
		for (int i = 0; i < input.length(); i++) {
			a = input.charAt(i);
			if (a != ' ') {
				nstr = nstr + input.charAt(i);
				Count++;

			} else {
				myString.put(nstr, Count);
				Count = 0;
				nstr = "";
			}
		}
		myString.put(nstr, Count);
		System.out.println("Longest SubString length: " + Collections.max(myString.values()));
	}

}