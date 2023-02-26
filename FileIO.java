/*****************************************
* Author: Zhan Yi Cheok            	     *
* Purpose: To read/write student files   *
* Date: 8/08/2020                 	     *
* Date Modified: 11/08/2020        		 *
*****************************************/

import java.io.*;
import java.util.*;
import java.net.*;

class FileIO implements Serializable
{
	public static HttpURLConnection connection;
	
	/*******************************************************
	*  Modified from Prac 1 							   *
	*  Date Retrieved: 28/10/2020						   *
	********************************************************/
	public static DSALinkedList readAssetFile(String inFilename) 
	{
		FileInputStream fileStrm = null;
		InputStreamReader rdr;
		BufferedReader bufRdr;
		int lineNum;
		Asset csvRow;
		String line;
		DSALinkedList assetList = new DSALinkedList();
		try {
			fileStrm = new FileInputStream(inFilename); // Open the file
			rdr = new InputStreamReader(fileStrm); // Create a reader to read the stream
			bufRdr = new BufferedReader(rdr); // To read the stream one line at a time
			lineNum = 0;
			line = bufRdr.readLine(); // Read the first line - no data
			line = bufRdr.readLine(); // Read the second line - no data
			line = bufRdr.readLine(); // Read the third line - start of data
			while (line != null) // While not end-of-file, process and read lines
			{ 
				lineNum++;
				csvRow = processLine(line); // Whatever processing on the line is required
				assetList.insertLast(csvRow); 
				line = bufRdr.readLine(); // Read the next line
			}
			fileStrm.close(); // Clean up the stream
		}
		catch (IOException e) // MUST catch IOExceptions
		{ 
			if (fileStrm != null) // Clean up the stream if it was opened
			{ 
				try 
				{ 
					fileStrm.close(); 
				} 
				catch (IOException ex2) { } // We can’t do anything more!
			}
		System.out.println("Error in file processing: " + e.getMessage()); // Or do a throw
		}
		
		return assetList;
	}
	
	private static Asset processLine(String csvRow) throws IllegalStateException
	{
		Asset crypto = new Asset();
		String circSupply;
		String thisToken = null;
		String current;
		int index;
		double price, volume;
		boolean found = false;
		StringTokenizer strTok;
		
		strTok = new StringTokenizer(csvRow, ",");
		try {
			crypto.setRank(Integer.parseInt(strTok.nextToken()));
			crypto.setName(strTok.nextToken());
			crypto.setSymbol(strTok.nextToken());
			
			//Begin skipping market cap (unclean)
			//Contains , seperators in the number
			//that we have to skip over
			do
			{
				current = strTok.nextToken();
				/*check to see if its reached the end of marketcap (unclean)
				usually contains a space at the end inside the asset file
				to denote the end*/ 
				index = current.lastIndexOf(" ");
				if(index > 0)
				{
					found = true;
				}
			}while(!found);
			found = false;
			
			crypto.setMarketCap((long)Double.parseDouble(strTok.nextToken()));
			
			//reset current strtoken
			current = "";
			/*start formating the price*/
			do
			{
				current = current + strTok.nextToken();
				//finds the end of the data type
				index = current.lastIndexOf(" ");
				
				//if the price data type is a double containing scientific notation (some do)
				try
				{
					price = Double.parseDouble(current);
					//set it and end the loop
					crypto.setPrice(current);
					found = true;
				} catch(Exception e)
				{}
				
				//otherwise continue checking for space at the end
				if(index > 0)
				{
					crypto.setPrice(current);
					found = true;
				}
			}while(!found);
			found = false;
			
			//skip over circulating supply (unclean)
			do
			{
				current = strTok.nextToken();
				index = current.lastIndexOf(" ");
				if(index > 0)
				{
					found = true;
				}
			}while(!found);
			found = false;

			
			circSupply = strTok.nextToken();
			//if circulating supply (clean) = ?
			if(circSupply.equals("?"))
			{
				crypto.setCircSupply(0);
			}
			else
			{
				crypto.setCircSupply((long)Double.parseDouble(circSupply));
			}
			
			//reset current stroken
			current = "";
			do
			{
				current += strTok.nextToken();
				//check if strtoken contains space = end of data type section
				index = current.lastIndexOf(" ");
				//some are doubles containing scientific notation
				try
				{
					volume = Double.parseDouble(current);
					crypto.setVolume(current);
					//end the loop
					found = true;
				} catch(Exception e)
				{}
				
				//if strtoken doesn't have a space
				if(current.equals("$?"))
				{
					crypto.setVolume(current);
					found = true;
				}
				//if it does
				else if(index > 0)
				{
					crypto.setVolume(current);
					found = true;
				}
			}while(!found);
			found = false;
			
			crypto.setOneHr(strTok.nextToken());
			crypto.setTweentyFourHr(strTok.nextToken());
			crypto.setSevenDays(strTok.nextToken());
		}
		catch (Exception e) {
			throw new IllegalStateException("CSV row had invalid format"); 
		}	
		return crypto;
	}
	
	public static String readTradePairsFile(String inFilename) 
	{
		FileInputStream fileStrm = null;
		InputStreamReader rdr;
		BufferedReader bufRdr;
		int lineNum;
		String json = "";
		String line;
		try {
			fileStrm = new FileInputStream(inFilename); // Open the file
			rdr = new InputStreamReader(fileStrm); // Create a reader to read the stream
			bufRdr = new BufferedReader(rdr); // To read the stream one line at a time
			lineNum = 0;
			line = bufRdr.readLine(); // Read the first line - no data
			while (line != null) // While not end-of-file, process and read lines
			{ 
				lineNum++;
				json += line; // Whatever processing on the line is required
				line = bufRdr.readLine(); // Read the next line
			}
			fileStrm.close(); // Clean up the stream
		}
		catch (IOException e) // MUST catch IOExceptions
		{ 
			if (fileStrm != null) // Clean up the stream if it was opened
			{ 
				try 
				{ 
					fileStrm.close(); 
				} 
				catch (IOException ex2) { } // We can’t do anything more!
			}
		System.out.println("Error in file processing: " + e.getMessage()); // Or do a throw
		}
		
		return json;
	}
	
	/********************************************************************
	*  Retrieved from: https://www.youtube.com/watch?v=qzRKa8I36Ww	    *
	*  Date Retrieved: 30/10/2020						   				*
	*********************************************************************/
	public static String httpRequest(String uri)
	{
		BufferedReader reader;
		String line;
		StringBuffer responseContent = new StringBuffer();
		try
		{
			URL url = new URL(uri);
			connection = (HttpURLConnection) url.openConnection();
			
			//request setup
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			int status = connection.getResponseCode();
			
			if(status > 299)
			{
				responseContent.append("Invalid");
			}
			else
			{
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line = reader.readLine()) != null)
				{
					responseContent.append(line);
				}
				reader.close();
			}
		} catch(MalformedURLException e)
		{
			e.printStackTrace();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		return responseContent.toString();
	}
}