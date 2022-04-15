package com.java.programs;

import java.util.Scanner;

public class armstrongNumber {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter Number to be checked:");
		int input = myScan.nextInt();
		myScan.close();
		int num=0, result=0, temp=input, count=0;
		while(input>0)
		{
			input = input/10;
			count++;
		}
		input=temp;
		while(input>0)
		{
			num=input%10;
			input=input/10;
			result = (int) (result+Math.pow(num, count));
		}
		
		if(result==temp)
		{
			System.out.println("Armstrong");
		}
		else
		{
			System.out.println("Not Armstrong");
		}

	}

}
