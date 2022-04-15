import java.math.BigInteger;

public class Java2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BigInteger MyInteger = new BigInteger("30");
		MyInteger.intValue();
		MyInteger.longValue();
		MyInteger.floatValue();
		MyInteger.doubleValue();
		// System.out.println(myNum);
		float myNum = 5.757631f;
		System.out.println(myNum);
		double myNum1 = 19.99d;
		System.out.println(myNum1);
		double s= myNum + myNum1;
		System.out.println(s);
		
		int myNum2 = 5789;
		System.out.println(myNum2);
		long myNum3 = 19902536;
		System.out.println(myNum3);
		long si= myNum2 + myNum3;
		System.out.println(si);

		
		boolean isJavaFun = true;
		boolean isFishTasty = false;
		System.out.println(isJavaFun);     // Outputs true
		System.out.println(isFishTasty);   // Outputs false
		
		char myGrade = 'B';
		System.out.println(myGrade);
		char myVar1 = 65, myVar2 = 66, myVar3 = 67;
		System.out.println(myVar1);
		System.out.println(myVar2);
		System.out.println(myVar3);
	}

}
