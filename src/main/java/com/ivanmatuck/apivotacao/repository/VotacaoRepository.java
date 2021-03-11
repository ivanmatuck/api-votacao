package com.ivanmatuck.apivotacao.repository;

import com.ivanmatuck.apivotacao.entity.Votacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VotacaoRepository extends JpaRepository<Votacao, UUID> {
}
