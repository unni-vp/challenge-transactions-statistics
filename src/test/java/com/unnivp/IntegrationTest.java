package com.unnivp;

import static com.unnivp.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.unnivp.transaction_statistics.domain.Statistics;
import com.unnivp.transaction_statistics.domain.Transaction;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	
	@Test
	public void testGetStatistics_returnsStatistics() {

		fireTransactionRequest(BigDecimal.TEN, ZonedDateTime.now());
		fireTransactionRequest(BigDecimal.ONE, ZonedDateTime.now());
		
		ResponseEntity<Statistics> statistics = restTemplate.getForEntity("/statistics", Statistics.class);
		
		assertThat(statistics.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(statistics.getBody()).isNotNull();
		assertThat(statistics.getBody().getCount()).isEqualTo(2);
		assertThat(statistics.getBody().getSum()).isEqualTo(new BigDecimal(11).setScale(2, RoundingMode.HALF_UP));
		assertThat(statistics.getBody().getAvg()).isEqualTo(new BigDecimal(5.5).setScale(2, RoundingMode.HALF_UP));
		assertThat(statistics.getBody().getMax()).isEqualTo(BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP));
		assertThat(statistics.getBody().getMin()).isEqualTo(BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP));
	}
	
	@Test
	public void testGetStatistics_returnsEmptyStatistics() {
		
		ResponseEntity<Statistics> statistics = restTemplate.getForEntity("/statistics", Statistics.class);
		
		assertThat(statistics.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(statistics.getBody()).isNotNull();
		assertThat(statistics.getBody().getCount()).isEqualTo(0);
	}
	
	@Test
	public void testDeleteTransactions_returnsNoContent() {
		
		ResponseEntity<Void> response = restTemplate.exchange("/transactions", HttpMethod.DELETE, new HttpEntity<String>(""), Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	public void testPostTransactions_postsTransaction_withValidData() {
		
		HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(BigDecimal.ONE, ZonedDateTime.now()));
		ResponseEntity<Void> response = restTemplate.exchange("/transactions", HttpMethod.POST, request, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
	
	@Test
	public void testPostTransactions_postsTransaction_withFutureDate() {
		
		ZonedDateTime transactionDate = ZonedDateTime.now().plusSeconds(ONE_SECOND);
		HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(BigDecimal.ONE, transactionDate));
		ResponseEntity<Void> response = restTemplate.exchange("/transactions", HttpMethod.POST, request, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@Test
	public void testPostTransactions_postsTransaction_withOlderDate() {
		
		ZonedDateTime transactionDate = ZonedDateTime.now().minusSeconds(BOUNDARY_INTERVAL_IN_SECONDS);
		HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(BigDecimal.ONE, transactionDate));
		ResponseEntity<Void> response = restTemplate.exchange("/transactions", HttpMethod.POST, request, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	public void testPostTransactions_postsTransaction_withInvalidTransactionValues() {
		
		HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(null, ZonedDateTime.now()));
		ResponseEntity<Void> response = restTemplate.exchange("/transactions", HttpMethod.POST, request, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@Test
	public void testPostTransactions_postsTransaction_withInvalidRequestContent() {
		
		HttpEntity<Transaction> request = new HttpEntity<>(null);
		ResponseEntity<Void> response = restTemplate.exchange("/transactions", HttpMethod.POST, request, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
	
	private void fireTransactionRequest(BigDecimal value, ZonedDateTime time) {
		
		HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(value, time));
		restTemplate.exchange("/transactions", HttpMethod.POST, request, Void.class);
	}

}
