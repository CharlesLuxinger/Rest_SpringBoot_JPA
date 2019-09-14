package com.charlesluxinger.money.api.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class LancamentoFilter {

	private String descricao;

	@DateTimeFormat(pattern = "yyyyMMdd")
	private LocalDate dataVencimentoInit;

	@DateTimeFormat(pattern = "yyyyMMdd")
	private LocalDate dataVencimentoLast;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataVencimentoInit() {
		return dataVencimentoInit;
	}

	public void setDataVencimentoInit(LocalDate dataVencimentoInit) {
		this.dataVencimentoInit = dataVencimentoInit;
	}

	public LocalDate getDataVencimentoLast() {
		return dataVencimentoLast;
	}

	public void setDataVencimentoLast(LocalDate dataVencimentoLast) {
		this.dataVencimentoLast = dataVencimentoLast;
	}
}
