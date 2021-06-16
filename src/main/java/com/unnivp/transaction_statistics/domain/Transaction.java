package com.unnivp.transaction_statistics.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Class to hold transaction request details.
 * 
 * @author unni-vp
 *
 */
public class Transaction {
	
	private BigDecimal amount;
	
	private ZonedDateTime timestamp;
	
	public Transaction() {
		super();
	}

	public Transaction(BigDecimal amount, ZonedDateTime timestamp) {
		super();
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
