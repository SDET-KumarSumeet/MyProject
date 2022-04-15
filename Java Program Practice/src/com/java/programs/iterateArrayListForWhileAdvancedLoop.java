package com.java.programs;

import java.util.ArrayList;
import java.util.Iterator;

public class iterateArrayListForWhileAdvancedLoop {

	public static void main(String[] args) {
		ArrayList<Integer> myList = new ArrayList<Integer>();
		myList.add(10);
		myList.add(20);
		myList.add(50);
		myList.add(5);
		Iterator<Integer> itr = myList.iterator();
		System.out.println("While Loop:");
		while(itr.hasNext())
		{
			System.out.println(itr.next());
		}
		System.out.println("For Loop:");
		for(int i=0; i<myList.size(); i++)
		{
			System.out.println(myList.get(i));
		}
		System.out.println("Advanced Loop:");
		for(Object i: myList)
		{
			System.out.println(i);
		}
	}

}
