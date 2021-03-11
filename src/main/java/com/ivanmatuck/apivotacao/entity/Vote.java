package com.ivanmatuck.apivotacao.entity;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    private String cpf;
    private Answer answer;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(cpf, vote.cpf) &&
                answer == vote.answer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf, answer);
    }
}
