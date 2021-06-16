package com.unnivp.transaction_statistics.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.unnivp.transaction_statistics.controller.StatisticsController;
import com.unnivp.transaction_statistics.domain.Statistics;
import com.unnivp.transaction_statistics.service.StatisticsService;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StatisticsService statisticsService;

	@Test
	public void testGetStatistics_Success() throws Exception {
		
		given(statisticsService.getStatistics(anyLong())).willReturn(new Statistics());

		mockMvc.perform(get("/statistics"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("sum").hasJsonPath())
		.andExpect(jsonPath("avg").hasJsonPath())
		.andExpect(jsonPath("max").hasJsonPath())
		.andExpect(jsonPath("min").hasJsonPath())
		.andExpect(jsonPath("count").hasJsonPath());

	}

}
