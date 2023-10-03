package dev.wakandaacademy.produdoro.tarefa.application.repository;

import java.util.Optional;
import java.util.UUID;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import java.util.List;

public interface TarefaRepository {

    Tarefa salva(Tarefa tarefa);
    Optional<Tarefa> buscaTarefaPorId(UUID idTarefa);
	List<Tarefa> buscaTarefasPorUsuario(UUID idUsuario);
	void deletaTarefaPorId(Tarefa tarefa);
	void desativaTarefas(UUID idUsuario);
}
