package com.java.concepts;
class JavaConstructorExample {
	int modelYear;
	String modelName;

	public JavaConstructorExample(int year, String name) {
		modelYear = year;
		modelName = name;
	}

	public static void main(String[] args) {
		JavaConstructorExample myCar = new JavaConstructorExample(1969, "Mustang");
		System.out.println(myCar.modelYear + " " + myCar.modelName);
	}
}