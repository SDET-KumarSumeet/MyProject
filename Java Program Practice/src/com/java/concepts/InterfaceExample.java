package com.java.concepts;
interface Interface1{
	public void method1();
}
interface Interface2{
	public void method2();
}
class InterfaceDemo implements Interface1, Interface2{
	public void method1() {
		System.out.println("Method1");
	}
	public void method2() {
		System.out.println("Method2");
	}
}
public class InterfaceExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InterfaceDemo obj = new InterfaceDemo();
		obj.method1();
		obj.method2();

	}

}
