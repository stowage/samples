package org.exam.tb.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.jws.WebService;

import org.exam.tb.CandleObject;
import org.exam.tb.CandleType;
import org.exam.tb.CandlesCollator;
import org.exam.tb.CandlesDao;
import org.exam.tb.QuoteObject;
import org.exam.tb.ws.ICandleService;

@WebService( endpointInterface = "org.exam.tb.ws.ICandleService" )
public class CandleService implements ICandleService {
	
	private static final ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        }
    };
    
    private final CandlesCollator collator;
    private final CandlesDao candlesDao;

	public CandleService(CandlesCollator collator, CandlesDao candlesDao) {
		this.collator = collator;
		this.candlesDao = candlesDao;
	}
		
	public void passQuoteDataFast(String ticker, String ts, double price) throws ParseException {
		collator.map(ticker, new QuoteObject(price, dateFormat.get().parse(ts)));
		collator.reduce(ticker);
	}
	
	public void passQuoteData(String ticker, String ts, double price) throws ParseException {
		collator.map(ticker, new QuoteObject(price, dateFormat.get().parse(ts)));
	}
	
	public List<CandleObject> getCandles(String ticker, CandleType type, String from, String to) throws ParseException {
		CandleObject lastCandle = null;
		
		if(to == null) {
			lastCandle = collator.getLastCandle(ticker, type);
			if(lastCandle!=null) {
				to = dateFormat.get().format(lastCandle.getTs());
			}
		}
		List<CandleObject> resultCandles = candlesDao.getCandles(ticker, type, dateFormat.get().parse(from), dateFormat.get().parse(to));
		if(lastCandle!=null) resultCandles.add(lastCandle);
		
		return resultCandles;
	}
	
	@Override
	public CandleObject getLastCandle(String ticker, CandleType type)  {
		return collator.getLastCandle(ticker, type);
	}
}
