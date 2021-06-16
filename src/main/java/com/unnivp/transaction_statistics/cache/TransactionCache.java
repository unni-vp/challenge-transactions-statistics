package com.unnivp.transaction_statistics.cache;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.google.common.collect.LinkedHashMultimap;

/**
 * In-memory cache for transactions. Transaction details are stored in a multi-map to
 * cater for multiple entries for the same timestamp.
 * Cache eviction is not implemented.
 * 
 * @author unni-vp
 *
 */
@Component
public class TransactionCache {

	private LinkedHashMultimap<Long, BigDecimal> transactionCacheMap;

	public TransactionCache() {
		transactionCacheMap = LinkedHashMultimap.create();
	}

	/**
	 * Synchronized put Transaction details to transaction cache
	 * @param transactionTime timestamp in epoch milliseconds
	 * @param amount the big decimal value corresponding to the timestamp
	 */
	public void put(long transactionTime, BigDecimal amount) {

		synchronized (transactionCacheMap) {
			transactionCacheMap.put(transactionTime, amount);
		}
	}

	public LinkedHashMultimap<Long, BigDecimal> getValueMap() {
		return transactionCacheMap;
	}

	/**
	 * Clear the transaction cache, removes all entries.
	 */
	public void clear() {
		synchronized (transactionCacheMap) {
			transactionCacheMap.clear();
		}
	}

}
