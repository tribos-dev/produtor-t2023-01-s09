package dev.wakandaacademy.produdoro.usuario.application.service;

import java.util.UUID;
import dev.wakandaacademy.produdoro.usuario.application.api.UsuarioCriadoResponse;
import dev.wakandaacademy.produdoro.usuario.application.api.UsuarioNovoRequest;

public interface UsuarioService {
	UsuarioCriadoResponse criaNovoUsuario(UsuarioNovoRequest usuarioNovo);
	void mudaStatusParaPausaLonga(String usuario, UUID idUsuario);
    UsuarioCriadoResponse buscaUsuarioPorId(UUID idUsuario);
	void mudaStatusParaFoco(String usuarioEmail, UUID idUsuario);
	void mudaStatusParaPausaCurta(String usuario, UUID idUsuario);
}
