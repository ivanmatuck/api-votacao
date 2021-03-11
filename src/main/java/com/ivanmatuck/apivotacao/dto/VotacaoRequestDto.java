package com.ivanmatuck.apivotacao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotacaoRequestDto {

    private String agendaId;

    private Integer minutesToExpiration;

}
