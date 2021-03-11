package com.ivanmatuck.apivotacao.service;

import com.ivanmatuck.apivotacao.dto.*;
import com.ivanmatuck.apivotacao.entity.Agenda;
import com.ivanmatuck.apivotacao.entity.Answer;
import com.ivanmatuck.apivotacao.entity.Vote;
import com.ivanmatuck.apivotacao.entity.Votacao;
import com.ivanmatuck.apivotacao.exception.BusinessException;
import com.ivanmatuck.apivotacao.exception.NotFoundException;
import com.ivanmatuck.apivotacao.integration.CpfService;
import com.ivanmatuck.apivotacao.repository.AgendaRepository;
import com.ivanmatuck.apivotacao.repository.VotacaoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(MockitoJUnitRunner.class)
class VotacaoServiceTest {

    @Mock
    public VotacaoRepository votacaoRepository;

    @Mock
    public AgendaRepository agendaRepository;

    @Mock
    public CpfService cpfService;

    @Mock
    public ModelMapper modelMapper;

    @InjectMocks
    public VotacaoServiceImpl votacaoService;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(votacaoService, "modelMapper", new ModelMapper());
    }

    @Test
    public void shouldReturnZeroVotacaos() {
        List<VotacaoResponseDto> resp = votacaoService.listVotacaos();
        assertEquals(0, resp.size());
    }

    @Test
    public void shouldReturnVotacaos() {
        List<Votacao> votacaos = new ArrayList<>();
        votacaos.add(new Votacao());
        votacaos.add(new Votacao());

        Mockito.when(votacaoRepository.findAll()).thenReturn(votacaos);

        List<VotacaoResponseDto> resp = votacaoService.listVotacaos();
        assertEquals(2, resp.size());
    }

    @Test
    public void shouldReturnOneVotacao() {
        UUID id = UUID.randomUUID();
        Votacao votacao = new Votacao();
        votacao.setId(id);

        Mockito.when(votacaoRepository.findById(id)).thenReturn(java.util.Optional.of(votacao));

        VotacaoResponseDto resp = votacaoService.getVotacao(id.toString());
        assertEquals(id.toString(), resp.getId());
    }

    @Test
    public void shouldCreateVotacao() {
        UUID id = UUID.randomUUID();
        Agenda agenda = new Agenda("test");

        Votacao votacao = new Votacao(agenda, 10);
        votacao.setId(id);

        Mockito.when(agendaRepository.findById(id)).thenReturn(java.util.Optional.of(agenda));
        Mockito.when(votacaoRepository.save(new Votacao(agenda, 10))).thenReturn(votacao);

        VotacaoRequestDto req = new VotacaoRequestDto();
        req.setAgendaId(votacao.getId().toString());
        req.setMinutesToExpiration(10);
        VotacaoResponseDto resp = votacaoService.createVotacao(req);

        assertEquals(id.toString(), resp.getId());
    }

    @Test(expected = NotFoundException.class)
    public void shouldNotCreateVotacaoWhenAgendaNotExists() {
        UUID id = UUID.randomUUID();
        Agenda agenda = new Agenda("test");

        Votacao votacao = new Votacao(agenda, 10);
        votacao.setId(id);

        VotacaoRequestDto req = new VotacaoRequestDto();
        req.setAgendaId(votacao.getId().toString());

        votacaoService.createVotacao(req);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundException(){
        UUID id = UUID.randomUUID();
        Mockito.when(votacaoRepository.findById(id)).thenThrow(new NotFoundException("Votacao not found."));

        votacaoService.getVotacao(id.toString());
    }

    @Test
    public void shouldVote(){
        UUID id = UUID.randomUUID();
        Agenda agenda = new Agenda("test");

        Votacao votacao = new Votacao(agenda, 10);
        Mockito.when(votacaoRepository.findById(id)).thenReturn(java.util.Optional.of(votacao));

        Votacao votacao2 = new Votacao(agenda, 10);
        votacao2.setId(id);

        Vote vote = new Vote("123", Answer.NO);
        votacao2.addVote(vote);

        Mockito.when(votacaoRepository.save(votacao)).thenReturn(votacao2);

        VoteRequestDto dto = new VoteRequestDto();
        dto.setCpf("123");
        dto.setVotacaoId(votacao2.getId().toString());
        dto.setAnswer(Answer.NO);

        VoteResponseDto resp = votacaoService.addVote(dto);

        assertTrue(resp.isSuccess());
    }

    @Test(expected = BusinessException.class)
    public void shouldThrowVotacaoExpired(){
        UUID id = UUID.randomUUID();
        Agenda agenda = new Agenda("test");
        Votacao votacao = new Votacao(agenda, 1);

        votacao.setId(id);
        votacao.setExpirationDate(votacao.getExpirationDate().minusSeconds(61));

        Mockito.when(votacaoRepository.findById(id)).thenReturn(java.util.Optional.of(votacao));

        VoteRequestDto dto = new VoteRequestDto();
        dto.setCpf("123");
        dto.setVotacaoId(votacao.getId().toString());
        dto.setAnswer(Answer.NO);
        votacaoService.addVote(dto);
    }

    @Test(expected = BusinessException.class)
    public void shouldNotReturnResultWhenVotacaoIsOpen(){
        UUID id = UUID.randomUUID();
        Agenda agenda = new Agenda("test");
        agenda.setId(id);

        Votacao votacao = new Votacao(new Agenda("test"), 10);
        votacao.addVote(new Vote("1", Answer.NO));

        Mockito.when(votacaoRepository.findById(id)).thenReturn(java.util.Optional.of(votacao));

        votacaoService.getVotacaoResult(id.toString());
    }

    @Test
    public void shouldReturnVotacaoResult(){
        UUID id = UUID.randomUUID();
        Agenda agenda = new Agenda("test");
        agenda.setId(id);

        Votacao votacao = new Votacao(new Agenda("test"), 1);
        votacao.addVote(new Vote("1", Answer.NO));
        votacao.addVote(new Vote("2", Answer.NO));
        votacao.addVote(new Vote("3", Answer.YES));
        votacao.setExpirationDate(Instant.now().minusSeconds(10));

        Mockito.when(votacaoRepository.findById(id)).thenReturn(java.util.Optional.of(votacao));

        VotacaoResultResponseDto resp = votacaoService.getVotacaoResult(id.toString());

        assertEquals(2, resp.getVoteCount().getNo());
        assertEquals(1, resp.getVoteCount().getYes());
    }
}