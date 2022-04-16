package com.java.concepts;
import java.util.HashMap;

public class HashMapExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<Integer, String> myMap = new HashMap<Integer, String>();
		myMap.put(1, "Sumeet");
		myMap.put(2, "Amit");
		myMap.put(3, "Prashant");
		myMap.put(4, "Deb");
		myMap.put(5, "Kiran");
		myMap.put(6, "Sidd");
		// myMap.remove(1);
		System.out.println(myMap);
		for (int i : myMap.keySet()) {
			System.out.println("key: " + i + " value: " + myMap.get(i));
		}

	}

}
