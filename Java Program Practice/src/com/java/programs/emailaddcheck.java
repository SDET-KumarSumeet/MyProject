package com.java.programs;

import java.util.regex.Pattern;

public class emailaddcheck {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				String input = "sumeet123@gmail.com";
				boolean result = Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z+_.-]+$", input);
				if (result) {
					System.out.println("IPv4 Address: " + input + " is valid");
				} else {
					System.out.println("IPv4 Address: " + input + " is Invalid");
				}

			}

		}
