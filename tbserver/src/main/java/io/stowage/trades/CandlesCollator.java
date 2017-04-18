package io.stowage.trades;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

public class CandlesCollator {

	private final ConcurrentMap<String, PriorityBlockingQueue<QuoteObject>> quotes;
	private final ConcurrentMap<String, ConcurrentMap<CandleType, CandleObject>> candles;
	
	private CandlesDao candlesDao;
	
	private final Comparator<QuoteObject> candlesOrderCompare;
	
	public CandlesCollator() {
		this.quotes = new ConcurrentHashMap<String, PriorityBlockingQueue<QuoteObject>>();
		this.candles = new ConcurrentHashMap<String, ConcurrentMap<CandleType, CandleObject>>();
		this.candlesOrderCompare = new Comparator<QuoteObject>() {
			@Override
		    public int compare(QuoteObject quote1, QuoteObject quote2) {
				return quote1.getTs().compareTo(quote2.getTs());
			}
		};
	}
	
	public void putIntoQueue(String ticker, QuoteObject quote) {
		Queue<QuoteObject> queue = quotes.get(ticker);
		
		if(queue == null) {
			PriorityBlockingQueue<QuoteObject> newQueue 
				= new PriorityBlockingQueue<QuoteObject>(10, candlesOrderCompare);
			queue = quotes.putIfAbsent(ticker, newQueue);
			
			if(queue == null) {
				queue = newQueue;
			}
		}
		
		queue.offer(quote);
	}
	
	@Async
	public void putCandle(String ticker) {
		
		ConcurrentMap<CandleType, CandleObject> candle = candles.get(ticker);
		
		if(candle == null) {
			ConcurrentMap<CandleType, CandleObject> newCandle = new ConcurrentHashMap<CandleType, CandleObject>();
			candle = candles.putIfAbsent(ticker, newCandle);
			
			if(candle == null) {
				candle = newCandle;
			}
		}
		
		Queue<QuoteObject> queue = quotes.get(ticker);
		
		if(queue == null) 
			return;
		
		while(!queue.isEmpty()) {
			QuoteObject quote = queue.poll();
			
			calculate(ticker, candle, quote);
		}
	
	}
	
	private void calculate(String ticker, ConcurrentMap<CandleType, CandleObject> candle, QuoteObject quote) {
		Date quoteTs = quote.getTs();
		double price = quote.getPrice();

		for(CandleType type: CandleType.values()) {
			CandleObject candleByType = candle.get(type);
			
			if(candleByType == null) {
				CandleObject newCandleByType = new CandleObject(price, price, price, price, 
						CandleUtils.getCandlePeriod(type, quoteTs));
				
				candleByType = candle.putIfAbsent(type, newCandleByType);
				
				if(candleByType == null) {
					candleByType = newCandleByType;
				}
			}
			
			adjustCandle(ticker, type, candleByType, quote);
				
		}
	}
	
	public CandleObject getLastCandle(String ticker, CandleType type) {
		ConcurrentMap<CandleType, CandleObject> candle = candles.get(ticker);
		CandleObject candleToReturn = new CandleObject();
		
		if(candle != null) {
			CandleObject candleByType = candle.get(type);
			if(candleByType != null) {
				try {
					candleByType.lock();
					
					candleToReturn.setO(candleByType.getO());
					candleToReturn.setH(candleByType.getH());
					candleToReturn.setL(candleByType.getL());
					candleToReturn.setC(candleByType.getC());
					candleToReturn.setTs(candleByType.getTs());
					
					return candleToReturn; 
					
				} finally {
					candleByType.unlock();
				}
			}
		}
		
		return null;
	}
	
	private void adjustCandle(String ticker, CandleType type, CandleObject candle, QuoteObject quote) {
		Date quoteTs = quote.getTs();
		
		double price = quote.getPrice();
		
		try {
			candle.lock();
			Date candleTs = candle.getTs();
			
			if(CandleUtils.belongsToCandle(type, candleTs, quoteTs)) {
				if(price > candle.getH())
					candle.setH(price);
				if(price < candle.getL())
					candle.setL(price);
				
				candle.setC(price);
			} else {
				//TODO: candle timestamp could be invalid how should we avoid this
				//TODO: try to find other solution
				if(CandleUtils.isCandlePeriodNotLess(type, candleTs, quoteTs)) {
					candlesDao.record(ticker, type, new CandleObject(candle.getO(),
						candle.getH(), candle.getL(), candle.getC(), 
						candle.getTs()));
				}
				
				candle.setO(price);
				candle.setH(price);
				candle.setL(price);
				candle.setC(price);
				candle.setTs(CandleUtils.getCandlePeriod(type, quoteTs));

			}
			
		} finally {
			candle.unlock();
		}
		
	}
	
	public Set<String> getSymbols() {
		return Collections.unmodifiableSet(quotes.keySet());
	}

	public CandlesDao getCandlesDao() {
		return candlesDao;
	}

	@Autowired
	public void setCandlesDao(CandlesDao candlesDao) {
		this.candlesDao = candlesDao;
	}
	
	
}
