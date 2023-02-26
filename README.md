# Crypto Graph Program

This program is designed to give valuable information on cryptocurrency assets and tradeable options. It allows the user to generate an exportable report of recent trade data as well as provide an interactive menu which the user can do various functions available in the program.

## Requirements

Make sure Java JDK11+ is installed

To check version
```bash
java --version
```
Output

```bash
openjdk 11.0.9 2020-10-20
OpenJDK Runtime Environment (build 11.0.9+11-Ubuntu-0ubuntu1.18.04.1)
OpenJDK 64-Bit Server VM (build 11.0.9+11-Ubuntu-0ubuntu1.18.04.1, mixed mode, sharing)

```

Install Java JDK

```bash
sudo apt-get install openjdk-11-jdk
```

## Installation

Use the java compiler to compile the program

```bash
javac *.java
```

## Usage

To get usage information

```bash
java cryptoGraph
```

To run the interactive mode

```bash
java cryptoGraph -i
```

To run the report mode

`-r asset <assetfile.csv>` : Show report from asset file
`-r trade <exportedfile.txt> : Export latest trade history from binance to a file

Example:
```bash
java cryptoGraph -r asset asset_info.csv
```

Example 2:
```bash
java cryptoGraph -r trade exported.txt
```

Advanced Report Mode

`-r asset <assetfile.csv> -pg <value> -q <value>` : `-pg` is page and `-q` is quantity

Example
```bash
java cryptoGraph -r asset asset_info.csv -pg 5 -q 10
```
Show 10 rows of assets on page 5

## Files
`cryptoGraph.java` – Main program file that contains the Main method for running the menu and other functions with set arguments
`DSALinkedList.java` – Modified Doubly-ended linked list. This is the main data type for most of the classes in the program
`DSAGraph.java` – Similar implementation of an Adjacency list with linked list. This is used for analysing trade paths between two assets
`DSAStack.java` – Implementation of Java Stack and uses Linked List. This is used for the asset filter and for comparing common assets of two indirect assets
`ContainerClass.java` – Java Serialization driver code to save and load serialized data
`TradePairs.java` – Simple class that holds the data of all trades associated to a specific trade pair 
`TradeTransactions.java` – Simple class that holds all the different transactions of cryptocurrency
`Asset.java` – Simple class that holds information about a particular asset
`FileIO.java` – File importing and processing of an asset file as well as a JSON http GET API call
`UnitDSAGraphTest.java` – Test file for DSAGraph.java
`UnitDSALinkedListTest.java` – Test file for DSALinkedListTest.java
`UnitFileIOTest.java` – Test file for FileIO.java
`asset_info.csv` – Asset file extracted from coinmarketcap API
`exchangeInfo.json` – Json file extracted from binance API
`org/json/*` - JSON Package from www.github.com/stleary/JSON-java
