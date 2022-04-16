package com.java.concepts;
import java.util.Scanner;

public class JavaMethodExample {
	static void speak(String name) {
		System.out.println("My Name is: " + name);
	}
	static void myAge(String fname, int age) {
	    System.out.println(fname + " is " + age + " Years Old");
	  }
	static void myMethod(String fname) {
		System.out.println("Full Name: "+fname + " Sinha");
	}
	
	static void withoutParamMethod() {
		System.out.println("withoutParamMethod is executed");
	}

	public static void main(String[] args) {
		System.out.println("Enter Your Name");
		Scanner sc = new Scanner(System.in);
		String MyName = sc.nextLine();
		System.out.println("Enter Your Age");
		int Age = sc.nextInt();
		sc.close();
		speak(MyName);
		myMethod("Sumeet");
	    withoutParamMethod();
	    myAge(MyName, Age);

	}

}
