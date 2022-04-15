package com.java.programs;

import java.util.ArrayList;
import java.util.Scanner;

public class occurenceOfSubstringInString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		// myScan.nextLine();
		System.out.println("Enter Sub-String to be searched");
		String search = myScan.nextLine();
		myScan.close();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int count = 0, index = 0;
		while ((index = input.indexOf(search, index)) != -1) {
			count++;
			indices.add(index);
			index++;
		}

		System.out.println("Total occurrences of a substring in the given string: " + count);
		System.out.println("Indices of substring are: " + indices);

	}

}
