package com.ivanmatuck.apivotacao.service;

import com.ivanmatuck.apivotacao.dto.*;

import java.util.List;

public interface VotacaoService {
    List<VotacaoResponseDto> listVotacaos();
    VotacaoResponseDto getVotacao(String id);
    VotacaoResponseDto createVotacao(VotacaoRequestDto dto);
    VoteResponseDto addVote(VoteRequestDto dto);
    VotacaoResultResponseDto getVotacaoResult(String id);
}
