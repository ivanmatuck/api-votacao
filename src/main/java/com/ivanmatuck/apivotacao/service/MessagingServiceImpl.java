package com.ivanmatuck.apivotacao.service;

import com.ivanmatuck.apivotacao.dto.VotacaoResultResponseDto;
import com.ivanmatuck.apivotacao.kafka.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingServiceImpl implements MessagingService {

    private final Producer producer;

    @Autowired
    public MessagingServiceImpl(Producer producer) {
        this.producer = producer;
    }

    @Override
    public void send(VotacaoResultResponseDto votacaoResult) {
        producer.send(String.format("Agenda '%s' closed! Votes: [Yes= %d] ~ [No= %d]",
                votacaoResult.getAgenda().getName(),
                votacaoResult.getVoteCount().getYes(),
                votacaoResult.getVoteCount().getNo()
        ));
    }
}
