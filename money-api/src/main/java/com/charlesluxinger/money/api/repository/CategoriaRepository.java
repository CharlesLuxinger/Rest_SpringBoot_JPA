package com.charlesluxinger.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.charlesluxinger.money.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
