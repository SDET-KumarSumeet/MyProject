import java.util.LinkedList;

public class LinkedListExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedList<Integer> myInt = new LinkedList<Integer>();
		for (int i = 0; i < 5; i++) {
			myInt.add(i, (int) (Math.random() * 101));
		}
		for (int j : myInt) {
			System.out.println(j);
		}
		
	}

}
