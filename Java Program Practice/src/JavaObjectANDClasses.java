import java.util.Scanner;

class myClass {
	int sum(int x, int y) {
		return x + y;
	}

}

class myClass2 {
	void speak() {
		System.out.println("myClass2 is executed !");		
	}
}

public class JavaObjectANDClasses {

	public static void main(String[] args) {
		System.out.println("Enter x:");
		Scanner myScan = new Scanner(System.in);
		int a = myScan.nextInt();
		myScan.nextLine();
		System.out.println("Enter y:");
		int b = myScan.nextInt();
		myScan.close();
		myClass obj = new myClass();
		System.out.println("The Sum is:  " + obj.sum(a, b));
		myClass2 obj2 = new myClass2();
		obj2.speak();

	}

}
