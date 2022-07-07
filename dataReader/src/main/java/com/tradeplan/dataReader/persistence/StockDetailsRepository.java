package com.tradeplan.dataReader.persistence;

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
	
	public static String groupingText = " GROUP BY user_defined_market_time " + 
			"  ORDER BY user_defined_market_time ";
	
	@Query(value = "SELECT time_bucket('1 minute', market_time) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + groupingText + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get1mStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit);
	
	@Query(value = "SELECT time_bucket('2 minute', market_time, '1 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + groupingText + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get2mStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit);
	
	@Query(value = "SELECT time_bucket('3 minute', market_time) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + groupingText + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get3mStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit);
	

	@Query(value = "SELECT time_bucket('4 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + groupingText + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get4mStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit);
	
	@Query(value = "SELECT time_bucket('5 minute', market_time) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + groupingText + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get5mStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit);
	
	@Query(value = "SELECT time_bucket('6 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + groupingText + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get6mStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit);
	
	@Query(value = "SELECT time_bucket('7 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + groupingText + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get7mStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit);
	
	@Query(value = "SELECT time_bucket('8 minute', market_time, '3 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + groupingText + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get8mStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit);
	
	@Query(value = "SELECT time_bucket('9 minute', market_time, '6 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + groupingText + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get9mStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit);
	
	@Query(value = "SELECT time_bucket('10 minute', market_time, '5 minute'\\:\\:INTERVAL) AS user_defined_market_time,\r\n" + attributesText
			+ "  WHERE symbol= :symbol\r\n" + groupingText + "  LIMIT :limit", nativeQuery = true)
	List<StockDetailsEntity> get10mStockDetails(@Param("symbol") String symbol, @Param("limit") Integer limit);
}
