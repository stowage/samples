package io.stowage.trades;

import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

public class CandlesCollatorTest {
	
	private CandlesCollator collator;
	
	@Before
	public void setUp() throws Exception {
		collator = new CandlesCollator();
		collator.setCandlesDao(createNiceMock(CandlesDaoImpl.class));
    }
	
	
	@Test
	public void candleWriteTest() throws ParseException {
		collator.putIntoQueue("GBPUSD", new QuoteObject(10, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:00")));
		collator.putIntoQueue("GBPEUR", new QuoteObject(12, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:00")));
		collator.putCandle("GBPUSD");
		collator.putCandle("GBPEUR");
		CandleObject candle = collator.getLastCandle("GBPUSD", CandleType.M1);
		if(candle.getC() != 10)
			fail("Got " + candle.getC() + "but should be 10");
		CandleObject candle2 = collator.getLastCandle("GBPEUR", CandleType.D1);
		if(candle2.getC() != 12)
			fail("Got " + candle2.getO() + "but should be 12");

		
	}
	
	@Test
	public void candleOHLCTest() throws ParseException {
		collator.putIntoQueue("GBPUSD", new QuoteObject(2, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:00")));
		collator.putIntoQueue("GBPUSD", new QuoteObject(1, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:05")));
		collator.putIntoQueue("GBPUSD", new QuoteObject(4, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:10")));
		collator.putIntoQueue("GBPUSD", new QuoteObject(3, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:15")));
		collator.putCandle("GBPUSD");
		CandleObject candle = collator.getLastCandle("GBPUSD", CandleType.M1);
		if(candle.getO() != 2 || candle.getC() !=3 || candle.getH() != 4 || candle.getL() !=1 )
			fail("Got "+ candle.getTs() + "," + candle.getO() + "," + candle.getH() + "," + candle.getL() + "," + candle.getC());
	}
	
	@Test
	public void candleUnorderedTest() throws ParseException {
		collator.putIntoQueue("GBPUSD", new QuoteObject(3, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:15")));
		collator.putIntoQueue("GBPUSD", new QuoteObject(2, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:00")));
		collator.putIntoQueue("GBPUSD", new QuoteObject(1, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:05")));
		collator.putIntoQueue("GBPUSD", new QuoteObject(4, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:10")));
		
		collator.putCandle("GBPUSD");
		CandleObject candle = collator.getLastCandle("GBPUSD", CandleType.M1);
		if(candle.getO() != 2 || candle.getC() !=3 || candle.getH() != 4 || candle.getL() !=1 )
			fail("Got "+ candle.getTs() + "," + candle.getO() + "," + candle.getH() + "," + candle.getL() + "," + candle.getC());
	}

	@Test
	public void nextCandleTest() throws ParseException {
		collator.setCandlesDao(new CandlesDaoMock());
		collator.putIntoQueue("GBPUSD", new QuoteObject(3, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 11:00:15")));
		collator.putIntoQueue("GBPUSD", new QuoteObject(2, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:00")));
		collator.putIntoQueue("GBPUSD", new QuoteObject(1, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:05")));
		collator.putIntoQueue("GBPUSD", new QuoteObject(4, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:10")));
		
		collator.putCandle("GBPUSD");
		CandleObject candle = collator.getLastCandle("GBPUSD", CandleType.M1);
		if(candle.getO() != 3 || candle.getC() !=3 || candle.getH() != 3 || candle.getL() !=3 )
			fail("Got "+ candle.getTs() + "," + candle.getO() + "," + candle.getH() + "," + candle.getL() + "," + candle.getC());
	}
	
	
	

	
}