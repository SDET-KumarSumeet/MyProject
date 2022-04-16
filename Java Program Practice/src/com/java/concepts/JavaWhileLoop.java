package com.java.concepts;

public class JavaWhileLoop {

	public static void main(String[] args) {
		int i = 0, j = 6;
		
		//while loop
		while (i < 5) {
			System.out.println(i);
			i++;

		}
		
		//do-while loop
		System.out.println("---------------------");
		do {
			System.out.println(j);
			j--;
		} while (j < 5 && j>=0);

	}

}
