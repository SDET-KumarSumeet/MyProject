package com.java.concepts;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author Kumar
 *
 */
public class Java1 {
	public static void main(String[] args) {
		System.out.println("Please Enter Your Name");
		Scanner myScan = new Scanner(System.in);
		String firstName = myScan.nextLine();
		//myScan.nextLine();
		String lastName = myScan.nextLine();
		myScan.close();
		String fullName = firstName +" "+ lastName;
		System.out.println(fullName);
	}
}
