package com.unnivp.transaction_statistics.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.unnivp.transaction_statistics.domain.Transaction;
import com.unnivp.transaction_statistics.exception.TransactionInvalidException;
import com.unnivp.transaction_statistics.exception.TransactionOldException;
import com.unnivp.transaction_statistics.service.TransactionService;

/**
 * Controller class to hold methods for managing transactions.
 * 
 * @author unni-vp
 *
 */
@RestController
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	
	/**
	 * Create an entry in Transaction cache for the input transaction.
	 * @param transaction input transaction
	 * @throws TransactionInvalidException if the transaction time is after current time
	 * @throws TransactionOldException if the transaction time is older than 60 seconds of current time
	 */
	@PostMapping("/transactions")
	@ResponseStatus(value = HttpStatus.CREATED)
	private void createTransaction(@RequestBody Transaction transaction) throws TransactionInvalidException, TransactionOldException {
				
		if (transaction.getAmount() == null) throw new TransactionInvalidException();
		transactionService.createTransaction(transaction, Instant.now().toEpochMilli());
	}
	
	/**
	 * Clear all the transaction data till date.
	 * @throws Exception
	 */
	@DeleteMapping("/transactions")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	private void deleteAllTransactions() throws Exception {
				
		transactionService.clearTransactionCache();
	}
	
	// Exception Handlers to handle response status based on exception
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	private void handleException(TransactionOldException exception) {}
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	private void handleException(TransactionInvalidException exception) {}
	
	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	private void handleException(InvalidFormatException exception) {}
	
}
