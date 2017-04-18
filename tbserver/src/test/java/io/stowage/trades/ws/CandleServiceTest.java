package io.stowage.trades.ws;

import static org.junit.Assert.*;

import java.text.ParseException;

import io.stowage.trades.CandleObject;
import io.stowage.trades.CandleType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Ignore
public class CandleServiceTest {

	private ApplicationContext context;
	private CandleService service;
	
	@Before
	public void setUp() {
		context = new ClassPathXmlApplicationContext(new String[] {"classpath:/spring-ds.xml", 
				"classpath:/spring-base.xml", "classpath:/spring-package-scan.xml"});
		
		service = (CandleService)context.getBean("candleX");
	}
	
	@Test
	public void testJob() throws ParseException, InterruptedException {
		service.passQuoteData("USDEUR", "11-09-2012 11:12:13", 2);
		Thread.sleep(3000);
		CandleObject candle = service.getLastCandle("USDEUR", CandleType.D1);
		
		assertTrue(candle.getC() == 2);
	}
	
	@Test
	public void testDbRecord() throws ParseException, InterruptedException {
		
		service.passQuoteDataFast("USDEUR", "11-09-2012 11:12:13", 2);
		service.passQuoteDataFast("USDEUR", "11-09-2012 11:13:13", 3);
		Thread.sleep(3000);
		CandleObject candle = service.getLastCandle("USDEUR", CandleType.M1);
		//System.out.println(candle.getC());
		assertTrue(candle.getC() == 3);
		
		int size = service.getCandles("USDEUR", CandleType.M1, "11-09-2012 11:12:00", null).size();
		if(size<2)
			fail("Got size:"+size);
	}

}
