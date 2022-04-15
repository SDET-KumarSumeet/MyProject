package com.java.programs;

import java.util.Scanner;

public class replaceWhiteSpaceinStringWithoutUsingreplace {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		myScan.close();
		String[] arr = input.split("");
		String nstr="";
		for (int i = 0; i<arr.length; i++)
		{
			if ((arr[i]!=" ") && (arr[i]!="\t"))
			{
				nstr=nstr+input.charAt(i);
			}
		}
		System.out.println(nstr);
	}

}
