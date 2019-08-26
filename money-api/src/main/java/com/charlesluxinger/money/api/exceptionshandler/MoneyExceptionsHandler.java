package com.charlesluxinger.money.api.exceptionshandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
		String messageDeveloper = ex.getCause().toString();
		return handleExceptionInternal(ex, new Erro(messageUser, messageDeveloper), headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private static class Erro {
		private String messageUser;
		private String messageDeveloper;
		
		public Erro(String messageUser, String messageDeveloper) {
			this.messageUser = messageUser;
			this.messageDeveloper = messageDeveloper;
		}

		@SuppressWarnings("unused")
		public String getMessageUser() {
			return messageUser;
		}

		@SuppressWarnings("unused")
		public String getMessageDeveloper() {
			return messageDeveloper;
		}		
	}
}
