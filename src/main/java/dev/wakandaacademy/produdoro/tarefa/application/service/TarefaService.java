package dev.wakandaacademy.produdoro.tarefa.application.service;

import dev.wakandaacademy.produdoro.tarefa.application.api.EditaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import java.util.UUID;
import javax.validation.Valid;

public interface TarefaService {
    TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest);
    Tarefa detalhaTarefa(String usuario, UUID idTarefa);
	void ativaTarefa(UUID idTarefa, String emailUsuario);
	void editaTarefa(String usuario, UUID idTarefa, @Valid EditaTarefaRequest editaTarefaRequest);
	void concluiTarefa(String usuario, UUID idTarefa);
}
