package com.richgerdes.bitcointrader;

public class PercentageStrategy extends Strategy{
	
	private float peak;
	private float trough;
	private float lastSell;
	private float lastBuy;
	
	private float factor = 0.01f;
	
	private boolean firstTrade = true;
	
	public PercentageStrategy(float usd, float btc, float factor){
		super(usd, btc);
		this.factor = factor;
	}

	@Override
	public void tradeAt(float currentPrice) {
		if(firstTrade){
			peak = trough = lastSell = lastBuy = currentPrice;
			firstTrade = false;
		}
		
		if(peak < currentPrice){
			peak = currentPrice;
			//System.out.println("New Peak: " + peak);
		}
		if(trough > currentPrice){
			trough = currentPrice;
			//System.out.println("New Trough: " + trough);
		}
		
		buy(currentPrice);
		sell(currentPrice);
		
	}
	
	private void buy(float currentPrice){
		
		if(usd.getBalance() < 0.01f)
			return;
		
		if(currentPrice > trough && (currentPrice - trough) / trough > 0.01f){
			float toBuy = usd.getBalance() / currentPrice;

			System.out.println("Buying " + toBuy + "BTC @ " + currentPrice + "USD");
			
			btc.deposit(toBuy);
			usd.withdraw(toBuy * currentPrice);
			
			lastBuy = currentPrice;
			trough = currentPrice;
		}
		
	}
	
	private void sell(float currentPrice){
		if(btc.getBalance() < 0.01f)
			return;
		
		if(currentPrice < lastBuy)
			return;
		
		if(currentPrice > trough && (peak - currentPrice) / (peak - lastBuy) > 0.05f){
			float toSell = btc.getBalance();

			System.out.println("Selling " + toSell + "BTC @ " + currentPrice + "USD");
			
			btc.withdraw(toSell);
			usd.deposit(toSell * currentPrice);
			
			lastSell = currentPrice;
			peak = currentPrice;
		}
	}

}
