package com.ivanmatuck.apivotacao.service;

import com.ivanmatuck.apivotacao.dto.AgendaRequestDto;
import com.ivanmatuck.apivotacao.dto.AgendaResponseDto;
import com.ivanmatuck.apivotacao.entity.Agenda;
import com.ivanmatuck.apivotacao.exception.NotFoundException;
import com.ivanmatuck.apivotacao.repository.AgendaRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendaServiceImpl implements AgendaService {

    private final AgendaRepository agendaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AgendaServiceImpl(AgendaRepository agendaRepository, ModelMapper modelMapper) {
        this.agendaRepository = agendaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AgendaResponseDto> listAgendas() {
        List<Agenda> agendaList = this.agendaRepository.findAll();

        return agendaList.stream().map(
            agenda -> modelMapper.map(agenda, AgendaResponseDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public AgendaResponseDto getAgenda(String id) {
        Agenda agenda = this.agendaRepository.findById(UUID.randomUUID()).
                orElseThrow(() -> new NotFoundException("Agenda not found."));

        return modelMapper.map(agenda, AgendaResponseDto.class);
    }

    @Override
    public AgendaResponseDto createAgenda(AgendaRequestDto agendaRequestdto) {
        Agenda agenda = new Agenda(agendaRequestdto.getName());
        agenda = agendaRepository.save(agenda);

        return modelMapper.map(agenda, AgendaResponseDto.class);
    }
}
