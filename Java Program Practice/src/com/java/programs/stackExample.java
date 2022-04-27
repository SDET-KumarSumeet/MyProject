package com.java.programs;

import java.util.*;

class stackExample {

	public static boolean paranValidate(String input) {
		String[] chrArray = input.split("");
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i < chrArray.length; i++) {
			if (chrArray[i].trim().equals("{"))
				stack.push("}");
			if (chrArray[i].trim().equals("("))
				stack.push(")");
			if (chrArray[i].trim().equals("["))
				stack.push("]");
			if (!stack.empty()) {
				if (chrArray[i].trim().equals("}") && chrArray[i].trim().equals(stack.peek()))
					stack.pop();
				if (chrArray[i].trim().equals(")") && chrArray[i].trim().equals(stack.peek()))
					stack.pop();
				if (chrArray[i].trim().equals("]") && chrArray[i].trim().equals(stack.peek()))
					stack.pop();
			}
			else return false;
		}

		return stack.empty();
	}

	public static void main(String[] argh) {
		Scanner sc = new Scanner(System.in);

		while (sc.hasNext()) {
			String input = sc.next();
			// Complete the code
			if (paranValidate(input)) {
				System.out.println("true");
			} else {
				System.out.println("false");
			}

		}
		sc.close();
	}
}