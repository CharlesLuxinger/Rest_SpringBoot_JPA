package com.charlesluxinger.money.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.charlesluxinger.money.api.model.Pessoa;
import com.charlesluxinger.money.api.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa update(Long id, Pessoa pessoa) {
		Pessoa pessoaSaved = pessoaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));

		BeanUtils.copyProperties(pessoa, pessoaSaved, "id");

		return pessoaRepository.save(pessoaSaved);
	}
}
