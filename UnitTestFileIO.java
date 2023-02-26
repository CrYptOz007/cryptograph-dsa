import java.io.*;
import java.util.*;
public class UnitTestFileIO
{
	public static void main(String[] args)
	{
		readAssetFileTest();
		readTradePairsFileTest();
		readHttpResponse();
	}
	
	public static void readAssetFileTest()
	{
		DSALinkedList assetList = FileIO.readAssetFile("asset_info.csv");
		System.out.println("Succesfully loaded file");
		
		Asset coin = (Asset)assetList.peekFirst();
		System.out.println("First asset's price : " + coin.getPrice());
	}
	
	public static void readTradePairsFileTest()
	{
		String json = FileIO.readTradePairsFile("exchangeInfo.json");
		System.out.println("Succesfully read exchangeInfo.json file");
	}
	
	public static void readHttpResponse()
	{
		String response = FileIO.httpRequest("https://www.binance.com/api/v3/exchangeInfo");
		if(response.equals("Invalid"))
		{
			System.out.println("Failed to retrieve JSON String from URL");
		}
		else
		{
			System.out.println("Successfully retrieved JSON string from URL");
		}
	}
}
