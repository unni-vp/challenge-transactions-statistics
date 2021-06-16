package com.unnivp.transaction_statistics.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.unnivp.transaction_statistics.cache.TransactionCache;
import com.unnivp.transaction_statistics.domain.Statistics;

/**
 * Class to hold service implementation for methods related to Statistics.
 * @author unni-vp
 *
 */
@Service
public class StatisticsService {

	@Autowired
	private TransactionCache transactionCache;

	private static final long ONE_MINUTE_IN_MILLISEC = 60 * 1000;

	/**
	 * Get transaction statistics for transactions which are within 60 second of the input timestamp 
	 * @param requestTime input timestamp in milliseconds
	 * @return statistics for sum, average, minimum, maximum and count
	 * @throws Exception
	 */
	public Statistics getStatistics(long requestTime) throws Exception {

		Statistics stats = new Statistics();

		//Get the timestamp for last possible transaction
		long lastPossibleDate = requestTime - ONE_MINUTE_IN_MILLISEC;

		if (transactionCache != null && transactionCache.getValueMap() != null
				&& !CollectionUtils.isEmpty(transactionCache.getValueMap().keys())) {
			
			//Filter the records in transaction cache, if their timestamp is within the last possible timestamp
			Set<Entry<Long, Collection<BigDecimal>>> mapValues = transactionCache.getValueMap().asMap().entrySet();
			mapValues.parallelStream().filter(e -> (e.getKey() >= lastPossibleDate)).forEach(e -> {
				e.getValue().forEach(value -> {
					synchronized (stats) {
						stats.addSum(value);
						stats.setMax(value);
						stats.setMin(value);
						stats.addCount(1);
					}
				});

			});

		}

		//Calculate the average if there are any matching transactions
		if (stats.getCount() > 0) {
			stats.setAvg(stats.getSum().divide(new BigDecimal(stats.getCount()), 2, RoundingMode.HALF_UP));
			stats.setSum(stats.getSum().setScale(2, RoundingMode.HALF_UP));
		}
		
		return stats;
	}

}
