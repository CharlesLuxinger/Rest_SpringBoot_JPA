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

import com.charlesluxinger.money.api.model.Lancamento;
import com.charlesluxinger.money.api.model.Lancamento_;
import com.charlesluxinger.money.api.repository.filter.LancamentoFilter;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Lancamento> filter(LancamentoFilter lancamentoFilter) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteriaQuery = builder.createQuery(Lancamento.class);

		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

		Predicate[] predicates = getRetricoes(lancamentoFilter, builder, root);
		criteriaQuery.where(predicates);

		TypedQuery<Lancamento> query = entityManager.createQuery(criteriaQuery);

		return query.getResultList();
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

}
