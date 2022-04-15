package com.java.programs;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validateIPv4Java {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter IPv4 address to be validated:");
		String input = myScan.nextLine();
		myScan.close();
		String IPv4_Regex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		Pattern IPv4_Pattern = Pattern.compile(IPv4_Regex);
		Matcher IPv4_Matcher = IPv4_Pattern.matcher(input);
		boolean matchResult = IPv4_Matcher.find();
		if (matchResult) {
			System.out.println("IPv4 Address: " + input + " is valid");
		} else {
			System.out.println("IPv4 Address: " + input + " is Invalid");
		}

	}

}
