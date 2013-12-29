package com.richgerdes.bitcointrader;

public class Runner {

	public static void main(String[] args) {
		MarketWatcher m = new MarketWatcher();
		m.addStratagy(new PercentageStrategy(1000f, 0, 0.01f));
		
		m.start();
	}

}
