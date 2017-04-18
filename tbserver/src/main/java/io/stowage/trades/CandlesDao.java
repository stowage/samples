package io.stowage.trades;

import java.util.Date;
import java.util.List;

public interface CandlesDao {

	void record(String ticker, CandleType type, CandleObject candle);
	
	List<CandleObject> getCandles(String ticker, CandleType type, Date from, Date to);
	
}
