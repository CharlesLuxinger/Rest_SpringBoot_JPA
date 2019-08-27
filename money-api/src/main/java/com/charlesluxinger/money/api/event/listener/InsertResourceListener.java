package com.charlesluxinger.money.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.charlesluxinger.money.api.event.InsertResourceEvent;

@Component
public class InsertResourceListener implements ApplicationListener<InsertResourceEvent> {

	@Override
	public void onApplicationEvent(InsertResourceEvent event) {
		setUri(event.getResponse(), event.getId());
	}

	private void setUri(HttpServletResponse response, Long id) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(id).toUri();

		response.setHeader("Location", uri.toASCIIString());
	}

}
