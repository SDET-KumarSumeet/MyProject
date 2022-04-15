package com.java.programs;

import java.util.Scanner;

public class primeNumber {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter Number");
		int input = myScan.nextInt();
		myScan.close();
		int flag = 0;
		if (input == 0 || input == 1) {
			flag = 0;
		}

		for (int i = 1; i <= input; i++) {
			if (input%i == 0) {
				flag = flag+1;
			}
		}
		if (flag == 2) {
			System.out.println(input + "- Is Prime");
		}
		else
		{
			System.out.println(input + "- Is Not Prime");
		}
	}

}
