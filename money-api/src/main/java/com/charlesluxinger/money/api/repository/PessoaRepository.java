package com.charlesluxinger.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.charlesluxinger.money.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
