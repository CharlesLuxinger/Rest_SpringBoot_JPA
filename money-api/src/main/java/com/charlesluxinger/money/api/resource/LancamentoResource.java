package com.charlesluxinger.money.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.charlesluxinger.money.api.event.InsertResourceEvent;
import com.charlesluxinger.money.api.model.Lancamento;
import com.charlesluxinger.money.api.repository.LancamentoRepository;

@RestController
@RequestMapping("/lancamento")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Lancamento> findAll() {
		return lancamentoRepository.findAll();
	}

	@PostMapping
	public ResponseEntity<Lancamento> insert(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoInserted = lancamentoRepository.save(lancamento);

		publisher.publishEvent(new InsertResourceEvent(this, response, lancamentoInserted.getId()));

		return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> findById(@PathVariable long id) {
		Optional<Lancamento> lancamento = this.lancamentoRepository.findById(id);

		return (lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build());
	}
}
