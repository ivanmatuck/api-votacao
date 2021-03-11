package com.ivanmatuck.apivotacao.api;

import com.ivanmatuck.apivotacao.dto.*;
import com.ivanmatuck.apivotacao.entity.VoteCount;
import com.ivanmatuck.apivotacao.service.VotacaoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
class VotacaoControllerTest {

    @Mock
    public VotacaoService votacaoService;

    @InjectMocks
    public VotacaoController votacaoController;

    @Test
    public void shouldReturnZeroVotacaos() {
        ResponseEntity<?> resp = votacaoController.getAll();

        assertEquals(resp.getStatusCode(), HttpStatus.OK);
        assertEquals(0, ((LinkedList<?>) resp.getBody()).size());
    }

    @Test
    public void shouldReturnVotacaos() {
        List<VotacaoResponseDto> listDto = new ArrayList<>();
        VotacaoResponseDto votacao = new VotacaoResponseDto();
        listDto.add(votacao);

        Mockito.when(votacaoService.listVotacaos()).thenReturn(listDto);

        ResponseEntity<?> resp = votacaoController.getAll();

        assertEquals(resp.getStatusCode(), HttpStatus.OK);
        assertEquals(1, ((ArrayList<?>) resp.getBody()).size());
    }

    @Test
    public void shouldCreateVotacao() throws URISyntaxException {
        VotacaoRequestDto req = new VotacaoRequestDto();
        VotacaoResponseDto resp = new VotacaoResponseDto();

        resp.setId(UUID.randomUUID().toString());
        Mockito.when(votacaoService.createVotacao(req)).thenReturn(resp);

        ResponseEntity<?> response = votacaoController.create(req);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldVote() throws URISyntaxException {
        VoteRequestDto req = new VoteRequestDto();
        VoteResponseDto resp = new VoteResponseDto(true);

        Mockito.when(votacaoService.addVote(req)).thenReturn(resp);

        ResponseEntity<?> response = votacaoController.vote(req);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldGetVotacaoResult(){
        String id = UUID.randomUUID().toString();
        VotacaoResultResponseDto resp = new VotacaoResultResponseDto();
        resp.setAgenda(new AgendaResponseDto(id, "test"));
        resp.setVoteCount(new VoteCount(1L,2L));

        Mockito.when(votacaoService.getVotacaoResult(id)).thenReturn(resp);

        ResponseEntity<?> response = votacaoController.getVotacaoResult(id);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
    }
}