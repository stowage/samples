package io.stowage.trades;

import java.util.Date;
import static org.junit.Assert.*;

import java.util.List;

public class CandlesDaoMock implements CandlesDao {

	@Override
	public void record(String ticker, CandleType type, CandleObject candle) {
		
		if(candle.getO() != 2 || candle.getC() !=4 || candle.getH() != 4 || candle.getL() !=1)
			fail("Got "+ candle.getTs() + "," + candle.getO() + "," + candle.getH() + "," + candle.getL() + "," + candle.getC());

	}

	@Override
	public List<CandleObject> getCandles(String ticker, CandleType type,
			Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

}
