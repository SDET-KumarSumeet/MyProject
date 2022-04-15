
public class JavaIfElse {

	public static void main(String[] args) {
		int x = 20;
		int y = 18;
		if (x > y) {
			System.out.println("x is greater than y");
		} else {
			System.out.println("y is greater than x");
		}
		int time = 22;
		if (time < 10) {
			System.out.println("Good morning.");
		} else if (time < 20) {
			System.out.println("Good day.");
		} else {
			System.out.println("Good evening.");
		}
		//variable = (condition) ? expressionTrue :  expressionFalse;
		String result = (time < 18) ? "Good day." : "Good evening.";
		System.out.println(result);
	}

}
