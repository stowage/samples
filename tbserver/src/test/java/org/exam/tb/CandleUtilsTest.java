package org.exam.tb;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class CandleUtilsTest {

	
	@Test
	public void testBelongsToCandle() throws ParseException {
		if(!CandleUtils.belongsToCandle(CandleType.M1, new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:00"), 
				new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:15")))
		fail("Belongs to candle error");
	}
	
	@Test
	public void testStartPeriod() throws ParseException {
		Date quoteDate = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:15");
		Date candleDate = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").parse("11-09-2012 10:00:00");
		Date testDate = CandleUtils.getCandlePeriod(CandleType.M1, quoteDate);
		if(!testDate.equals(candleDate))
			fail("Candle date error got:"+testDate);
	}

}
