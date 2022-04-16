package com.java.concepts;

public class MethodOverloadingExample {
	
	static int sumOfNumber(int x, int y) {
		return x+y;
	}
	
	static int sumOfNumber(int x, int y, int z) {
		return x+y+z;
	}

	static double sumOfNumber(double x, double y) {
		return x+y;
	}
	public static void main(String[] args) {
		System.out.println(sumOfNumber(2,3));
		System.out.println(sumOfNumber(2,3,5));
		System.out.println(sumOfNumber(2.34, 4.56));

	}

}
