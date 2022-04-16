package com.java.concepts;
class inheritanceExample {
	protected String brand = "Ford";

	public void honk() {
		System.out.println("Pooo...Pooo..");
	}
}

class inheritanceExample2 extends inheritanceExample{
	String model = "Mustang";
}

public class JavaInheritanceExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		inheritanceExample2 obj = new inheritanceExample2();
		obj.honk();
		System.out.println("Car Name: " + obj.brand +" "+ obj.model);
		

	}

}
