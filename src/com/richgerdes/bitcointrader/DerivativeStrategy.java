package com.richgerdes.bitcointrader;

import java.util.ArrayList;

public class DerivativeStrategy extends Strategy{

	ArrayList<Float> raw = new ArrayList<Float>();
	ArrayList<Float> smooth = new ArrayList<Float>();
	ArrayList<Float> firstDer = new ArrayList<Float>();
	ArrayList<Float> secondDer = new ArrayList<Float>();

	ArrayList<Float> buyPrices = new ArrayList<Float>();
	ArrayList<Float> sellPrices = new ArrayList<Float>();

	protected DerivativeStrategy(float startingUSD, float startingBTC) {
		super(startingUSD, startingBTC);
		
		name = "Deriv";
		
		turnOutput(false);
	}

	@Override
	public void tradeAt(float currentPrice) {
		
		raw.add(currentPrice);
		smoothLast();
		getDerivativesForLast();
		
		int id = raw.size() - 1;
		
		buy(id);
		sell(id);
		
	}

	private void smoothLast() {
		int id = raw.size() - 1;
		if(id == 0){
			smooth.add(raw.get(id));
		}else if(id == 1){
			smooth.add(raw.get(id) * 0.7f + raw.get(id - 1) * 0.3f);
		}else{
			smooth.add(raw.get(id) * 0.5f + raw.get(id - 1) * 0.3f + raw.get(id - 2) * 0.2f);
		}
		
	}

	private void getDerivativesForLast() {
		int id = smooth.size() - 1;
		if(id == 0){
			firstDer.add(0f);
			secondDer.add(0f);
		}else if(id == 1){
			firstDer.add(smooth.get(id) - smooth.get(id - 1));
			secondDer.add(0f);
		}else{
			firstDer.add(smooth.get(id) - smooth.get(id - 1));
			secondDer.add(firstDer.get(id) - firstDer.get(id - 1));
		}
	}

	private void buy(int id) {
		if(usd.getBalance() < 0.01f)
			return;
		
		if(firstDer.get(id) > 0.0f && secondDer.get(id) > 0.0f){
			float toBuy = usd.getBalance() / raw.get(id);
			
			output("Buying " + toBuy + "BTC @ " + raw.get(id) + "USD");
			
			usd.withdraw(toBuy * raw.get(id));
			btc.deposit(toBuy);
			
			buyPrices.add(raw.get(id));
		}
			
	}

	private void sell(int id) {
		if(btc.getBalance() <= 0)
			return;
		
		if(firstDer.get(id) < 0.0f && secondDer.get(id) < 0.0f  && (buyPrices.size() == 0 || raw.get(id) > buyPrices.get(buyPrices.size() - 1))){
			float toSell = btc.getBalance();
			
			output("Selling " + toSell + "BTC @ " + raw.get(id) + "USD");
			
			btc.withdraw(toSell);
			usd.deposit(toSell * raw.get(id));
			
			sellPrices.add(raw.get(id));
		}
	}

}
