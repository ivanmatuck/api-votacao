package com.ivanmatuck.apivotacao.repository;

import com.ivanmatuck.apivotacao.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface AgendaRepository extends JpaRepository<Agenda, UUID> {
}
