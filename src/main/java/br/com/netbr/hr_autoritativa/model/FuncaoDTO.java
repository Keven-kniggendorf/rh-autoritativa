package br.com.netbr.hr_autoritativa.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FuncaoDTO {

    private Long id;

    @NotNull
    @Size(max = 80)
    private String nomeFuncao;

    @NotNull
    @Size(max = 80)
    private String departamento;

}
