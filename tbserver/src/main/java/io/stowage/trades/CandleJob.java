package io.stowage.trades;

import java.util.Set;

public class CandleJob {
	
	CandlesCollator collator;
	
	public CandleJob(CandlesCollator collator) {
		this.collator = collator;
	}
	
	public void processIt() {
		Set<String> symbols = collator.getSymbols();
		for(String ticker: symbols) {
			collator.putCandle(ticker);
		}
	}
	
}
