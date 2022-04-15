import java.util.Scanner;

public class JavaSwitchCase {

	public static void main(String[] args) {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Enter day number");
		int day = myScan.nextInt();
		myScan.close();
		switch (day) {
		case 1:
			System.out.println("Monday");
			break;
		case 2:
			System.out.println("Tuesday");
			break;
		case 3:
			System.out.println("Wednesday");
			break;
		case 4:
			System.out.println("Thursday");
			break;
		case 5:
			System.out.println("Friday");
			break;
		case 6:
			System.out.println("Saturday");
			break;
		case 7:
			System.out.println("Sunday");
		default:
			System.out.println("Invalid day entry");
			break;
		}

	}

}
