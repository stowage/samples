package io.stowage.trades;

import java.util.Date;

public class QuoteObject {
	private double price;
	private Date ts;
	
	public QuoteObject(double price, Date ts) {
		super();
		this.price = price;
		this.ts = ts;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	
}
