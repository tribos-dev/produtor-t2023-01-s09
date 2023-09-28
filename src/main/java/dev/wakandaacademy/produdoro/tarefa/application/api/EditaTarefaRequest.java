package dev.wakandaacademy.produdoro.tarefa.application.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EditaTarefaRequest {
	@NotBlank
	@Size(message = "Campo descrição tarefa não pode estar vazio", max = 255, min = 3)
	private String descricao;
}
