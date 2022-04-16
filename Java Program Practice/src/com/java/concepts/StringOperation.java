package com.java.concepts;

public class StringOperation {

	public static void main(String[] args) {
		String txt = "ABC DEF DEF";
		System.out.println("The length of the txt string is: " + txt.length());

		System.out.println(txt.toUpperCase()); 
		System.out.println(txt.toLowerCase());  
		if (txt.indexOf("out") == 1)
		{
		System.out.println("Not Available in the string");
		}
		
		String str = "\'We\' are the so-called \"Vikings\" from the north\\.";
		System.out.println(str);
		String str1 = "\'We\' are the \rso-called \"Vikings\" from \tthe north\\.";
		System.out.println(str1);
	}

}
