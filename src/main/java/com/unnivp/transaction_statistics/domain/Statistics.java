package com.unnivp.transaction_statistics.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * Class to hold statistic values based of the transactions of the last 60
 * seconds
 * 
 * @author unni-vp
 *
 */
public class Statistics {

	/** total sum of transaction values in the last 60 seconds **/
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private @Getter @Setter BigDecimal sum;

	/** average amount of transaction values in the last 60 seconds **/
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private @Getter @Setter BigDecimal avg;

	/** single highest transaction value in the last 60 seconds **/
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private @Getter BigDecimal max;

	/** single lowest transaction value in the last 60 seconds **/
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private @Getter BigDecimal min;

	/** total number of transactions that happened in the last 60 seconds **/
	private @Getter @Setter long count;
	
	
	/**
	 * Default Constructor
	 */
	public Statistics() {
		super();
		this.sum = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		this.avg = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		this.max = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		this.min = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		this.count = 0;
	}

	/**
	 * Constructor using fields.
	 * @param sum
	 * @param avg
	 * @param min
	 * @param max
	 * @param count
	 */
	public Statistics(BigDecimal sum, BigDecimal avg, BigDecimal min, BigDecimal max, long count) {
		super();
		this.sum = sum;
		this.avg = avg;
		this.min = min;
		this.max = max;
		this.count = count;
	}
	
	public void addSum(BigDecimal value) {
		// Add input value to sum
		this.sum = this.sum.add(value);
	}

	public void setMin(BigDecimal value) {
		//Compares input value and assign to min, if it less than min or min is null 
		this.min = (this.min.compareTo(BigDecimal.ZERO) == 0 || value.compareTo(this.min) < 0) ? value.setScale(2, RoundingMode.HALF_UP) : this.min;
	}
	
	public void setMax(BigDecimal value) {
		//Compares input value and assign to max, if it greater than max 
		this.max = value.compareTo(this.max) > 0 ? value.setScale(2, RoundingMode.HALF_UP) : this.max;
	}

	public void addCount(int count) {
		this.count+= count;
	}
	
}
