package com.ivanmatuck.apivotacao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "votacao")
public class Votacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private UUID id;

    private Agenda agenda;

    private Integer minutesToExpiration;

    private Instant expirationDate;

    private List<Vote> votes;

    private boolean closed;

    public Votacao(Agenda agenda, Integer minutesToExpiration) {
        this.agenda = agenda;
        this.minutesToExpiration = minutesToExpiration;
        this.expirationDate = Instant.now().plusSeconds(minutesToExpiration * 60);
        this.votes = new ArrayList<>();
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public boolean cpfAlreadyVoted(String cpf){
        return this.votes.stream().anyMatch(vote -> vote.getCpf().equals(cpf));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Votacao votacao = (Votacao) o;
        return Objects.equals(id, votacao.id) &&
                Objects.equals(minutesToExpiration, votacao.minutesToExpiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, minutesToExpiration);
    }
}
