package com.charlesluxinger.money.api.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.charlesluxinger.money.api.model.Lancamento;
import com.charlesluxinger.money.api.repository.filter.LancamentoFilter;
import com.charlesluxinger.money.api.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {
	public Page<Lancamento> filter(LancamentoFilter lancamentoFilter, Pageable pageable);

	public Page<ResumoLancamento> resume(LancamentoFilter lancamentoFilter, Pageable pageable);
}
