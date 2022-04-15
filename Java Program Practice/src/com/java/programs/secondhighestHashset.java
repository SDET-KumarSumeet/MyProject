package com.java.programs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

public class secondhighestHashset {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		ArrayList<Integer> myHash= new ArrayList<Integer>();
		int temp = 0;
		for (int i = 0; i < 10; i++) {
			System.out.println("Enter Number " + i);
			myHash.add(myScan.nextInt());
		}
		Collections.sort(myHash);
		System.out.println(myHash);


	}

}
