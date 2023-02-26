/***********************************
* Author: Zhan Yi Cheok            *
* Program: Trade pairs class       *
* Date: 30/10/2020                 *
* Date Modified: 2/11/2020         *
***********************************/

public class TradePairs
{
	private int id;
	private double price;
	private double qty;
	private double quoteQty;
	private long time;
	private boolean isBuyerMaker;
	private boolean isBestMatch;
	
	public TradePairs(int inId, double inPrice, double inQty, double inQuoteQty, long inTime, boolean buyerMatch, boolean bestMatch)
	{
		id = inId;
		price = inPrice;
		qty = inQty;
		quoteQty = inQuoteQty;
		time = inTime;
		isBuyerMaker = buyerMatch;
		isBestMatch = bestMatch;
	}
	
	public void printTradePair()
	{
		System.out.format("%15d%15f%15f%15f%16d%11s%11s\n", id, price, qty, quoteQty, time, Boolean.toString(isBuyerMaker), Boolean.toString(isBestMatch));
	}
}