package com.ivanmatuck.apivotacao.service;

import com.ivanmatuck.apivotacao.dto.AgendaRequestDto;
import com.ivanmatuck.apivotacao.dto.AgendaResponseDto;
import com.ivanmatuck.apivotacao.entity.Agenda;
import com.ivanmatuck.apivotacao.exception.NotFoundException;
import com.ivanmatuck.apivotacao.repository.AgendaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class AgendaServiceTest {

    @Mock
    public AgendaRepository agendaRepository;

    @Mock
    public ModelMapper modelMapper;

    @InjectMocks
    public AgendaServiceImpl agendaService;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(agendaService, "modelMapper", new ModelMapper());
    }

    @Test
    public void shouldReturnZeroAgendas() {
        List<AgendaResponseDto> resp = agendaService.listAgendas();
        assertEquals(0, resp.size());
    }

    @Test
    public void shouldReturnAgendas() {
        List<Agenda> agendas = new ArrayList<>();
        agendas.add(new Agenda("test1"));
        agendas.add(new Agenda("test2"));

        Mockito.when(agendaRepository.findAll()).thenReturn(agendas);

        List<AgendaResponseDto> resp = agendaService.listAgendas();
        assertEquals(2, resp.size());
    }

    @Test
    public void shouldReturnOneAgenda() {
        UUID id = UUID.randomUUID();
        Agenda agenda = new Agenda("test1");
        agenda.setId(id);

        Mockito.when(agendaRepository.findById(id)).thenReturn(java.util.Optional.of(agenda));

        AgendaResponseDto resp = agendaService.getAgenda(id.toString());
        assertEquals(id.toString(), resp.getId());
        assertEquals(agenda.getName(), resp.getName());
    }

    @Test
    public void shouldCreateAgenda() {
        UUID id = UUID.randomUUID();
        String name = "test1";

        Agenda agenda = new Agenda(name);
        agenda.setId(id);

        Mockito.when(agendaRepository.save(new Agenda(name))).thenReturn(agenda);

        AgendaRequestDto req = new AgendaRequestDto();
        req.setName(name);
        AgendaResponseDto resp = agendaService.createAgenda(req);

        assertEquals(id.toString(), resp.getId());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundException(){
        UUID id = UUID.randomUUID();
        Mockito.when(agendaRepository.findById(id)).thenThrow(new NotFoundException("Agenda not found."));

        agendaService.getAgenda(id.toString());
    }
}