package org.exam.tb;

import java.util.Calendar;
import java.util.Date;

class CandleUtils {
	
	private static final ThreadLocal<Calendar> calendar = new ThreadLocal<Calendar>() {
	    protected Calendar initialValue() {
	      return Calendar.getInstance();
	    }
	};
	
	public static boolean belongsToCandle(CandleType type, Date candleTs, Date quoteTs) {
		Calendar cl = calendar.get();
		cl.setTime(candleTs);
		
		switch(type) {
			case M1: 
				cl.add(Calendar.MINUTE, 1);
				break;
			case H1:
				cl.add(Calendar.HOUR, 1);
				break;
			case D1:
				cl.add(Calendar.DATE, 1);
				break;
		}
		
		return quoteTs.before(cl.getTime());
	}


	public static boolean isCandlePeriodNotLess(CandleType type, Date candleTs, Date quoteTs) {
		Calendar cl = calendar.get();
		cl.setTime(candleTs);
		return !quoteTs.before(cl.getTime());
	}

	public static Date getCandlePeriod(CandleType type, Date quoteTs) {
		Calendar cl = calendar.get();
		cl.setTime(quoteTs);
		
		switch(type) {
			case M1: 
				
				cl.set(Calendar.SECOND, 0);
				cl.set(Calendar.MILLISECOND, 0);

				break;
			case H1:
				cl.set(Calendar.MINUTE, 0);
				cl.set(Calendar.SECOND, 0);
				cl.set(Calendar.MILLISECOND, 0);

				break;
			case D1:
				cl.set(Calendar.HOUR_OF_DAY, 0);
				cl.set(Calendar.MINUTE, 0);
				cl.set(Calendar.SECOND, 0);
				cl.set(Calendar.MILLISECOND, 0);
				break;
		}
		return cl.getTime();
	}
	
}
