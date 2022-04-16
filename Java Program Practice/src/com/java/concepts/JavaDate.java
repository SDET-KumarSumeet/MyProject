package com.java.concepts;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class JavaDate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalDate myVar1 = LocalDate.now();
		LocalDateTime myVar2 = LocalDateTime.now();
		LocalTime myVar3 = LocalTime.now();
		DateTimeFormatter myVar4 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		DateTimeFormatter myVar5 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDateTime = myVar2.format(myVar4);
		String formattedDate = myVar1.format(myVar5);
		System.out.println("LocalDate: "+myVar1);
		System.out.println("LocalDateTime: "+myVar2);
		System.out.println("LocalTime: "+myVar3);
		System.out.println("Formatted DateTime: "+formattedDateTime);
		System.out.println("Formatted Date: "+formattedDate);
	}

}
