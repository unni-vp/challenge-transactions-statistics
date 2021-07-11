package com.unnivp.transaction_statistics.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class to hold transaction request details.
 * 
 * @author unni-vp
 *
 */
public @Data @AllArgsConstructor class Transaction {
	
	private BigDecimal amount;
	
	private ZonedDateTime timestamp;

}
