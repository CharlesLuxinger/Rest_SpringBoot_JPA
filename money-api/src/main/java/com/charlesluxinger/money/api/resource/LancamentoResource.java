package com.charlesluxinger.money.api.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.charlesluxinger.money.api.event.InsertResourceEvent;
import com.charlesluxinger.money.api.exceptionshandler.MoneyExceptionsHandler.Erro;
import com.charlesluxinger.money.api.model.Lancamento;
import com.charlesluxinger.money.api.repository.LancamentoRepository;
import com.charlesluxinger.money.api.repository.filter.LancamentoFilter;
import com.charlesluxinger.money.api.repository.projection.ResumoLancamento;
import com.charlesluxinger.money.api.service.LancamentoService;
import com.charlesluxinger.money.api.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> filter(LancamentoFilter lancamentoFilter, Pageable pageable) {
		System.out.println("********");
		return lancamentoRepository.filter(lancamentoFilter, pageable);
	}

	@GetMapping(params = "resume")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<ResumoLancamento> resume(LancamentoFilter lancamentoFilter, Pageable pageable) {
		System.out.println("##########");
		return lancamentoRepository.resume(lancamentoFilter, pageable);
	}

	//	@GetMapping
	//	public List<Lancamento> findAll() {
	//		return lancamentoRepository.findAll();
	//	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> insert(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoInserted = lancamentoService.save(lancamento);

		publisher.publishEvent(new InsertResourceEvent(this, response, lancamentoInserted.getId()));

		return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> findById(@PathVariable long id) {
		Optional<Lancamento> lancamento = this.lancamentoRepository.findById(id);

		return (lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	public void remove(@PathVariable long id) {
		lancamentoRepository.deleteById(id);
	}

	@ExceptionHandler({PessoaInexistenteOuInativaException.class})
	protected ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {

		String messageUser = messageSource.getMessage("pessoa.invalida", null, LocaleContextHolder.getLocale());
		String messageDeveloper = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

		List<Erro> erros = Arrays.asList(new Erro(messageUser, messageDeveloper));

		return ResponseEntity.badRequest().body(erros);
	}

}
