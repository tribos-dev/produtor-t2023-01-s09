package dev.wakandaacademy.produdoro.tarefa.application.service;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.EditaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import javax.validation.Valid;

@Service
@Log4j2
@RequiredArgsConstructor

public class TarefaApplicationService implements TarefaService {
	private final TarefaRepository tarefaRepository;
	private final UsuarioRepository usuarioRepository;

	@Override
	public TarefaIdResponse criaNovaTarefa(TarefaRequest tarefaRequest) {
		log.info("[inicia] TarefaApplicationService - criaNovaTarefa");
		Tarefa tarefaCriada = tarefaRepository.salva(new Tarefa(tarefaRequest));
		log.info("[finaliza] TarefaApplicationService - criaNovaTarefa");
		return TarefaIdResponse.builder().idTarefa(tarefaCriada.getIdTarefa()).build();
	}

	@Override
	public Tarefa detalhaTarefa(String usuario, UUID idTarefa) {
		log.info("[inicia] TarefaApplicationService - detalhaTarefa");
		Usuario usuarioPorEmail = usuarioRepository.buscaUsuarioPorEmail(usuario);
		log.info("[usuarioPorEmail] {}", usuarioPorEmail);
		Tarefa tarefa = tarefaRepository.buscaTarefaPorId(idTarefa)
				.orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Tarefa não encontrada!"));
		tarefa.pertenceAoUsuario(usuarioPorEmail);
		log.info("[finaliza] TarefaApplicationService - detalhaTarefa");
		return tarefa;
	}

	@Override
	public void deletaTarefa(UUID idTarefa, String usuario) {
		log.info("[inicia] TarefaApplicationService - deletaTarefa");
		tarefaRepository.deletaTarefaPorId(detalhaTarefa(usuario, idTarefa));
		log.info("[finaliza] TarefaApplicationService - deletaTarefa");
	}

	@Override
	public void incrementaPomodoro(UUID idTarefa, String usuarioEmail) {
		log.info("[inicia] TarefaApplicationService - incrementaPomodoro");
		Tarefa tarefa = detalhaTarefa(usuarioEmail, idTarefa);
		tarefa.incrementaContagemPomodoro();
		tarefaRepository.salva(tarefa);
		log.info("[finaliza] TarefaApplicationService - incrementaPomodoro");
    }
    
    @Override
	public void ativaTarefa(UUID idTarefa, String emailUsuario) {
        log.info("[inicia] TarefaApplicationService - ativaTarefa");
        Usuario usuarioPorEmail = usuarioRepository.buscaUsuarioPorEmail(emailUsuario);
        Tarefa tarefa =
                tarefaRepository.buscaTarefaPorId(idTarefa)
                .orElseThrow(() -> APIException.build(HttpStatus.BAD_REQUEST, "id da tarefa invalido"));
        tarefa.pertenceAoUsuario(usuarioPorEmail);
        tarefaRepository.desativaTarefas(usuarioPorEmail.getIdUsuario());
        tarefa.ativaTarefa();
        tarefaRepository.salva(tarefa);
        log.info("[finaliza] TarefaApplicationService - ativaTarefa");
	}
	
	public void editaTarefa(String usuario, UUID idTarefa, @Valid EditaTarefaRequest editaTarefaRequest) {
		log.info("[inicia] TarefaApplicationService - editaTarefa");
		Tarefa tarefa = detalhaTarefa(usuario, idTarefa);
		tarefa.altera(editaTarefaRequest);
		tarefaRepository.salva(tarefa);
		log.info("[finaliza] TarefaApplicationService - editaTarefa");
    }

	@Override
	public List<TarefaDetalhadoResponse> buscarTarefasPorUsuario(String emailUsuario, UUID idUsuario) {
        log.info("[inicia] TarefaApplicationService - buscarTarefasPorUsuario");
        Usuario usuario = usuarioRepository.buscaUsuarioPorEmail(emailUsuario);
        usuarioRepository.buscaUsuarioPorId(idUsuario);
        validaIdUsuario(idUsuario, usuario.getIdUsuario());
        List<Tarefa> tarefas = tarefaRepository.buscaTarefasPorUsuario(idUsuario);
        log.info("[finaliza] TarefaApplicationService - buscarTarefasPorUsuario");
		return TarefaDetalhadoResponse.converteListaTarefas(tarefas);
	}

	private void validaIdUsuario(UUID idUsuarioRequest, UUID idUsuario) {
        log.info("[inicia] TarefaApplicationService - validaIdUsuario");
        if(!idUsuarioRequest.equals(idUsuario)) {
			throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não autorizado");
        }
        log.info("[finaliza] TarefaApplicationService - validaIdUsuario");
    }
    
	public void concluiTarefa(String usuario, UUID idTarefa) {
		log.info("[inicia] TarefaApplicationService - concluiTarefa");
		Usuario usuarioPorEmail = usuarioRepository.buscaUsuarioPorEmail(usuario);
		log.info("[usuarioPorEmail] {}", usuarioPorEmail);
		Tarefa tarefa =
				tarefaRepository.buscaTarefaPorId(idTarefa).orElseThrow(() -> APIException.build(HttpStatus.BAD_REQUEST, "Tarefa não encontrada!"));
		tarefa.pertenceAoUsuario(usuarioPorEmail);
		tarefa.concluiTarefa();
		tarefaRepository.salva(tarefa);
		log.info("[finaliza] TarefaApplicationService - concluiTarefa");
	}
}
