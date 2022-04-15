public class EnumExample {
	enum Level {
		SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY,
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Level myVar = Level.MONDAY;
		switch(myVar){
			case MONDAY:
				System.out.println(myVar);
			break;
			case TUESDAY:
				System.out.println(myVar);
			break;
			case WEDNESDAY:
				System.out.println(myVar);
			break;
			case THURSDAY:
				System.out.println(myVar);
			break;
			case FRIDAY:
				System.out.println(myVar);
			break;
			case SATURDAY:
				System.out.println(myVar);
			break;
			case SUNDAY:
				System.out.println(myVar);
			break;
		}
	}

}
