package com.ivanmatuck.apivotacao.dto;

import com.ivanmatuck.apivotacao.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequestDto {
    private String votacaoId;
    private String cpf;
    private Answer answer;

}
