package com.java.programs;

import java.util.Scanner;
import java.util.regex.Pattern;

public class validateIPAddress {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter IPv4 address to be validated:");
		String input = myScan.nextLine();
		myScan.close();
		String[] arrStr = input.split("\\.");
		// System.out.println(arrStr);
		int count = 0;
		String IPv4_Regex = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";
		boolean flag = Pattern.matches(IPv4_Regex, input);

		// Convert String Array to Integer Array
		int size = arrStr.length;
		int[] arrInt = new int[size];
		for (int i = 0; i < size; i++) {
			arrInt[i] = Integer.parseInt(arrStr[i]);
		}

		if (size != 4 || flag == false) {
			System.out.println("IPv4 Address: " + input + " is Invalid");
			System.exit(0);
		}
		for (int j = 0; j < size; j++) {
			if ((arrInt[j] >= 0) && (arrInt[j] <= 255)) {
				count++;
			}
		}
		if (count == 4) {
			System.out.println("IPv4 Address: " + input + " is valid");
		} else {
			System.out.println("IPv4 Address: " + input + " is Invalid");
		}
	}

}
