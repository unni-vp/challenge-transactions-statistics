package com.unnivp.transaction_statistics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unnivp.transaction_statistics.cache.TransactionCache;
import com.unnivp.transaction_statistics.domain.Transaction;
import com.unnivp.transaction_statistics.exception.TransactionInvalidException;
import com.unnivp.transaction_statistics.exception.TransactionOldException;

/**
 * Class to hold service implementation for methods related to Transactions.
 * @author unni-vp
 *
 */
@Service
public class TransactionService {

	@Autowired
	private TransactionCache transactionCache;
	
	private static final long ONE_MINUTE_IN_MILLISEC = 60 * 1000;

	/**
	 * Create an entry in Transaction cache for the input transaction.
	 * @param transaction input transaction
	 * @param requestTime the time when the transaction was requested
	 * @throws TransactionInvalidException if the transaction time is more recent than @requestTime
	 * @throws TransactionOldException if the transaction time is older than 60 seconds of @requestTime
	 */
	public void createTransaction(Transaction transaction, long requestTime) throws TransactionInvalidException, TransactionOldException {
		
		//Calculate elapsed time since request time
		long transactionTime = transaction.getTimestamp().toInstant().toEpochMilli();
        long timeElapsed = requestTime - transactionTime;
        
        if (timeElapsed > ONE_MINUTE_IN_MILLISEC) {
        	//if the transaction time is older than 60 seconds of request time
            throw new TransactionOldException();
        } else if (timeElapsed < 0) {
        	//if the transaction time is more recent than request time
        	throw new TransactionInvalidException();
        } else {
        	// Valid transaction. Put it into cache.
            transactionCache.put(transactionTime, transaction.getAmount());
        }
	}
	
	/**
	 * Clear all the transactions from Transaction cache.
	 * @throws Exception
	 */
	public void clearTransactionCache() throws Exception {

		transactionCache.clear();
	}

}
