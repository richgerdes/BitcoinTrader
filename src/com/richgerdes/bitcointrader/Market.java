package com.richgerdes.bitcointrader;

import java.util.ArrayList;

public abstract class Market extends Thread{

	private ArrayList<Strategy> stratagies = new ArrayList<Strategy>();
	
	public void addStratagy(Strategy s) {
		stratagies.add(s);
	}
	
	public abstract void run();

	protected void tradeAt(float price) {
		for (Strategy s : stratagies) {
			s.tradeAt(price);
		}
	}
	
	protected void printStradegyWorths(float price){
		for(Strategy s : stratagies){
			System.out.println(s.getName() + ": " + s.totalValueUSD(price));
		}
	}

}
