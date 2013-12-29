package com.richgerdes.bitcointrader;

public class Account {
	
	private final float startingBalance;
	private String name;
	private float balance;
	
	public Account(float balance, String name){
		this.balance = balance;
		this.startingBalance = balance;
		this.name = name;
	}
	
	public void withdraw(float amount){
		balance -= amount;
	}
	
	public void deposit(float amount){
		balance += amount;
	}
	
	public float gain(){
		return balance - startingBalance;
	}
	
	public String getName(){
		return name;
	}
	
	public float getBalance(){
		return (float) Math.floor(balance * 100) / 100;
	}

}
