package com.java.programs;

import java.util.Scanner;

public class reverseOfString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		myScan.close();
		int strLen = input.length();
		String nstr="";
		char ch;
		for(int i = strLen-1; i>=0; i--)
		{
			ch = input.charAt(i);
			nstr = nstr + ch;
		}
		
		System.out.println("Reversed String is: " + nstr);
	}

}
