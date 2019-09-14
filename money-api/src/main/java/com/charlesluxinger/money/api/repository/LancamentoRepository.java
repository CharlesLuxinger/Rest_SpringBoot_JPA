package com.charlesluxinger.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.charlesluxinger.money.api.model.Lancamento;
import com.charlesluxinger.money.api.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}
