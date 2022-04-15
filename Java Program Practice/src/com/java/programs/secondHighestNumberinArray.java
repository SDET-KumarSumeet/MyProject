package com.java.programs;

import java.util.Scanner;

public class secondHighestNumberinArray {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		int[] arr = new int[10];
		int temp = 0;
		for (int i = 0; i < 10; i++) {
			System.out.println("Enter Number " + i);
			arr[i] = myScan.nextInt();
		}
		myScan.close();
		int largest = 0, secondLargest = 0;
		for (int j = 0; j < arr.length; j++) {
			if (arr[j] > largest) {
				secondLargest = largest;
				largest = arr[j];
			}
			else
			{
				secondLargest=arr[j];
			}
			

		}
		System.out.println("2nd highest number is: " + secondLargest);

	}

}
