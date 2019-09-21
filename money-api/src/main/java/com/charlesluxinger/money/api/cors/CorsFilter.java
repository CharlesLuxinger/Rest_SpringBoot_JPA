package com.charlesluxinger.money.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.charlesluxinger.money.api.config.property.MoneyApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

	@Autowired
	private MoneyApiProperty moneyApiProperty;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;

		servletResponse.setHeader("Access-Control-Allow-Origin", moneyApiProperty.getAllowedOrigin());
		servletResponse.setHeader("Access-Control-Allow-Credentials", "true");

		if ("OPTIONS".equalsIgnoreCase(servletRequest.getMethod())
				&& moneyApiProperty.getAllowedOrigin().equalsIgnoreCase(servletRequest.getHeader("Origin"))) {
			servletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
			servletResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			servletResponse.setHeader("Access-Control-Max-Age", "3600");

			servletResponse.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(servletRequest, servletResponse);
		}
	}

}
