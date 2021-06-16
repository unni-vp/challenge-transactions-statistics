package com.unnivp.transaction_statistics.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unnivp.transaction_statistics.domain.Statistics;
import com.unnivp.transaction_statistics.service.StatisticsService;

/**
 * Controller class to hold methods for retrieving statistics.
 * 
 * @author unni-vp
 *
 */
@RestController
public class StatisticsController {
	
	@Autowired
	private StatisticsService statisticsService;
	
	/**
	 * /**
	 * Get statistics for all transactions which are within 60 second of the current time 
	 * @return statistics for sum, average, minimum, maximum and count
	 * @throws Exception
	 */
	@GetMapping("/statistics")
	private Statistics getStatistics() throws Exception {
		
		Statistics stats = statisticsService.getStatistics(Instant.now().toEpochMilli());
		
		return stats;
	}

}
