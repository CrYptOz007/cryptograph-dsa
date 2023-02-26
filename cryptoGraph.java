/***********************************
* Author: Zhan Yi Cheok            *
* Program: cryptoGraph       *
* Date: 29/10/2020                 *
* Date Modified: 2/11/2020         *
***********************************/

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.*;
import java.util.*;

public class cryptoGraph
{
	public static final String RESET = "\u001B[0m";
	public static final String BLACK = "\u001B[30m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String PURPLE = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";
	
	public static final String TRADE_PAIR_API = "https://www.binance.com/api/v3/trades?symbol=";
	public static final String MARKET_API = "https://www.binance.com/api/v3/exchangeInfo";
	public static final String ASSET_API = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?CMC_PRO_API_KEY=API_KEY_HERE";
	public static final String TRADE_24HR = "https://www.binance.com/api/v3/ticker/24hr";

	private static void usage()
	{
		System.out.println("Usage: java cryptoGraph <command> [<option> <value>] ");
		System.out.println("\n<command>:");
		System.out.println("\n	-i : interactive testing environment");
		System.out.println("	-r [option] : report mode");
		System.out.println("\n[option]:");
		System.out.println("\n	asset : specify the asset file to import");
		System.out.println("	trade : specify the trade file to export\n");
	}
	
	public static void main(String[] args)
	{
		if(args.length == 1 && args[0].equals("-i"))
		{
			interactive();
		}
		else if(args.length >= 3 && args.length <= 7 && args[0].equals("-r") && args[1].equals("asset"))
		{
			if(args.length == 3)
			{
				reportAsset(args[2], 10, 1);
			}
			else if(args.length >= 5 && args[3].equals("-q"))
			{
				if(args.length == 7 && args[5].equals("-pg"))
				{
					reportAsset(args[2], Integer.parseInt(args[4]), Integer.parseInt(args[6]));
				}
				else
				{
					reportAsset(args[2], Integer.parseInt(args[4]), 1);
				}
			}
			else if(args.length >= 5 && args[3].equals("-pg"))
			{
				if(args.length == 7 && args[5].equals("-q"))
				{
					reportAsset(args[2], Integer.parseInt(args[6]), Integer.parseInt(args[4]));
				}
				else
				{
					reportAsset(args[2], 10, Integer.parseInt(args[4]));
				}
			}
			else
			{
				System.out.println("Usage: cryptoGraph -r asset <asset_file.csv> [<option> <value> | <option> <value>]\n");
				System.out.println("[option]:\n");
				System.out.println("	-q : quantity per page");
				System.out.println("	-pg : page index");
				return;
			}
		}
		else if(args.length == 3 && args[0].equals("-r") && args[1].equals("trade"))
		{
			reportTrade(args[2]);
		}
		else
		{
			usage();
		}
	}
	
	private static void interactiveUsage()
	{
		System.out.println();
		System.out.println("1. Load Data");
		System.out.println("2. Find and display asset");
		System.out.println("3. Find and display trade details");
		System.out.println("4. Find and display potential trade paths");
		System.out.println("5. Set asset filter");
		System.out.println("6. Asset overview");
		System.out.println("7. Trade overview");
		System.out.println("8. Save data (serialised)");
		System.out.println("9. Exit");
		System.out.print("Enter from 1-9: ");
	}
	
