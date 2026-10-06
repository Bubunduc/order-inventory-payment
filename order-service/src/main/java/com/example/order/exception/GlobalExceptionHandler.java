package com.example.order.exception;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.order.dto.ErrorMessageResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		log.warn("Ошибка валидации данных запроса: {}", errors);
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorMessageResponse> handleOrderNotFound(OrderNotFoundException ex) {
		log.info("Заказ не найден: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageResponse(ex.getMessage()));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorMessageResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
		log.warn("Некорректный формат JSON в теле запроса: {}", ex.getMostSpecificCause().getMessage());
		return ResponseEntity.badRequest().body(new ErrorMessageResponse("Некорректное тело запроса"));
	}

	@ExceptionHandler(DuplicateSkuException.class)
	public ResponseEntity<ErrorMessageResponse> handleDuplicateSku(DuplicateSkuException ex) {
		log.warn("Обнаружен дублирующийся SKU: {}", ex.getMessage());
		return ResponseEntity.badRequest().body(new ErrorMessageResponse(ex.getMessage()));
	}

	@ExceptionHandler({ DataAccessException.class, TransactionException.class })
	public ResponseEntity<ErrorMessageResponse> handleDatabaseException(Exception e) {
		log.error("Ошибка при работе с базой данных: {}", e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorMessageResponse("Ошибка при работе с базой данных"));
	}
}