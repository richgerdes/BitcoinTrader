package com.richgerdes.bitcointrader;

public class Runner {

	public static void main(String[] args) {
		MarketFile m = new MarketFile();
		m.addStratagy(new PercentageStrategy(1000f, 0, 0.01f));
		m.addStratagy(new DerivativeStrategy(1000f, 0));
		m.start();
	}

}
