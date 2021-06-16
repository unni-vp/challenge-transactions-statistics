package com.unnivp.transaction_statistics.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unnivp.transaction_statistics.controller.TransactionController;
import com.unnivp.transaction_statistics.domain.Transaction;
import com.unnivp.transaction_statistics.exception.TransactionInvalidException;
import com.unnivp.transaction_statistics.exception.TransactionOldException;
import com.unnivp.transaction_statistics.service.TransactionService;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private TransactionService transactionService;

	@Test
	public void testPostTransaction_Success() throws Exception, TransactionInvalidException, TransactionOldException {

		doNothing().when(transactionService).createTransaction(any(), anyLong());

		mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new Transaction(BigDecimal.ONE, ZonedDateTime.now()))))
				.andExpect(status().isCreated());

	}

	@Test
	public void testDeleteTransaction_Success() throws Exception {

		doNothing().when(transactionService).clearTransactionCache();

		mockMvc.perform(delete("/transactions")).andExpect(status().isNoContent());

	}

}
