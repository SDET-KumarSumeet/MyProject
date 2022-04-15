class outerClass{
	int x=10;
	class innerClass{
		int y=20;
	}
}
public class JavaInnerClassObjectExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		outerClass OutCls = new outerClass();
		outerClass.innerClass InCls = OutCls.new innerClass();
		System.out.println("In Outer Class value of X:" + OutCls.x);
		System.out.println("In Inner Class value of Y:" + InCls.y);
		

	}

}
