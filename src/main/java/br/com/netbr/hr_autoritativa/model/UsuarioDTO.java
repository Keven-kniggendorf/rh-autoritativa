package br.com.netbr.hr_autoritativa.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsuarioDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotNull
    @Size(max = 255)
    private String nome;

    @NotNull
    @Size(max = 255)
    @UsuarioEmailUnique
    private String email;

    @NotNull
    @Size(max = 50)
    private String funcao;

//    @NotNull
//    private LocalDateTime dataAdmissao;
//
//    @NotNull
//    private LocalDateTime dataDemissao;
//
//    @NotNull
//    private LocalDateTime dataAtualizacao;

    @NotNull
    private Status status;


}
