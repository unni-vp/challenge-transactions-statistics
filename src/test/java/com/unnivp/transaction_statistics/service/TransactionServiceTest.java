package com.unnivp.transaction_statistics.service;

import static com.unnivp.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.unnivp.transaction_statistics.cache.TransactionCache;
import com.unnivp.transaction_statistics.domain.Transaction;
import com.unnivp.transaction_statistics.exception.TransactionInvalidException;
import com.unnivp.transaction_statistics.exception.TransactionOldException;
import com.unnivp.transaction_statistics.service.TransactionService;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {
	
	@InjectMocks
	private TransactionService transactionService;
	
	@Mock
	private TransactionCache transactionCache;
	
	@Test
	public void testClearTransactionCache_invocation() throws Exception {
		
		doNothing().when(transactionCache).clear();
		transactionService.clearTransactionCache();
		verify(transactionCache, times(1)).clear();
	}
	
	@Test
	public void testCreateTransaction_ForValidData() throws TransactionInvalidException, TransactionOldException {
		
		doNothing().when(transactionCache).put(anyLong(), any());
		
		long requestTime = Instant.now().toEpochMilli();
		ZonedDateTime transactionDate = ZonedDateTime.now().minusSeconds(ONE_SECOND);
		
		transactionService.createTransaction(new Transaction(BigDecimal.ONE, transactionDate), requestTime);
		verify(transactionCache, times(1)).put(anyLong(), any());

	}
	
	@Test(expected = TransactionInvalidException.class)
	public void testCreateTransaction_throwsTransactionInvalidException() throws TransactionInvalidException, TransactionOldException {
		
		long requestTime = Instant.now().toEpochMilli();
		ZonedDateTime transactionDate = ZonedDateTime.now().plusSeconds(ONE_SECOND);
		
		transactionService.createTransaction(new Transaction(BigDecimal.ONE, transactionDate), requestTime);
	}
	
	@Test(expected = TransactionOldException.class)
	public void testCreateTransaction_throwsTransactionOldException() throws TransactionInvalidException, TransactionOldException {
		
		long requestTime = Instant.now().toEpochMilli();
		ZonedDateTime transactionDate = ZonedDateTime.now().minusSeconds(BOUNDARY_INTERVAL_IN_SECONDS);
		
		transactionService.createTransaction(new Transaction(BigDecimal.ONE, transactionDate), requestTime);
	}

}
