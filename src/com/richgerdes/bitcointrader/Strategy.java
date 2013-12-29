package com.richgerdes.bitcointrader;

public abstract class Strategy {

	protected Account usd;
	protected Account btc;

	protected Strategy(float startingUSD, float startingBTC) {
		usd = new Account(startingUSD, "USD");
		btc = new Account(startingBTC, "BTC");
	}

	public void buyBTC(float amount, float price) {
		btc.deposit(amount);
		usd.withdraw(amount * price);
	}
	
	public void sellBTC(float amount, float price) {
		btc.withdraw(amount);
		usd.deposit(amount * price);
	}
	
	public float totalValueUSD(float btcPrice){
		return usd.getBalance() + btc.getBalance() * btcPrice;
	}
	
	public float totalValueBTC(float btcPrice){
		return usd.getBalance() / btcPrice + btc.getBalance();
	}
	
	public abstract void tradeAt(float currentPrice);
}
