package com.charlesluxinger.money.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.charlesluxinger.money.api.model.Categoria_;
import com.charlesluxinger.money.api.model.Lancamento;
import com.charlesluxinger.money.api.model.Lancamento_;
import com.charlesluxinger.money.api.model.Pessoa_;
import com.charlesluxinger.money.api.repository.filter.LancamentoFilter;
import com.charlesluxinger.money.api.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Lancamento> filter(LancamentoFilter lancamentoFilter, Pageable pageable) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteriaQuery = builder.createQuery(Lancamento.class);

		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		Predicate[] predicates = getRetricoes(lancamentoFilter, builder, root);
		criteriaQuery.where(predicates);

		TypedQuery<Lancamento> query = entityManager.createQuery(criteriaQuery);

		addRestricaoPaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	@Override
	public Page<ResumoLancamento> resume(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);

		criteria.select(builder.construct(ResumoLancamento.class,
											root.get(Lancamento_.ID),
											root.get(Lancamento_.DESCRICAO),
											root.get(Lancamento_.DATA_VENCIMENTO),
											root.get(Lancamento_.DATA_PAGAMENTO),
											root.get(Lancamento_.VALOR),
											root.get(Lancamento_.TIPO),
											root.get(Lancamento_.CATEGORIA).get(Categoria_.NOME),
											root.get(Lancamento_.PESSOA).get(Pessoa_.NOME)));

		Predicate[] predicates = getRetricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<ResumoLancamento> query = entityManager.createQuery(criteria);

		addRestricaoPaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	private Predicate[] getRetricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (lancamentoFilter.getDescricao() != null) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.DESCRICAO)),
					"%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}

		if (lancamentoFilter.getDataVencimentoInit() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO),
					lancamentoFilter.getDataVencimentoInit()));
		}

		if (lancamentoFilter.getDataVencimentoLast() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO),
					lancamentoFilter.getDataVencimentoLast()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void addRestricaoPaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	private Long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

		Root<Lancamento> root = criteria.from(Lancamento.class);

		Predicate[] predicates = getRetricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));

		return entityManager.createQuery(criteria).getSingleResult();
	}

}
