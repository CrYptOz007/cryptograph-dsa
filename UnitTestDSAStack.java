/***************************************************
* Author: Zhan Yi Cheok            				   *
* Program: Test harness for DSAStack and Queue     *
* Date: 31/08/2020                 				   *
* Date Modified: 2/11/2020         				   *
***************************************************/

/*******************************************************
*  Modified from Prac 4 							   *
*  Date Retrieved: 28/10/2020						   *
********************************************************/
public class UnitTestDSAStack
{
	public static void main(String[] args)
	{			
		System.out.println("///////// Stack Test /////////");
		DSAStackTest();
		System.out.println();
		
		
	}
	
	public static void DSAStackTest()
	{
		DSAStack stack = new DSAStack();
		stack.push(1);
		stack.push(2);
		
		stack.printStack(); //OUTPUT [1, 2, null]
		
		stack.pop();
		System.out.println("1 Stack pop()\nTop is now: " + stack.top()); //OUTPUT TOP: 1
		stack.push(4);
		stack.push(5);
		
		stack.printStack(); //OUTPUT [1, 4, 5]
	}	
}