package io.stowage.trades.ws;

import java.text.ParseException;
import java.util.List;

import javax.jws.WebService;

import io.stowage.trades.CandleType;
import io.stowage.trades.CandleObject;

@WebService
public interface ICandleService {
	
	void passQuoteData(String ticker, String ts, double price) throws ParseException;
	
	List<CandleObject> getCandles(String ticker, CandleType type, String from, String to) throws ParseException;
	
	CandleObject getLastCandle(String ticker, CandleType type);
}
