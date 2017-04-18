package io.stowage.trades;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CandleObject {
	
	private Lock candleLock = new ReentrantLock();
	
	private double o;
	private double h;
	private double l;
	private double c;
	private Date ts;
	
	public CandleObject(double o, double h, double l, double c, Date ts) {
		this.o = o;
		this.h = h;
		this.l = l;
		this.c = c;
		this.ts = ts;
	}
	
	public CandleObject() {
		
	}

	public double getO() {
		return o;
	}

	public void setO(double o) {
		this.o = o;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getL() {
		return l;
	}

	public void setL(double l) {
		this.l = l;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	public void lock() {
		candleLock.lock();
	}
	
	public void unlock() {
		candleLock.unlock();
	}

	
}
