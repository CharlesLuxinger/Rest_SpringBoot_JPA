package com.charlesluxinger.money.api.repository.lancamento;

import java.util.List;

import com.charlesluxinger.money.api.model.Lancamento;
import com.charlesluxinger.money.api.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
	public List<Lancamento> filter(LancamentoFilter lancamentoFilter);
}
