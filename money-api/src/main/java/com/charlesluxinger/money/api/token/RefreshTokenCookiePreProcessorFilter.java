package com.charlesluxinger.money.api.token;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;

		if ("/oauth/token".equalsIgnoreCase(servletRequest.getRequestURI())
				&& "refresh_token".equalsIgnoreCase(servletRequest.getParameter("grant_type"))
				&& servletRequest.getCookies() != null) {

			String refreshToken =  Stream.of(servletRequest.getCookies())
					.filter(cookie -> "refreshToken".equals(cookie.getName()))
					.findFirst()
					.map(cookie -> cookie.getValue())
					.orElse(null);

			servletRequest = new MyServletRequestWrapper(servletRequest, refreshToken);

		}

		chain.doFilter(servletRequest, response);
	}

	static class MyServletRequestWrapper extends HttpServletRequestWrapper {
		private String refreshToken;

		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken });
			map.setLocked(true);
			return map;
		}
	}

}
