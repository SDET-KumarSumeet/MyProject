package com.java.programs;

import java.util.Scanner;

public class palindromeString {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		myScan.close();
		String nstr="";
		for (int i = input.length()-1; i>=0; i--)
		{
			nstr = nstr + input.charAt(i);
		}
		if(nstr.equalsIgnoreCase(input))
		{
			System.out.println(input + " is Palindrome");
		}
		else
		{
			System.out.println(input + " is Not Palindrome");
		}

	}

}
