package com.richgerdes.bitcointrader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MarketFile extends Market {

	private ArrayList<Float> trades = new ArrayList<Float>();
	
	public MarketFile(){
		importTrades();
	}
	
	private void importTrades() {
		//Import Trades
		
		String inputFile = "mtgoxUSD.txt";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(inputFile));
		
			String read = "";
			while((read = br.readLine()) != null){
				
				String[] arr = read.split(", ");
				
				for(String a : arr){
					trades.add(Float.parseFloat(a));
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}

	public void run(){
		
		for(float value : trades){
			tradeAt(value);
		}
		
		printStradegyWorths(837);
		printStradegyWorths(trades.get(trades.size() - 1));
	}
}
