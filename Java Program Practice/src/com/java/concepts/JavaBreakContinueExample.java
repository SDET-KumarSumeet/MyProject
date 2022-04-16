package com.java.concepts;

public class JavaBreakContinueExample {

	public static void main(String[] args) {
		//Break Example
		for (int i = 0; i < 10; i++) {
			if (i == 5) {
				break;
			}
			System.out.println(i);
		}
		
		//Continue Example
		System.out.println("-------");
		for (int j = 0; j < 10; j++) {
			if (j>4 && j<8) {
				System.out.println("Skipped Numbers are: " + j);
				continue;
			}
			System.out.println(j);
		}
	}

}
