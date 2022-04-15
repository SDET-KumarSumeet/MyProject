package com.java.programs;

import java.util.Scanner;

public class reverseSuStringOfaString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter String");
		String input = myScan.nextLine();
		System.out.println("Enter Sub-String to be reversed");
		String search = myScan.nextLine();
		myScan.close();
		String nstr = "", finalString = "";
		for (int i = search.length() - 1; i >= 0; i--) {
			nstr = nstr + search.charAt(i);
		}
		finalString=input.replace(search,nstr);
		/*int index = 0;
		for (int j = 0; j < input.length(); j++) {
			if ((index = input.indexOf(search, index)) != -1) {
				finalString = finalString + input.charAt(j);
			} else {
				finalString = finalString + nstr;
				j = j + nstr.length() - 1;
				index++;
			}
		}*/
		
		System.out.println("The final String is: "+ finalString);

	}

}
