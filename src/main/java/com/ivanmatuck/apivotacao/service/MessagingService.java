package com.ivanmatuck.apivotacao.service;

import com.ivanmatuck.apivotacao.dto.VotacaoResultResponseDto;

public interface MessagingService {
    void send(VotacaoResultResponseDto votacaoResult);
}
