
public class JavaArray {

	public static void main(String[] args) {
		// Single Dimension Array
		int array[] = new int[5];
		String strArray[] = new String[5];
		for (int i = 0; i < 5; i++) {
			array[i] = i;
			strArray[i] = "Sumeet";

		}
		for (int j : array) {
			System.out.println(j);
		}
		for (String k : strArray) {
			System.out.println(k);
		}

		// Double Dimension array
		int myArray[][] = new int[5][5];
		for (int a = 0; a < 5; a++) {
			for (int b = 0; b < 5; b++) {
				myArray[a][b] = 5;
			}
		}
		for (int a = 0; a < 5; a++) {
			for (int b = 0; b < 5; b++) {
				System.out.println(myArray[a][b]);
			}
		}
		
	}

}
