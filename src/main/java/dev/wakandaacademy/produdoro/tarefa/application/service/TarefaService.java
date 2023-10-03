package dev.wakandaacademy.produdoro.tarefa.application.service;

import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import java.util.List;
import dev.wakandaacademy.produdoro.tarefa.application.api.EditaTarefaRequest;
import java.util.UUID;
import javax.validation.Valid;

public interface TarefaService {
    TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest);
    Tarefa detalhaTarefa(String usuario, UUID idTarefa);
	List<TarefaDetalhadoResponse> buscarTarefasPorUsuario(String emailUsuario, UUID idUsuario);
	void incrementaPomodoro(UUID idTarefa, String usuarioEmail);
	void ativaTarefa(UUID idTarefa, String emailUsuario);
	void editaTarefa(String usuario, UUID idTarefa, @Valid EditaTarefaRequest editaTarefaRequest);
	void concluiTarefa(String usuario, UUID idTarefa);
   	void deletaTarefa(UUID idTarefa, String usuario);
}
