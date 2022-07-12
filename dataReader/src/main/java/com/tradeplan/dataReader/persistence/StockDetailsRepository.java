package com.tradeplan.dataReader.persistence;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDetailsRepository extends CrudRepository<StockDetailsEntity, StockDetailsId> {
	
	public static String attributesText = "first(open, demo.stock_details_entity1.market_time) as open, " + 
			"min(low) as low, max(high) as high, last(close, market_time) as close, sum(volume) as volume, first(symbol, market_time) as symbol, last(modified_time, market_time) as modified_time " + 
			"FROM demo.stock_details_entity1";
	
	public static String bkwdGroup = " GROUP BY user_defined_market_time " + 
			"  ORDER BY user_defined_market_time DESC ";
	public static String bkwdOp = "<";
	
	public static String fwdGroup = " GROUP BY user_defined_market_time " + 
			"  ORDER BY user_defined_market_time ASC ";
	public static String fwdOp = ">=";
	
	/**
	 * ---------------------------------------<b><i>  1-minute Backward </i></b>---------------------------------------
	 * @param symbol : symbol that represent stock, to which details has to be retrieved.
	 * @param limit : number of rows of details to be retrieved.
	 * @param marketTime : reference market time for retrieval. For backward collection, given market time is excluded.
	 *  example, if given market time is 't' then all rows of 1-minute table with less than 't' and up to limit is retrieved <br>
	 *  i.e if limit = 3, then rows with 't-1', 't-2', 't-3' are retrieved.  <br>
	 *  i.e if 't = 10hr 05min (10:05) and limit=3 then rows at 10:04, 10:03, 10:02 market time are retrieved 
	 * @return list of {@link StockDetailsEntity} which includes OLHCV values.
	 */
	@Query(value = "SELECT time_bucket('1 minute', market_time) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n"+ " AND market_time " + bkwdOp + ":marketTime"+ bkwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get1mBackwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	/**
	 *  ---------------------------------------<b><i>  1-minute Forward </i></b>---------------------------------------
	 * @param symbol : symbol that represent stock, to which details has to be retrieved.
	 * @param limit : number of rows of details to be retrieved.
	 * @param marketTime : reference market time for retrieval. For foward collection, given market time is included.
	 *  example, if given market time is 't' then all rows  of 1-minute table with greater than or equal to 't' and up to limit is retrieved <br>
	 *  i.e if limit = 3, then rows with 't', 't+1', 't+2' are retrieved. <br>
	 *  i.e if 't = 10hr 05min (10:05) and limit=3 then rows at 10:05, 10:06, 10:07 market time are retrieved
	 * @return list of {@link StockDetailsEntity} which includes OLHCV values.
	 */
	@Query(value = "SELECT time_bucket('1 minute', market_time) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n"+ " AND market_time " + fwdOp + ":marketTime"+ fwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get1mForwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	
	/**
	 * ---------------------------------------<b><i>  2-minute Backward </i></b>---------------------------------------
	 * @param symbol : symbol that represent stock, to which details has to be retrieved.
	 * @param limit : number of rows of details to be retrieved.
	 * @param marketTime : reference market time for retrieval. For backward collection, given market time is excluded.
	 *  example, if given market time is 't' then all rows of 2-minute table with less than 't' and up to limit is retrieved <br>
	 *  i.e if limit = 3, then rows with 't-1', 't-2', 't-3' are retrieved.  <br>
	 *  i.e if 't = 10hr 10min (10:10) and limit=3 then rows at 10:08, 10:06, 10:04 market time are retrieved 
	 * @return list of {@link StockDetailsEntity} which includes OLHCV values.
	 */
	@Query(value = "SELECT time_bucket('2 minute', market_time, '1 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + " AND market_time " + bkwdOp + ":marketTime"+ bkwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get2mBackwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	/**
	 *  ---------------------------------------<b><i>  2-minute Forward </i></b>---------------------------------------
	 * @param symbol : symbol that represent stock, to which details has to be retrieved.
	 * @param limit : number of rows of details to be retrieved.
	 * @param marketTime : reference market time for retrieval. For foward collection, given market time is included.
	 *  example, if given market time is 't' then all rows  of 2-minute table with greater than or equal to 't' and up to limit is retrieved <br>
	 *  i.e if limit = 3, then rows with 't', 't+1', 't+2' are retrieved. <br>
	 *  i.e if 't = 10hr 10min (10:10) and limit=3 then rows at 10:10, 10:12, 10:14 market time are retrieved
	 * @return list of {@link StockDetailsEntity} which includes OLHCV values.
	 */
	@Query(value = "SELECT time_bucket('2 minute', market_time, '1 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + " AND market_time " + fwdOp + ":marketTime"+ fwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get2mForwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	// 3-minute
	@Query(value = "SELECT time_bucket('3 minute', market_time) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + " AND market_time " + bkwdOp + ":marketTime" + bkwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get3mBackwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit,@Param("marketTime") Calendar marketTime);
	

	@Query(value = "SELECT time_bucket('3 minute', market_time) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + " AND market_time " + fwdOp + ":marketTime" + fwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get3mForwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit,@Param("marketTime") Calendar marketTime);
	
	
	// 4-minute
	@Query(value = "SELECT time_bucket('4 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" +" AND market_time " + bkwdOp + ":marketTime" + bkwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get4mBackwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	@Query(value = "SELECT time_bucket('4 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" +" AND market_time " + fwdOp + ":marketTime" + fwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get4mForwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	
	// 5-minute
	@Query(value = "SELECT time_bucket('5 minute', market_time) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" +" AND market_time " + bkwdOp + ":marketTime" + bkwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get5mBackwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	@Query(value = "SELECT time_bucket('5 minute', market_time) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" +" AND market_time " + fwdOp + ":marketTime" + fwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get5mForwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	
	// 6-minute
	@Query(value = "SELECT time_bucket('6 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + " AND market_time " + bkwdOp + ":marketTime" + bkwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get6mBackwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	@Query(value = "SELECT time_bucket('6 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + " AND market_time " + fwdOp + ":marketTime" + fwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get6mForwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	
	// 7-minute
	@Query(value = "SELECT time_bucket('7 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + " AND market_time " + bkwdOp + ":marketTime" + bkwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get7mBackwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);

	@Query(value = "SELECT time_bucket('7 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + " AND market_time " + fwdOp + ":marketTime" + fwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get7mForwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	
	// 8-minute
	@Query(value = "SELECT time_bucket('8 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" +" AND market_time " + bkwdOp + ":marketTime" + bkwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get8mBackwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	@Query(value = "SELECT time_bucket('8 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" +" AND market_time " + fwdOp + ":marketTime" + fwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get8mForwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	
	// 9-minute
	@Query(value = "SELECT time_bucket('9 minute', market_time, '6 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" +" AND market_time " + bkwdOp + ":marketTime" + bkwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get9mBackwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	@Query(value = "SELECT time_bucket('9 minute', market_time, '6 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" +" AND market_time " + fwdOp + ":marketTime" + fwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get9mForwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	
	// 10-minute
	@Query(value = "SELECT time_bucket('10 minute', market_time, '5 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" +" AND market_time " + bkwdOp + ":marketTime" + bkwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get10mBackwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
	
	@Query(value = "SELECT time_bucket('10 minute', market_time, '5 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" +" AND market_time " + fwdOp + ":marketTime" + fwdGroup + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get10mForwardStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit, @Param("marketTime") Calendar marketTime);
}
