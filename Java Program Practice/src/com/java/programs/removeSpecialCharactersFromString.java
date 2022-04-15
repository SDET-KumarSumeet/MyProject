package com.java.programs;

import java.util.Scanner;

public class removeSpecialCharactersFromString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		myScan.close();
		input = input.replaceAll("[^a-zA-Z0-9]", " ");  
		System.out.println(input); 
	}

}
