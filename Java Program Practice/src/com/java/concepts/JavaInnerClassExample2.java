package com.java.concepts;
class outerClasss {
	void speak(String name) {
		System.out.println("I am " + name);
	}

	class innerClass {

		void Call() {
			outerClasss OtrCls = new outerClasss();
			OtrCls.speak("Sumeet");

		}
	}
}

public class JavaInnerClassExample2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		outerClasss OtrCls = new outerClasss();
		outerClasss.innerClass InrCls = OtrCls.new innerClass();
		InrCls.Call();
	}

}
