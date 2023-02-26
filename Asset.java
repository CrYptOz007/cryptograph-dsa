/***********************************
* Author: Zhan Yi Cheok            *
* Program: Contain asset/coin data *
* Date: 30/10/2020                 *
* Date Modified: 2/11/2020         *
***********************************/
import java.io.*;
import java.util.*;

public class Asset implements Serializable
{
	private int rank;
	private String name;
	private String symbol;
	private long marketCap;
	private String price;
	private long circSupply;
	private String volume;
	private String oneHr;
	private String tweentyfourHr;
	private String sevenDays;
	
	public Asset()
	{
		rank = 0;
		name = null;
		symbol = null;
		marketCap = 0;
		price = null;
		circSupply = 0;
		volume = null;
		oneHr = null;
		tweentyfourHr = null;
		sevenDays = null;
	}
	
	//Alternate Constructor
	public Asset(int inRank, String inName, String inSymbol, 
				long inMarketCap, String inPrice, long inCircSupply, String inVolume,
				String inOneHr, String in24Hr, String in7D)
	{
		rank = inRank;
		name = inName;
		symbol = inSymbol;
		marketCap = inMarketCap;
		price = inPrice;
		circSupply = inCircSupply;
		volume = inVolume;
		oneHr = inOneHr;
		tweentyfourHr = in24Hr;
		sevenDays = in7D;
	}
	
	public void setRank(int inRank)
	{
		rank = inRank;
	}
	
	public void setName(String inName)
	{
		name = inName;
	}
	
	public void setSymbol(String inSymbol)
	{
		symbol = inSymbol;
	}
	
	public void setMarketCap(long inMarketCap)
	{
		marketCap = inMarketCap;
	}
	
	public void setPrice(String inPrice)
	{
		price = inPrice;
	}
	
	public void setCircSupply(long inCircSupply)
	{
		circSupply = inCircSupply;
	}
	
	public void setVolume(String inVolume)
	{
		volume = inVolume;
	}
	
	public void setOneHr(String inOneHr)
	{
		oneHr = inOneHr;
	}
	
	public void setTweentyFourHr(String inTweentyFourHr)
	{
		tweentyfourHr = inTweentyFourHr;
	}
	
	public void setSevenDays(String inSevenDays)
	{
		sevenDays = inSevenDays;
	}
	
	public int getRank()
	{
		return rank;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getSymbol()
	{
		return symbol;
	}
	
	public long getMarketCap()
	{
		return marketCap;
	}

	public String getPrice()
	{
		return price;
	}
	
	public long getCircSupply()
	{
		return circSupply;
	}
	
	public String getVolume()
	{
		return volume;
	}
	
	public String getOneHr()
	{
		return oneHr;
	}
	
	public String getTweentyfourHr()
	{
		return tweentyfourHr;
	}
	
	public String getSevenDays()
	{
		return sevenDays;
	}
	
	public void display()
	{
		System.out.println("Name: " + getName());
		System.out.println("Symbol: " + getSymbol());
		System.out.println("Market Cap: ~$" + getMarketCap());
		System.out.println("Price: " + getPrice());
		System.out.println("Circulating Supply: " + getCircSupply() + " " + getSymbol());
		System.out.println("Volume: " + getVolume());
		System.out.println("% 1h: " + getOneHr());
		System.out.println("% 24h: " + getTweentyfourHr());
		System.out.println("% 7d: " + getSevenDays());
	}
	
	public void displayHorizontal()
	{
		System.out.format("%15s", getName());
		System.out.format("%7s", getSymbol());
		System.out.format("%20s", getMarketCap());
		System.out.format("%15s", getPrice());
		System.out.format("%18s %s", getCircSupply(), getSymbol());
		System.out.format("%19s", getVolume());
		System.out.format("%8s", getOneHr());
		System.out.format("%8s", getTweentyfourHr());
		System.out.format("%8s", getSevenDays());
	}
	
	
}