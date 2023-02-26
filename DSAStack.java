/***************************************************
* Author: Zhan Yi Cheok            				   *
* Program: Java Stack class      				   *
* Date: 31/08/2020                 				   *
* Date Modified: 2/11/2020         				   *
***************************************************/

import java.io.*;
import java.util.*;

/*******************************************************
*  Retrieved from Prac 4 							   *
*  Date Retrieved: 28/10/2020						   *
********************************************************/
public class DSAStack implements Iterable, Serializable
{	
	//Private class fields
	private DSALinkedList stack;
	private int count;
	
	//Default Constructor
	public DSAStack()
	{
		stack = new DSALinkedList();
		count = 0;
	}
	
	public Iterator iterator()
	{
		return stack.iterator();
	}
	
	public void printStack()
	{
		System.out.print("[ ");
		for(Object o : this.stack)
		{
			System.out.print(o + ", ");
		}
		System.out.println(" ]");
	}
	
	public int getCount()
	{
		return count;
	}
	
	public Object top()
	{
		Object topVal;
		if(stack.isEmpty())
		{
			throw new IllegalArgumentException("Stack is empty");
		}
		else
		{
			topVal = stack.peekLast();
		}
		return topVal;
	}
	
	//MUTATORS
	public void push(Object value)
	{
		if(stack.isEmpty())
		{
			stack.insertFirst(value);
		}
		else
		{
			stack.insertLast(value);
		}
		count++;
	}
	
	public Object pop()
	{
		Object topVal;
		topVal = stack.peekLast();
		stack.removeLast();
		count--;
		return topVal;
	}
}