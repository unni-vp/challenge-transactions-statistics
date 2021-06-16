package com.unnivp.transaction_statistics.service;

import static com.unnivp.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.LinkedHashMultimap;
import com.unnivp.transaction_statistics.cache.TransactionCache;
import com.unnivp.transaction_statistics.domain.Statistics;
import com.unnivp.transaction_statistics.service.StatisticsService;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceTest {
	
	@InjectMocks
	private StatisticsService statisticsService;
	
	@Mock
	private TransactionCache transactionCache;
	
	private static final BigDecimal TEST_VALUE = BigDecimal.TEN.setScale(2);
	
	private static final BigDecimal ZERO_VALUE = BigDecimal.ZERO.setScale(2);
	
	@Test
	public void getStatistics_returnsEmptyStatisticDetails() throws Exception {
		
		given(transactionCache.getValueMap()).willReturn(null);

		Statistics stats = statisticsService.getStatistics(Instant.now().toEpochMilli());
		

		assertThat(stats).isNotNull();
		assertThat(stats.getCount()).isEqualTo(0);
		assertThat(stats.getSum()).isEqualTo(ZERO_VALUE);
		assertThat(stats.getAvg()).isEqualTo(ZERO_VALUE);
		assertThat(stats.getMax()).isEqualTo(ZERO_VALUE);
		assertThat(stats.getMin()).isEqualTo(ZERO_VALUE);
	}


	@Test
	public void getStatistics_returnsStatisticDetailsForValidData() throws Exception {
		
		given(transactionCache.getValueMap()).willReturn(populateTransactionCacheWithValidData());

		Statistics stats = statisticsService.getStatistics(Instant.now().toEpochMilli());

		assertThat(stats).isNotNull();
		assertThat(stats.getCount()).isEqualTo(2);
		assertThat(stats.getSum()).isEqualTo(TEST_VALUE.add(TEST_VALUE.divide(TEST_VALUE)));
		assertThat(stats.getAvg()).isEqualTo(TEST_VALUE.add(TEST_VALUE.divide(TEST_VALUE)).divide(new BigDecimal(2)));
		assertThat(stats.getMax()).isEqualTo(TEST_VALUE);
		assertThat(stats.getMin()).isEqualTo((TEST_VALUE.divide(TEST_VALUE)).setScale(2));
	}
	
	@Test
	public void getStatistics_returnsStatisticDetailsForInValidData() throws Exception {
		
		given(transactionCache.getValueMap()).willReturn(populateTransactionCacheWithInValidData());

		Statistics stats = statisticsService.getStatistics(Instant.now().toEpochMilli());

		assertThat(stats).isNotNull();
		assertThat(stats.getCount()).isEqualTo(0);
		assertThat(stats.getSum()).isEqualTo(ZERO_VALUE);
		assertThat(stats.getAvg()).isEqualTo(ZERO_VALUE);
		assertThat(stats.getMax()).isEqualTo(ZERO_VALUE);
		assertThat(stats.getMin()).isEqualTo(ZERO_VALUE);
	}
	
	
	
	@Test
	public void getStatistics_returnsStatisticDetailsForPartialValidData() throws Exception {
		
		given(transactionCache.getValueMap()).willReturn(populateTransactionCacheWithPartialValidData());

		Statistics stats = statisticsService.getStatistics(Instant.now().toEpochMilli());

		assertThat(stats).isNotNull();
		assertThat(stats.getCount()).isEqualTo(1);
		assertThat(stats.getSum()).isEqualTo(TEST_VALUE);
		assertThat(stats.getAvg()).isEqualTo(TEST_VALUE);
		assertThat(stats.getMax()).isEqualTo(TEST_VALUE);
		assertThat(stats.getMin()).isEqualTo(TEST_VALUE);
	}
	
	@Test
	public void getStatistics_returnsStatisticDetailsForValidDuplicateData() throws Exception {
		
		given(transactionCache.getValueMap()).willReturn(populateTransactionCacheWithDuplicateValidData());

		Statistics stats = statisticsService.getStatistics(Instant.now().toEpochMilli());

		assertThat(stats).isNotNull();
		assertThat(stats.getCount()).isEqualTo(2);
		assertThat(stats.getSum()).isEqualTo(TEST_VALUE.add(TEST_VALUE.divide(TEST_VALUE)));
		assertThat(stats.getAvg()).isEqualTo(TEST_VALUE.add(TEST_VALUE.divide(TEST_VALUE)).divide(new BigDecimal(2)));
		assertThat(stats.getMax()).isEqualTo(TEST_VALUE);
		assertThat(stats.getMin()).isEqualTo((TEST_VALUE.divide(TEST_VALUE)).setScale(2));
	}

	private LinkedHashMultimap<Long, BigDecimal> populateTransactionCacheWithValidData() {
		
		TransactionCache cache = new TransactionCache();
		long timeNow = Instant.now().toEpochMilli();
		cache.put(timeNow, TEST_VALUE);
		cache.put(timeNow - ONE_SECOND_IN_MILLISEC, TEST_VALUE.divide(TEST_VALUE));
		return cache.getValueMap();
		
	}
	
	private LinkedHashMultimap<Long, BigDecimal> populateTransactionCacheWithInValidData() {
		
		TransactionCache cache = new TransactionCache();
		long timeNow = Instant.now().toEpochMilli();
		cache.put(timeNow - BOUNDARY_INTERVAL_IN_MILLISEC, TEST_VALUE);
		cache.put(timeNow - BOUNDARY_INTERVAL_IN_MILLISEC - ONE_SECOND_IN_MILLISEC, TEST_VALUE.divide(TEST_VALUE));
		return cache.getValueMap();
		
	}
	
	private LinkedHashMultimap<Long, BigDecimal> populateTransactionCacheWithPartialValidData() {
		
		TransactionCache cache = new TransactionCache();
		long timeNow = Instant.now().toEpochMilli();
		cache.put(timeNow - ONE_SECOND_IN_MILLISEC, TEST_VALUE);
		cache.put(timeNow - BOUNDARY_INTERVAL_IN_MILLISEC, TEST_VALUE.divide(TEST_VALUE));
		return cache.getValueMap();
		
	}
	
	private LinkedHashMultimap<Long, BigDecimal> populateTransactionCacheWithDuplicateValidData() {
		
		TransactionCache cache = new TransactionCache();
		long timeNow = Instant.now().toEpochMilli();
		cache.put(timeNow - ONE_SECOND_IN_MILLISEC, TEST_VALUE);
		cache.put(timeNow - ONE_SECOND_IN_MILLISEC, TEST_VALUE.divide(TEST_VALUE));
		return cache.getValueMap();
		
	}

}
