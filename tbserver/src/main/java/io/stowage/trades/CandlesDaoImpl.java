package io.stowage.trades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class CandlesDaoImpl implements CandlesDao {
	
	private final JdbcTemplate jdbcTemplate;

	public CandlesDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public void record(String ticker, CandleType type, CandleObject candle) {
		String sql = "INSERT INTO candles" + type.name() + "(ticker, ts, op, hi, lo, cl) values(?,?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{ticker, 
    			candle.getTs(), candle.getO(), candle.getH(), candle.getL(), candle.getC()});
	}

	@Override
	public List<CandleObject> getCandles(String ticker, CandleType type, Date from, Date to) {
        String sql = "SELECT ts, op, hi, lo, cl" +
                " FROM candles" + type.name() +
                " WHERE ticker=? AND ts BETWEEN ? AND ? ORDER BY TS";
        return jdbcTemplate.query(
        	    sql,
        	    new Object[] { ticker, from, to},
        	    new RowMapper<CandleObject>() {
        	        public CandleObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        	        	return new CandleObject( rs.getDouble("op"),
       						 rs.getDouble("hi"),
       						 rs.getDouble("lo"),
       						 rs.getDouble("cl"),
       						 rs.getTimestamp("ts"));
        	        }
        	    });
	}
	
}
