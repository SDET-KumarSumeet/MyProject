package com.java.programs;

import java.util.Scanner;

public class reverseAllSubStringOfaString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		myScan.close();
		String nstr = "", finalString = "";
		String[] myArr = input.split(" ");
		for (int j = 0; j < myArr.length; j++) {
			for (int i = myArr[j].length() - 1; i >= 0; i--) {
				nstr = nstr + myArr[j].charAt(i);
			}
			finalString = finalString + " " + nstr;
			nstr = "";
		}
		System.out.println(finalString.trim());
		for (String i : myArr) {
			System.out.println(i);
		}
	}

}
