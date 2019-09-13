package com.charlesluxinger.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.charlesluxinger.money.api.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
