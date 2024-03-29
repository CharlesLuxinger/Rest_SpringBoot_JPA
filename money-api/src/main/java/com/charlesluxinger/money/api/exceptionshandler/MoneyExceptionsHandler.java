package com.charlesluxinger.money.api.exceptionshandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MoneyExceptionsHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String messageUser = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String messageDeveloper = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

		List<Erro> erros = Arrays.asList(new Erro(messageUser, messageDeveloper));

		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<Erro> erros = listErros(ex.getBindingResult());

		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}

	private List<Erro> listErros(BindingResult bindingResult) {
		List<Erro> erros = new ArrayList<MoneyExceptionsHandler.Erro>();

		bindingResult.getFieldErrors().forEach(fieldError -> {

			String messageUser = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String messageDeveloper = fieldError.toString();

			erros.add(new Erro(messageUser, messageDeveloper));
		});

		return erros;
	}

	public static class Erro {

		private String messageUser;
		private String messageDeveloper;

		public Erro(String messageUser, String messageDeveloper) {
			this.messageUser = messageUser;
			this.messageDeveloper = messageDeveloper;
		}

		public String getMessageUser() {
			return messageUser;
		}

		public String getMessageDeveloper() {
			return messageDeveloper;
		}

	}

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException(WebRequest request,
			EmptyResultDataAccessException ex) {

		String messageUser = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
		String messageDeveloper = ex.toString();

		List<Erro> erros = Arrays.asList(new Erro(messageUser, messageDeveloper));

		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleDataIntegrityViolationException(WebRequest request,
			DataIntegrityViolationException ex) {
		String messageUser = messageSource.getMessage("recurso.operacao-nao-permitida", null,
				LocaleContextHolder.getLocale());
		String messageDeveloper = ExceptionUtils.getRootCauseMessage(ex);

		List<Erro> erros = Arrays.asList(new Erro(messageUser, messageDeveloper));

		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
}
