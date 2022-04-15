abstract class Animal1{
	abstract void animalSound();
	void sleep() {
		System.out.println("zzzz");
	}
}
class pig1 extends Animal1{
	void animalSound() {
		System.out.println("Wee-Weee");
		
	}
}
public class AbstractClassExample2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		pig1 myPig = new pig1();
		myPig.animalSound();
		myPig.sleep();

	}

}
