package com.ivanmatuck.apivotacao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Answer {

    NO("Não"),
    YES("Sim");

    private String answer;

}
