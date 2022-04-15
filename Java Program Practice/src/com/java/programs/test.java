package com.java.programs;

import java.util.regex.Pattern;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String input = "1.11.123.45";
		boolean result = Pattern.matches("^([0-255]{1,3})\\.([0-255]{1,3})\\.([0-255]{1,3})\\.([0-255])$", input);
		if (result) {
			System.out.println("IPv4 Address: " + input + " is valid");
		} else {
			System.out.println("IPv4 Address: " + input + " is Invalid");
		}

	}

}
