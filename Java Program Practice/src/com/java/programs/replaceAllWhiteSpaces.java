package com.java.programs;

import java.util.Scanner;

public class replaceAllWhiteSpaces {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		myScan.close();
		String input2 = input.replaceAll("\\s", "");
		System.out.println(input2);

	}

}
