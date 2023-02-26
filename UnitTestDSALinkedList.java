public class UnitTestDSALinkedList
{
	public static void main(String[] args)
	{
		int index;
		DSALinkedList list = new DSALinkedList();
		
		list.insertLast("ETH");
		list.insertLast("BTC");
		list.insertLast("ADA");
		for(Object o : list)
		{
			System.out.println(o);
		}

		System.out.println("Peek first " + list.peekFirst());
	}
}