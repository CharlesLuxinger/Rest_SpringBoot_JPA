package com.charlesluxinger.money.api.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charlesluxinger.money.api.model.Lancamento;
import com.charlesluxinger.money.api.model.Pessoa;
import com.charlesluxinger.money.api.repository.LancamentoRepository;
import com.charlesluxinger.money.api.repository.PessoaRepository;
import com.charlesluxinger.money.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	public Lancamento save(@Valid Lancamento lancamento) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getId());

		if (pessoa.get().getStatus() && pessoa.isPresent()) {
			return lancamentoRepository.save(lancamento);
		}

		throw new PessoaInexistenteOuInativaException();
	}

}
