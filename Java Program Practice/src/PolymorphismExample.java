class Animal{
	void animalSound() {
	System.out.println("Animal makes sound");	
	}
}
class pig extends Animal{
	void animalSound() {
		System.out.println("wee-wee-wee-wee");	
		} 
}
class dog extends Animal{
	void animalSound() {
		System.out.println("bho-bho-bho-bho");	
		} 
}
class cat extends Animal{
	void animalSound() {
		System.out.println("meow-meow-meow-meow");	
		} 
}
public class PolymorphismExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Animal myAnimal = new Animal();
		Animal myPig = new pig();
		Animal myDog = new dog();
		Animal myCat = new cat();
		
		myCat.animalSound();
		myDog.animalSound();
		myPig.animalSound();
		myAnimal.animalSound();
		

	}

}
