package com.ivanmatuck.apivotacao.dto;

import com.ivanmatuck.apivotacao.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotacaoResponseDto {

    private String id;

    private AgendaResponseDto agenda;

    private Integer minutesToExpiration;

    private Instant expirationDate;

    private List<Vote> votes;

    private boolean closed;

}
