package com.java.programs;

import java.util.Scanner;

public class fibbonaaciSeries {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter Number");
		int input = myScan.nextInt();
		myScan.close();
		int a=0, b=0, c=1;
		for (int i=1; i<=input; i++)
		{
			a=b;
			b=c;
			c=a+b;
			System.out.println(a + " ");
		}

	}

}
