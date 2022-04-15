import java.util.ArrayList;
import java.util.Collections;

public class JavaArrayListExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Integer> myInteger = new ArrayList<Integer>();
		ArrayList<String> myString = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			myInteger.add(i, (int) (Math.random() * 101));
			myString.add("abd" + (int) (Math.random() * 101));
		}
		Collections.sort(myInteger);
		Collections.sort(myString);
		int size = myString.size();
		System.out.println("The Biggest Integer is: "+ myInteger.get(size-1));
		System.out.println("The Smallest Integer is: "+ myInteger.get(0));
		for (int j : myInteger) {
			System.out.println(j);
		}
		
		System.out.println("------------");
		System.out.println("The Biggest Word is: "+ myString.get(size-1));
		System.out.println("The Smallest Word is: "+ myString.get(0));
		for (String k : myString) {
			System.out.println(k);
		}
	}

}