	private static void interactive()
	{
		Scanner sc = new Scanner(System.in);
		DSALinkedList assetList = null;
		DSALinkedList tradePairHistory = new DSALinkedList();
		DSALinkedList tradeHistory = new DSALinkedList();
		DSAGraph graph = new DSAGraph();
		DSAStack filters = new DSAStack();
		Asset coin = null;
		int selection;
		String fileName, assetName, baseAsset, quoteAsset, filterAsset, tradePair, responseString;
		boolean found;
		
		try
		{
			do
			{
				interactiveUsage();
				selection = sc.nextInt();
				
				switch(selection)
				{
					/////////////////////////////////////////////////////////
					////////			Load Data 					/////////
					/////////////////////////////////////////////////////////
					case 1:
						System.out.println("	a. Load Asset");
						System.out.println("	b. Auto Load Asset");
						System.out.println("	c. Load Trade");
						System.out.println("	d. Auto Load Trade");
						System.out.println("	e. Load Serialized");
						System.out.print("Selection option: ");
						selection = sc.next().charAt(0);
						
						////////////////////////////////
						//////// Load Asset File ///////
						////////////////////////////////
						if(selection == 'A' || selection == 'a')
						{
							boolean loaded = true;
							System.out.print("Enter file name (.csv): ");
							fileName = sc.next();
							try
							{
								assetList = FileIO.readAssetFile(fileName);
							}
							catch(Exception e)
							{
								System.out.println(RED + e.getMessage() + RESET);
							}
							if(loaded)
							{
								System.out.println(GREEN + "Successfully imported asset file: " + YELLOW + fileName + RESET);
							}
						}
						/////////////////////////////////////
						//////// Auto Load Asset File ///////
						/////////////////////////////////////
						else if(selection == 'B' || selection == 'b')
						{
							assetList = new DSALinkedList();
							//get json string from http
							responseString = FileIO.httpRequest(ASSET_API);
							
							//convert it to a JSONObject
							JSONObject exchange = new JSONObject(responseString);
							
							//convert attritube 'data' value to an array
							JSONArray array = exchange.getJSONArray("data");
							
							//loop through data array
							for(int i = 0; i < array.length(); i++)
							{
								//start creating the asset/coin
								JSONObject asset = (JSONObject)array.get(i);
								JSONObject quote = asset.getJSONObject("quote");
								JSONObject usd = quote.getJSONObject("USD");
								coin = new Asset(
									asset.getInt("cmc_rank"),
									asset.getString("name"),
									asset.getString("symbol"),
									usd.getLong("market_cap"),
									String.format("%.2f", usd.getDouble("price")),
									asset.getLong("circulating_supply"),
									String.format("%.2f", usd.getDouble("volume_24h")),
									String.format("%.2f", usd.getDouble("percent_change_1h")),
									String.format("%.2f", usd.getDouble("percent_change_24h")),
									String.format("%.2f", usd.getDouble("percent_change_7d"))
								);
								
								//add it to the list
								assetList.insertLast(coin);
								
							}
							System.out.println(GREEN + "Successfully imported asset data from Coin Market Cap" + RESET );
						}
						////////////////////////////////
						//////// Load Trade File ///////
						////////////////////////////////
						else if(selection == 'C' || selection == 'c')
						{
							System.out.print("Enter file name (.json): ");
							fileName = sc.next();
							try
							{
								JSONTokener jsonToken = new JSONTokener(new FileReader(fileName));
								//valid json file
								if(jsonToken != null)
								{
									//reset graph
									graph = new DSAGraph();
									//turn to an object
									JSONObject exchange = new JSONObject(jsonToken);
									
									//get the array of objects
									JSONArray array = exchange.getJSONArray("symbols");
									
									//Loop through each array
									for(int i = 0; i < array.length(); i++)
									{
										//add the vertex directional path
										JSONObject symbol = (JSONObject)array.get(i);
										graph.addEdge(symbol.getString("baseAsset"), symbol.getString("quoteAsset"));
									}
									System.out.println(GREEN + "Successfully imported '" + fileName + "'" + RESET );
								}
							} catch(Exception e)
							{
								System.out.println(RED + e.getMessage() + RESET);
							}
						}
						/////////////////////////////////////
						//////// Auto Load Trade      ///////
						/////////////////////////////////////
						else if(selection == 'D' || selection == 'd')
						{	
							//reset graph
							graph = new DSAGraph();
							//get json string from http
							responseString = FileIO.httpRequest(MARKET_API);

							//turn to an object
							JSONObject exchange = new JSONObject(responseString);
								
							//get the array of objects
							JSONArray array = exchange.getJSONArray("symbols");
							
							for(int i = 0; i < array.length(); i++)
							{
								//add the vertex directional path
								JSONObject symbol = (JSONObject)array.get(i);
								graph.addEdge(symbol.getString("baseAsset"), symbol.getString("quoteAsset"));
							}
							System.out.println(GREEN + "Successfully imported trade data from Binance Market" + RESET );
						}
						/////////////////////////////////////
						//////// Load Serialised      ///////
						/////////////////////////////////////
						else if(selection == 'E' || selection == 'e')
						{
							System.out.print("Specify file name of serialized file: ");
							fileName = sc.next();
							int count = 0;
							
							//load serialized file and apply it to graph
							graph = ContainerClass.load(fileName);
							System.out.println(GREEN + "Successfully loaded serialized file: " + YELLOW + fileName + RESET);
						}
						else
						{
							System.out.println(RED + "Invalid option" + RESET);
						}
						break;
						
					/////////////////////////////////////////////////////////
					////////	Find and display asset   	 		/////////
					/////////////////////////////////////////////////////////
					case 2:
						if(assetList == null)
						{
							System.out.println(RED + "Please load asset file first" + RESET);
						}
						else
						{
							found = false;
							System.out.print("Enter asset symbol eg. 'btc': ");
							assetName = sc.next();
							assetName = assetName.toUpperCase();
							
							//Loop through the asset list
							for(Object asset : assetList)
							{
								//make sure its a valid asset
								if(asset != null)
								{
									//type cast it
									coin = (Asset)asset;
									//check the symbol with whats inputed
									if((coin.getSymbol()).equals(assetName))
									{
										//if it matches, we've found the asset
										found = true;
										break;
									}
								}
							}
							
							//if we found the asset
							if(found)
							{
								//print the details
								System.out.println(YELLOW + "\nAsset Overview:" + RESET);
								coin.display();
							}
							else
							{
								System.out.println(RED + "Asset not found" + RESET);
							}
						}
						break;
					/////////////////////////////////////////////////////////
					////////	Find and display trade pairs  		/////////
					/////////////////////////////////////////////////////////
					case 3:
						int limit = 0;
						//get user input
						System.out.print("Enter trade pair eg. 'ETHBTC': ");
						tradePair = sc.next();
						tradePair = tradePair.toUpperCase();
						
						//get json response with parameter set to inputed trade pair
						responseString = FileIO.httpRequest(TRADE_PAIR_API + tradePair);
						if(responseString.equals("Invalid"))
						{
							System.out.println(RED + "Invalid Trade Pair" + RESET);
						}
						else
						{
							//get the transaction history of the trade pair
							JSONArray history = new JSONArray(responseString);
							for(int i = 0; i < history.length(); i++)
							{
								//add it to the object
								JSONObject transaction = history.getJSONObject(i);
								TradePairs trans = new TradePairs(
									transaction.getInt("id"),
									transaction.getDouble("price"),
									transaction.getDouble("qty"),
									transaction.getDouble("quoteQty"),
									transaction.getLong("time"),
									transaction.getBoolean("isBuyerMaker"),
									transaction.getBoolean("isBestMatch")
								);
								//insert it to the list
								tradePairHistory.insertLast(trans);
							}
						}
						
						//print out the trade pair transactions
						System.out.println(YELLOW + "Showing latest 10 trade data for " + GREEN + tradePair + RESET);
						System.out.format("%15s%15s%15s%15s%16s%13s%13s\n", "ID", "Price", "Quantity", "Quote Qty", "Time", "Buyer Maker", "Best Match?");
						for(Object transaction : tradePairHistory)
						{
							//only print out first 10
							if(limit >= 10)
							{
								break;
							}
							//type cast back to original state and print
							TradePairs current = (TradePairs)transaction;
							current.printTradePair();
							limit++;
						}
						System.out.println();
						break;
					/////////////////////////////////////////////////////////
					/////// Find and display potential trade paths  /////////
					/////////////////////////////////////////////////////////
					case 4:
						System.out.print("Enter the base asset: ");
						baseAsset = sc.next();
						baseAsset = baseAsset.toUpperCase();
						System.out.print("Enter the quote asset: ");
						quoteAsset = sc.next();
						quoteAsset = quoteAsset.toUpperCase();
						
						//check if both are valid vertices in the graph
						if(graph.findVertex(baseAsset) != null && graph.findVertex(quoteAsset) != null)
						{						
							//show direct trades by printing out the directional adjacent of the two
							System.out.println(YELLOW + "\nDirect Trades:" + RESET);
							if(graph.isAdjacent(baseAsset, quoteAsset))
							{
								System.out.println(GREEN + baseAsset + RESET + "/" + RED + quoteAsset + RESET);
							}
							else
							{
								System.out.println(RED + "None" + RESET);
							}
							System.out.println();
							System.out.println(YELLOW + "Indirect Trades:" + RESET);
							printIndirectTrades(graph, baseAsset, quoteAsset);
						}
						else
						{
							System.out.println(RED + "Base or Quote Asset does not exist!" + RESET);
						}
						break;
					/////////////////////////////////////////////////////////
					/////// 			Asset/trade fiter  			/////////
					/////////////////////////////////////////////////////////
					case 5:
						boolean cont = true;
						int index;
						
						//if asset list and graph not loaded
						if(assetList == null && graph == null)
						{
							System.out.println(RED + "Please load asset or trade file first" + RESET);
							break;
						}
						System.out.print("Current filters: ");
						filters.printStack();
						System.out.print("Enter asset to blacklist: ");
						filterAsset = sc.next();
						filterAsset = filterAsset.toUpperCase();
						
						//check if the filter already exist
						for(Object o : filters)
						{
							if(o.equals(filterAsset))
							{
								System.out.println(RED + "Filter already exist" + RESET);
								cont = false;
								break;
							}
						}
						
						//if filter doesn't exist
						if(cont)
						{
							//push it onto the stack
							filters.push(filterAsset);
							
							/* For removing it off assetList as well
							if(assetList != null)
							{
								try
								{
									index = assetList.getPosition(filterAsset);
									assetList.remove(index);
								} catch(Exception e)
								{
									System.out.println(e.getMessage());
								}
							}
							*/
							
							try
							{
								if(!graph.isEmpty())
								{
									graph.removeVertexAndEdges(filterAsset);
									System.out.println(GREEN + "Succesfully filtered out: " +YELLOW+ filterAsset + RESET);
								}
							}catch(Exception e)
							{}
						}
						break;
					/////////////////////////////////////////////////////////
					/////// 			Asset Overview				/////////
					/////////////////////////////////////////////////////////
					case 6:
						int count = 0;
						if(assetList == null)
						{
							System.out.println(RED + "Please load asset file first" + RESET);
						}
						else
						{
							System.out.println(YELLOW + "\nDisplaying top 10 assets by Market Cap" + RESET);
							System.out.format("%5s%15s%7s%20s%15s%22s%19s%8s%8s%8s", "Rank", "Name", "Symbol", "Market Cap (USD)", "Price (USD)", "Circulating Supply", "Volume (USD)", "% 1Hr", "% 24Hr", "% 7D");
							System.out.println();
							//loop through each asset in list
							for(Object asset : assetList)
							{
								//only show 10 rows
								if(count >= 10)
								{
									break;
								}
								else
								{
									//type cast it to original state
									coin = (Asset)asset;
									//start printing horizontally
									System.out.format("%5d", count+1);
									coin.displayHorizontal();
									System.out.println();
									count++;
								}
							}
						}
						break;
					case 7:
						count = 0;
						System.out.print("How many rows to display: ");
						limit = sc.nextInt();
						//get list of latest trade history 
						tradeHistory = latestTradeHistory(tradeHistory);
						
						System.out.println(YELLOW + "Showing latest " + limit + " trade data from Binance " + RESET);
						System.out.format("%10s%15s%15s%15s%15s%20s\n", "Symbol", "Ask Price", "Ask Qty", "Bid Price", "Bid Qty", "Quote Volume");
						//for each transaction in the history
						for(Object transaction : tradeHistory)
						{
							// stop at user inputed to display
							if(count >= limit)
							{
								break;
							}
							//typecast to original to print data
							TradeTransaction current = (TradeTransaction)transaction;
							current.printTradeTransactions();
							count++;
						}
						System.out.println();
						break;
					case 8:
						System.out.print("Name of serialized file to export: ");
						fileName = sc.next();
						
						ContainerClass.save(graph, fileName);
				}
				
			} while(selection != 9);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	//printing indirect trade paths
	private static void printIndirectTrades(DSAGraph graph, String baseAsset, String quoteAsset)
	{
		DSAStack baseStack = new DSAStack();
		DSAStack finalStack = new DSAStack();
		String quoteString = "";
		//get the directional paths of baseAsset
		DSALinkedList baseList = graph.getAdjacent(baseAsset);
		//get the directional paths of quoteAsset
		DSALinkedList quoteList = graph.getAdjacent(quoteAsset);
		String asset;
		int baseCount = 0;
		int quoteCount = 0;
		int finalCount = 0;
		
		//loop through baseVertex
		for(Object edge : baseList)
		{
			DSAGraph.DSAGraphVertex current = (DSAGraph.DSAGraphVertex)edge;
			//push it onto a stack
			baseStack.push(current.getLabel());
			baseCount++;
		}
		
		//loop through quoteVertex
		for(Object edge : quoteList)
		{
			DSAGraph.DSAGraphVertex current = (DSAGraph.DSAGraphVertex)edge;
			//trucate it onto a string
			quoteString += current.getLabel() + ",";
			quoteCount++;
		}
		
		//if both have > 1 directional path
		if(baseCount > 0 && quoteCount > 0)
		{
			do
			{
				//remove the asset off base stack
				asset = (String)baseStack.pop();
				//check if quote edges has the asset equal base edges
				if(quoteString.contains(asset))
				{
					//if it does, push it to the final stack
					finalStack.push(asset);
					finalCount++;
				}
				baseCount--;
			} while(baseCount != 0);
		}
			
		//print the final stack
		do
		{
			asset = (String)finalStack.pop();
			System.out.printf("%s%s%s/%s%s", GREEN, baseAsset, RESET, CYAN, asset);
			System.out.format("%7s", asset);
			System.out.printf("%s/%s%s%s\n", RESET, RED, quoteAsset, RESET);
			finalCount--;
		} while(finalCount != 0);
	}
	
	private static DSALinkedList latestTradeHistory(DSALinkedList tradeHistory)
	{
		tradeHistory = new DSALinkedList();
		String responseString = FileIO.httpRequest(TRADE_24HR);
		JSONArray history = new JSONArray(responseString);
		
		for(int i = 0; i < history.length(); i++)
		{
			JSONObject trade = history.getJSONObject(i);
			TradeTransaction trans = new TradeTransaction(
				trade.getString("symbol"),
				Double.parseDouble(String.format("%4f", trade.getDouble("askPrice"))),
				Double.parseDouble(String.format("%4f", trade.getDouble("askQty"))),
				Double.parseDouble(String.format("%4f", trade.getDouble("bidPrice"))),
				Double.parseDouble(String.format("%4f", trade.getDouble("bidQty"))),
				Double.parseDouble(String.format("%4f", trade.getDouble("quoteVolume")))
			);
			tradeHistory.insertLast(trans);
		}
		return tradeHistory;
	}
	
	private static void reportAsset(String fileName, int quantity, int page)
	{
		DSALinkedList assetList = null;
		Asset coin;
		int count = 0;
		int skip = 0;
		int skipMax = quantity*(page-1);
		int max = quantity * page;
		try
		{
			assetList = FileIO.readAssetFile(fileName);
			System.out.println(YELLOW + "\nDisplaying assets by Market Cap" + RESET);
			System.out.format("%5s%15s%7s%20s%15s%22s%19s%8s%8s%8s\n", "Rank", "Name", "Symbol", "Market Cap", "Price (USD)", "Circulating Supply", "Volume (USD)", "% 1Hr", "% 24Hr", "% 7D");
			
			//loop through each asset in list
			for(Object asset : assetList)
			{
				//only show quantity amount and stop at quantity *page
				if(skip < skipMax)
				{
					skip++;
					count = skipMax;
				}
				else
				{
					//reached end of page
					if(count >= max)
					{
						break;
					}
					else
					{
						//type cast it to original state
						coin = (Asset)asset;
						System.out.format("%5d", count+1);
						//start printing horizontally
						coin.displayHorizontal();
						System.out.println();
						count++;
					}
				}
			}
			System.out.println(CYAN + "Displaying: " + RED + quantity + CYAN + " rows on Page: " + RED + page + RESET);
			
		}
		catch(Exception e)
		{}
		System.out.println("Advanced Usage: java cryptoGraph -r <asset_file.csv> [<option> <value> | <option> <value>]\n");
		System.out.println("[option]:\n");
		System.out.println("	-q : quantity per page");
		System.out.println("	-pg : page index");
		
	}
	
	private static void reportTrade(String fileName)
	{
		try
		{
			PrintStream originalOut = System.out;  // save original ouput
			PrintStream fileOut = new PrintStream(fileName); // file showing console output
			//reading console output
			System.setOut(fileOut);
			
			DSALinkedList tradeHistory = new DSALinkedList();
			tradeHistory = latestTradeHistory(tradeHistory);
			
			System.out.println("#Showing latest 24hr trade data from Binance#");
			System.out.format("%10s%15s%15s%15s%15s%20s\n", "Symbol", "Ask Price", "Ask Qty", "Bid Price", "Bid Qty", "Quote Volume");
			
			// for each transaction in trade history
			for(Object transaction : tradeHistory)
			{
				//typecast back to original
				TradeTransaction current = (TradeTransaction)transaction;
				//print value
				current.printTradeTransactions();
			}
			System.out.println();
			
			System.setOut(originalOut); // restore output
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(GREEN + "24Hr Binance trade data saved to " + YELLOW + fileName + RESET);
	}
	
}