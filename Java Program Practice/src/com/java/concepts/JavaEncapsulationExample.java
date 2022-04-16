package com.java.concepts;
import java.util.Scanner;

class encapsulation {
	private String name;

	// Getter
	public String getName() {
		return name;
	}

	// Setter
	public void setName(String newName) {
		this.name = newName;
	}
}

public class JavaEncapsulationExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter new Name: ");
		String NewName = myScan.nextLine();
		encapsulation obj = new encapsulation();
		obj.setName(NewName);
		System.out.println(obj.getName());

	}

}
