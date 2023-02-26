/***********************************
* Author: Zhan Yi Cheok            *
* Program: DIY Graph ADT           *
* Date: 06/09/2020                 *
* Date Modified: 2/11/2020         *
***********************************/


import java.io.*;
import java.util.*;

public class DSAGraph implements Serializable
{
	public class DSAGraphVertex implements Serializable
	{
		//private class fields
		private String label;
		private DSALinkedList links;
		
		public DSAGraphVertex(String inLabel)
		{
			label = inLabel;
			links = new DSALinkedList();
		}
		
		public String getLabel()
		{
			return label;
		}
		
		public DSALinkedList getLinks()
		{
			return links;
		}
		
		public void setLink(DSAGraphVertex inVertex)
		{
			links.insertLast(inVertex);
		}
		
		public int getLinkIndex(String inLabel)
		{
			boolean found = false;
			int i = 0;
			//start looping through each edge list in the vertex
			for(Object o : links)
			{
				//each edge is a vertex in it of itself
				DSAGraphVertex current = (DSAGraphVertex)o;
				
				//if the edge list contains the label
				if((current.getLabel()).equals(inLabel))
				{
					//output the position back out;
					found = true;
					break;
				}
				i++;
			}
			if(!found)
			{
				i = -1;
			}
			return i;
		}
		
		public void displayLinks()
		{
			DSAGraphVertex current = null;
			//System.out.print("Link List " + getLabel() + ":\n");
			for(Object o : links)
			{
				current = (DSAGraphVertex)o;
				System.out.print(current.getLabel() + "\n");
			}
			//System.out.println();
		}
	}
	
	//private class fields
	private DSALinkedList vertices;
	
	public DSAGraph()
	{
		vertices = new DSALinkedList();
	}
	
	//mutators
	public DSAGraphVertex addVertex(String inLabel)
	{
		DSAGraphVertex newVertex = new DSAGraphVertex(inLabel);
		vertices.insertLast(newVertex);
		return newVertex;
	}
	
	public void addEdge(String label1, String label2)
	{
		DSAGraphVertex vertex1 = getVertex(label1);
		DSAGraphVertex vertex2 = getVertex(label2);
		if(vertex1 != null && vertex2 != null)
		{
			vertex1.setLink(vertex2);
		}
	}
	
	public DSAGraphVertex getVertex(String inLabel)
	{
		String label;
		DSAGraphVertex vertex = null;
		DSAGraphVertex current;
		//Loop through all vertices
		for(Object o : vertices)
		{
			//Type cast Object vertices to DSAGraphVertex vertices
			current = (DSAGraphVertex)o;
			//Get the label of the current vertex
			label = current.getLabel();
			//If it equals to argument provided
			if(label.equals(inLabel))
			{
				//Return it and break the loop
				vertex = current;
				break;
			}
		}
		//if vertex not found
		if(vertex == null)
		{
			//create one
			vertex = addVertex(inLabel);
		}
		return vertex;
	}
	
	public DSAGraphVertex findVertex(String inLabel)
	{
		String label;
		DSAGraphVertex vertex = null;
		DSAGraphVertex current;
		//Loop through all vertices
		for(Object o : vertices)
		{
			//Type cast Object vertices to DSAGraphVertex vertices
			current = (DSAGraphVertex)o;
			//Get the label of the current vertex
			label = current.getLabel();
			//If it equals to argument provided
			if(label.equals(inLabel))
			{
				//Return it and break the loop
				vertex = current;
				break;
			}
		}

		return vertex;
	}
	
	public DSALinkedList getVertices()
	{
		return vertices;
	}
	
	public int getVertexCount()
	{
		int count = 0;
		for(Object o : vertices)
		{
			count++;
		}
		return count;
	}
	
	public boolean isEmpty()
	{
		return(getVertices().peekFirst() == null);

	}
	
	public int getEdgeCount(String inLabel)
	{
		DSAGraphVertex vertex = getVertex(inLabel);
		DSALinkedList lists;
		int count = 0;
		
		if(vertex == null)
		{
			throw new IllegalArgumentException("Vertex does not exist");
		}
		
		lists = vertex.getLinks();
		for(Object o : lists)
		{
			count++;
		}
		return count;
	}
	
	public DSALinkedList getAdjacent(String inLabel)
	{
		DSAGraphVertex vertex = findVertex(inLabel);
		
		if(vertex == null)
		{
			throw new IllegalArgumentException("Vertex does not exist");
		}
		
		return vertex.getLinks();
	}
	
	public boolean isAdjacent(String label1, String label2)
	{
		DSALinkedList adjacentLinks = getAdjacent(label1);
		DSAGraphVertex current = null;
		boolean found = false;
		for(Object o : adjacentLinks)
		{
			current = (DSAGraphVertex)o;
			if(current.getLabel().equals(label2))
			{
				found = true;
				break;
			}
		}
		return found;
	}
	
	public void removeVertexAndEdges(String inLabel)
	{
		int vertexIndex = -1;
		int temp = 0;
		int linkIndex;
		//Loop start looping throw all the vertices in the graph
		for(Object vertex : vertices)
		{
			//type cast the individual vertex
			DSAGraphVertex current = (DSAGraphVertex)vertex;
			//get the edges of the invidual vertex
			DSALinkedList currentList = (DSALinkedList)current.getLinks();
			//if the vertex label = inLabel
			if((current.getLabel()).equals(inLabel))
			{
				//proccess it for removal later on
				vertexIndex = temp;
			}
			//start going through the rest of the vertices
			else
			{
				//get the index containing the label in the edge list of the vertex
				linkIndex = current.getLinkIndex(inLabel);
				//if the label is in one of the edge list of the vertex
				if(linkIndex != -1)
				{
					//remove it
					currentList.remove(linkIndex);
				}
				
			}
			temp++;
		}
		//remove the vertex from the graph
		vertices.remove(vertexIndex);
	}
	
	public void displayAdjacent(String inLabel)
	{
		DSAGraphVertex vertex = findVertex(inLabel);
		if(vertex == null)
		{
			throw new IllegalArgumentException("Vertex does not exist");
		}
		vertex.displayLinks();
	}
}