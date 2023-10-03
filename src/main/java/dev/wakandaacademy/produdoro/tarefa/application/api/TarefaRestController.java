package dev.wakandaacademy.produdoro.tarefa.application.api;

import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import dev.wakandaacademy.produdoro.config.security.service.TokenService;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.service.TarefaService;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@RestController
@Log4j2
@RequiredArgsConstructor
public class TarefaRestController implements TarefaAPI {
	private final TarefaService tarefaService;
	private final TokenService tokenService;

	public TarefaIdResponse postNovaTarefa(TarefaRequest tarefaRequest) {
		log.info("[inicia]  TarefaRestController - postNovaTarefa  ");
		TarefaIdResponse tarefaCriada = tarefaService.criaNovaTarefa(tarefaRequest);
		log.info("[finaliza]  TarefaRestController - postNovaTarefa");
		return tarefaCriada;
	}

	@Override
	public TarefaDetalhadoResponse detalhaTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - detalhaTarefa");
		String usuario = getUsuarioByToken(token);
		Tarefa tarefa = tarefaService.detalhaTarefa(usuario,idTarefa);
		log.info("[finaliza] TarefaRestController - detalhaTarefa");
		return new TarefaDetalhadoResponse(tarefa);
	}

	private String getUsuarioByToken(String token) {
		log.debug("[token] {}", token);
		String usuario = tokenService.getUsuarioByBearerToken(token).orElseThrow(() -> APIException.build(HttpStatus.UNAUTHORIZED, token));
		log.info("[usuario] {}", usuario);
		return usuario;
	}
	
	@Override
	public void deletaTarefa(UUID idTarefa, String token) {
		log.info("[inicia] TarefaRestController - deletaTarefa");
		String usuario = getUsuarioByToken(token);
		tarefaService.deletaTarefa(idTarefa, usuario);
		log.info("[finaliza] TarefaRestController - deletaTarefa");
	}

	@Override
	public void incrementaPomodoro(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - incrementaPomodoro");
		String usuarioEmail = getUsuarioByToken(token);
		tarefaService.incrementaPomodoro(idTarefa, usuarioEmail);
		log.info("[finaliza] TarefaRestController - incrementaPomodoro");
	}

	public void ativaTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - ativaTarefa");
		String emailUsuario = getUsuarioByToken(token);
		tarefaService.ativaTarefa(idTarefa, emailUsuario);
		log.info("[finaliza] TarefaRestController - ativaTarefa");
	}

	public void editaTarefa(String token, UUID idTarefa, @Valid EditaTarefaRequest editaTarefaRequest) {
		log.info("[inicia] TarefaRestController - editaTarefa");
		String usuario = getUsuarioByToken(token);
		tarefaService.editaTarefa(usuario, idTarefa, editaTarefaRequest);
		log.info("[finaliza] TarefaRestController - editaTarefa");	
	}

	public void concluiTarefa(String token, UUID idTarefa) {
		log.info("[inicia] TarefaRestController - concluiTarefa");
	    String usuario = getUsuarioByToken(token);
	    tarefaService.concluiTarefa(usuario, idTarefa);
		log.info("[finaliza] TarefaRestController - concluiTarefa");
	}
}
