package com.ivanmatuck.apivotacao.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
class VotacaoTest {

    @Test
    public void shouldReturnTrueIfExpired() {
        Votacao votacao = new Votacao();
        votacao.setExpirationDate(Instant.now().minusSeconds(30));

        assertTrue(votacao.getExpirationDate().isBefore(Instant.now()));
    }

    @Test
    public void shouldReturnFalseIfNotExpired() {
        Votacao votacao = new Votacao();
        votacao.setExpirationDate(Instant.now().plusSeconds(30));

        assertFalse(votacao.getExpirationDate().isBefore(Instant.now()));
    }

    @Test
    public void shouldValidateCpfAlreadyVoted(){
        Votacao votacao = new Votacao();

        votacao.addVote(new Vote("111", Answer.NO));
        votacao.addVote(new Vote("222", Answer.YES));
        votacao.addVote(new Vote("111", Answer.YES));

        assertTrue(votacao.cpfAlreadyVoted("111"));
    }

}