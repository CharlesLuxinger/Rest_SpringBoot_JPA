package com.charlesluxinger.money.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.charlesluxinger.money.api.config.property.MoneyApiProperty;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

	@Autowired
	private MoneyApiProperty moneyApiProperty;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
					ServerHttpRequest serverRequest, ServerHttpResponse serverResponse) {

		HttpServletRequest request = ((ServletServerHttpRequest) serverRequest).getServletRequest();
		HttpServletResponse response = ((ServletServerHttpResponse) serverResponse).getServletResponse();

		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;

		String refreshToken = body.getRefreshToken().getValue();

		addRefreshTokenToCookie(refreshToken, request, response);
		removeRefreshTokenOfBody(token);

		return body;
	}

	private void removeRefreshTokenOfBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);
	}

	private void addRefreshTokenToCookie(String refreshToken, HttpServletRequest request,
			HttpServletResponse response) {
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(moneyApiProperty.getSecurity().isEnableHttps());
		refreshTokenCookie.setPath(request.getContextPath() + "/oauth/token");
		refreshTokenCookie.setMaxAge(2592000); // 30 dias

		response.addCookie(refreshTokenCookie);
	}

}
