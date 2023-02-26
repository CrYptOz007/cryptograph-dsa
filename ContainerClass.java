/***********************************
* Author: Zhan Yi Cheok            *
* Program: Serialization class     *
* Date: 06/09/2020                 *
* Date Modified: 2/11/2020         *
***********************************/

import java.io.*;
import java.util.*;

/*******************************************************
*  Retrieved from Prac 4 							   *
*  Date Retrieved: 28/10/2020						   *
********************************************************/
public class ContainerClass implements Serializable
{
	public static void save(DSAGraph objToSave, String filename)
	{
		FileOutputStream fileStrm;
		ObjectOutputStream objStrm;
		try {
			fileStrm = new FileOutputStream(filename); // Underlying stream
			objStrm = new ObjectOutputStream(fileStrm); // Object serialization stream
			objStrm.writeObject(objToSave); // Serialize and save to filename
										//This will also save the ContainerClass’
										//contained Location object
			objStrm.close(); // Clean up
		}
		
		catch (Exception e) { 
			throw new IllegalArgumentException("Unable to save object to file");
		}
	} 
	
	public static DSAGraph load(String filename) throws IllegalArgumentException
	{
		FileInputStream fileStrm;
		ObjectInputStream objStrm;
		DSAGraph inObj = null;
		
		try {
			fileStrm = new FileInputStream(filename); // Underlying stream
			objStrm = new ObjectInputStream(fileStrm); // Object serialization stream
			inObj = (DSAGraph)objStrm.readObject(); // Deserialize. Note the cast is needed
			objStrm.close(); // Clean up
		}
		
		catch (ClassNotFoundException e) {
			System.out.println("Class DSAGraph not found" + e.getMessage());
		}
		
		catch (Exception e) {
			throw new IllegalArgumentException("Unable to load object from file");
		}
		
		return inObj;
	}
}