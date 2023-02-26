public class UnitTestDSAGraph
{
	public static void main(String[] args)
	{
		DSAGraph graph = new DSAGraph();
		DSALinkedList links = new DSALinkedList();
		
		
		System.out.println("Added Directional edges");
		graph.addEdge("ETH", "BTC");
		graph.addEdge("ETH", "ADA");
		graph.addEdge("ADA", "ETH");
		graph.addEdge("ADA", "BTC");
		
		System.out.println("Directional Links in ETH: ");
		graph.displayAdjacent("ETH");
		System.out.println("Directional Links in BTC: ");
		graph.displayAdjacent("BTC");	
		System.out.println("Directional Links in BTC: ");
		graph.displayAdjacent("ADA");	
		System.out.print("Is ETH adjacent to BTC?: ");
		if(graph.isAdjacent("ETH", "BTC"))
		{
			System.out.println("Yes");
		}
		else
		{
			System.out.println("No");
		}
		System.out.println("ETH has " + graph.getEdgeCount("ETH") + " Directional Edges");
		
		graph.removeVertexAndEdges("BTC");
		System.out.println("Directional Links in ETH: ");
		graph.displayAdjacent("ETH");
		System.out.println("Directional Links in ADA: ");
		graph.displayAdjacent("ADA");
		System.out.println("Directional Links in BTC: ");
		try
		{
			graph.displayAdjacent("BTC");	
		}catch(Exception e)
		{
			System.out.println("successfully removed. vertex does not exist");
		}
	}
}