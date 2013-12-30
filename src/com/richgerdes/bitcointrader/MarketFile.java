package com.richgerdes.bitcointrader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MarketWatcher extends Thread {
	
	private ArrayList<Strategy> stratagies = new ArrayList<Strategy>();
	private ArrayList<Float> trades = new ArrayList<Float>();
	
	public MarketWatcher(){
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
			for(Strategy s : stratagies){
				s.tradeAt(value);
			}
		}
		
		for(Strategy s : stratagies){
			System.out.println(s.getName() + ": " + s.totalValueUSD(837));
			System.out.println(s.getName() + ": " + s.totalValueUSD(trades.get(trades.size() - 1)));
		}
	}

	public void addStratagy(Strategy s) {
		stratagies.add(s);
	}

}
