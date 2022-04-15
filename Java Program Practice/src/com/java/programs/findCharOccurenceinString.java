package com.java.programs;

import java.util.HashMap;

public class findCharOccurenceinString {

	public static void main(String[] args) {
		String input = "my Name is Kumar Sumeet";
		// char search = 'a';
		HashMap<Character, Integer> result = new HashMap<Character, Integer>();
		for (int j = 0; j < input.length(); j++) {
			int count = 0;
			char search = input.charAt(j);
			for (int i = 0; i < input.length(); i++) {
				if (input.charAt(i) == search)
					count++;
			}
			result.put(search, count);
			// System.out.println("The Character '" + search + "' appears " + count + "times.");

		}
		System.out.print(result);

	}
}