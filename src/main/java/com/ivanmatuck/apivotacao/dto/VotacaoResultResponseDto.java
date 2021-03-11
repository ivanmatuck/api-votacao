package com.ivanmatuck.apivotacao.dto;

import com.ivanmatuck.apivotacao.entity.VoteCount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotacaoResultResponseDto {
    private AgendaResponseDto agenda;
    private VoteCount voteCount;

}
