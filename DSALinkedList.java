/***************************************************
* Author: Zhan Yi Cheok            				   *
* Program: Double Linked, Double ended linked list *
* Date: 5/09/2020                 				   *
* Date Modified: 2/11/2020         				   *
***************************************************/

import java.io.*;
import java.util.*;

/*******************************************************
*  Retrieved from Prac 4 (Unless otherwise stated)	   *
*  Date Retrieved: 28/10/2020						   *
********************************************************/
public class DSALinkedList implements Iterable, Serializable
{
	private class DSAListNode implements Serializable
	{
		//private class fields
		private Object value;
		private DSAListNode next;
		private DSAListNode prev;
		
		//alternate constructor
		public DSAListNode(Object inValue)
		{
			value = inValue;
			next = null;
			prev = null;
		}
		
		public Object getValue()
		{
			return value;
		}
		
		public void setValue(Object inValue)
		{
			value = inValue;
		}
		
		public DSAListNode getNext()
		{
			return next;
		}
		
		public void setNext(DSAListNode newNext)
		{
			next = newNext;
		}
		
		public DSAListNode getPrev()
		{
			return prev;
		}
		
		public void setPrev(DSAListNode newPrev)
		{
			prev = newPrev;
		}
	}
	
	//private class fields
	private DSAListNode head;
	private DSAListNode tail;
	
	//default constructor
	public DSALinkedList()
	{
		head = null;
		tail = null;
	}
	
	public void insertFirst(Object newValue)
	{
		DSAListNode newNd;
		newNd = new DSAListNode(newValue);
		
		if(isEmpty())
		{
			head = tail = newNd;
		}
		else
		{
			// new node = old head
			newNd.setNext(head);
			// old head prev = new node
			head.setPrev(newNd);
			// head is now new node
			head = newNd;
		}
	}
	
	public void insertLast(Object newValue)
	{
		DSAListNode newNd;
		newNd = new DSAListNode(newValue);
		
		if(isEmpty())
		{
			head = tail = newNd;
		}
		else
		{
			// new node = old tail
			newNd.setPrev(tail);
			// old tail next = new node
			tail.setNext(newNd);
			// tail is now new node
			tail = newNd;
		}
	}
	
	public boolean isEmpty()
	{
		return (head == null);
	}
	
	public Object peekFirst()
	{
		Object nodeValue;

		if(isEmpty())
		{
			throw new IllegalArgumentException("List is empty. Nothing at the head!");
		}
		else
		{
			nodeValue = head.getValue();
		}
		
		return nodeValue;
	}
	
	public Object peekLast()
	{
		Object nodeValue;
		
		if(isEmpty())
		{
			throw new IllegalArgumentException("List is empty. Nothing at the tail!");
		}
		else
		{
			nodeValue = tail.getValue();
		}
		return nodeValue;
	}
	
	public Object removeFirst()
	{
		Object nodeValue;
		nodeValue = head.getValue();
		
		if(isEmpty())
		{
			throw new IllegalArgumentException("List is empty. Cannot remove anything at head");
		}
		else
		{
			if(head == tail)
			{
				head = tail = null;
			}
			else
			{
				// current head = next head
				head = head.getNext();
				// new head prev = null
				head.setPrev(null);
			}
		}
		return nodeValue;
	}
	
	public Object removeLast()
	{
		Object nodeValue;
		nodeValue = tail.getValue();
		
		if(isEmpty())
		{
			throw new IllegalArgumentException("List is empty. Cannot remove anything at the tail");
		}
		else if(head == tail)
		{
			head = tail = null;
		}
		else
		{
			// current tail = prev
			tail = tail.getPrev();
			// prev's next is now null
			tail.setNext(null);
		}
		return nodeValue;
	}
	
	/******************************
	*	Not retrieved from prac 4 *
	******************************/
	public int getPosition(String value)
	{
		DSAListNode temp = head;
		Asset current = null;
		int position = 0;
		boolean stop = false;
		if(head == null && tail == null)
		{
			throw new IllegalArgumentException("List is empty");
		}
		
		do
		{
			current = (Asset)temp.getValue();
			//if value = head value
			if(current.getSymbol().equals(value))
			{
				stop = true;
			}
			else
			{
				//if next value = null means it cant find value
				if(temp.next == null)
				{
					throw new IllegalArgumentException("Can't find value to remove");
				}
				//if next has a node
				else
				{
					temp = temp.next;
					position++;
				}
			}
		} while(!stop);

		return position;
	}
	
	/******************************
	*	Not retrieved from prac 4 *
	******************************/
	public void remove(int position)
	{
		DSAListNode temp = head;
		boolean stop = false;
		
		//head needs to be removed
		if(position == 0)
		{
			head = temp.next;
		}
		else
		{
			//get node of one before deleted node
			for(int i = 0; temp!=null && i < position-1; i++)
			{
				temp = temp.next;
			}
			
			//get node of one after deleted node
			DSAListNode next = temp.next.next;
			
			//set node of one before deleted node to after deleted node
			temp.next = next;
		}
	}
	
	public Iterator iterator() // so for-each loop can be used.
	{
		return new DSALinkedListIterator(this); // return new Iterator of internal type DSALinkedListIterator
												// hook the iterator to this DSALinkedList object
	}
	
	private class DSALinkedListIterator implements Iterator
	{
		private DSAListNode iterNext; // cursor
		public DSALinkedListIterator(DSALinkedList theList)
		{
			iterNext = theList.head;
		}
		//Iterator interface implementation
		public boolean hasNext() { return (iterNext != null); }
		public Object next()
		{
			Object value;
			if(iterNext == null)
			{
				value = null;
			}
			else
			{
				value = iterNext.getValue(); // get the value in the node
				iterNext = iterNext.getNext(); // ready for subsequent calls to next()
			}
			return value;
		}
		public void remove() { throw new UnsupportedOperationException("Not supported"); }
	}
}
			
