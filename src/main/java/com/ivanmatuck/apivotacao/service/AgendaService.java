package com.ivanmatuck.apivotacao.service;

import com.ivanmatuck.apivotacao.dto.AgendaRequestDto;
import com.ivanmatuck.apivotacao.dto.AgendaResponseDto;

import java.util.List;

public interface AgendaService {
    List<AgendaResponseDto> listAgendas();
    AgendaResponseDto getAgenda(String id);
    AgendaResponseDto createAgenda(AgendaRequestDto dto);
}
