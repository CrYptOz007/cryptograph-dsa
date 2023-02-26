/***********************************
* Author: Zhan Yi Cheok            *
* Program: Trade Transaction Info  *
* Date: 06/09/2020                 *
* Date Modified: 8/09/2020         *
***********************************/

public class TradeTransaction
{
	//private class fields
	private String symbol;
	private double askPrice;
	private double askQty;
	private double bidPrice;
	private double bidQty;
	private double quoteVolume;
	
	//Alternative constructor
	public TradeTransaction(String inSymbol, double inAskPrice, double inAskQty,
							double inBidPrice, double inBidQty, double inQuoteVolume)
	{
		symbol = inSymbol;
		askPrice = inAskPrice;
		askQty = inAskQty;
		bidPrice = inBidPrice;
		bidQty = inBidQty;
		quoteVolume = inQuoteVolume;
	}
	
	//print data horizontally
	public void printTradeTransactions()
	{
		System.out.format("%10s%15f%15f%15f%15f%20f\n", symbol, askPrice, askQty, bidPrice, bidQty, quoteVolume);
	}
}