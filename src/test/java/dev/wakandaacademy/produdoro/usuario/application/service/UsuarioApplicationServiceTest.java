package dev.wakandaacademy.produdoro.usuario.application.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.credencial.application.service.CredencialService;
import dev.wakandaacademy.produdoro.pomodoro.application.service.PomodoroService;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;

@ContextConfiguration(classes = {UsuarioApplicationService.class })
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)

class UsuarioApplicationServiceTest {
	@MockBean
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioApplicationService usuarioApplicationService;
    @MockBean
    private CredencialService credencialService;
    @MockBean
    private PomodoroService pomodoroService;
    @MockBean
    private Usuario usuario;


    private static final UUID usuario1 = UUID.fromString("a713162f-20a9-4db9-a85b-90cd51ab18f4");

    @Test
    public void testMudaStatusParaFoco() {
        String usuarioEmail = "email@email.com";
        UUID idUsuario = UUID.fromString("a713162f-20a9-4db9-a85b-90cd51ab18f4");
        Usuario usuario = DataHelper.createUsuario();
        when(usuarioRepository.buscaUsuarioPorEmail(usuarioEmail)).thenReturn(usuario);
        usuarioApplicationService.mudaStatusParaFoco(usuarioEmail, idUsuario);
        verify(usuarioRepository,times(1)).salva(usuario);
    }

	@Test
	public void testMudaStatusParaPausaCurta() {
		String usuarioEmail = "email@email.com";
		UUID idUsuario = UUID.fromString("a713162f-20a9-4db9-a85b-90cd51ab18f4");
		Usuario usuario = DataHelper.createUsuario();
		when(usuarioRepository.buscaUsuarioPorEmail(usuarioEmail)).thenReturn(usuario);
		usuarioApplicationService.mudaStatusParaPausaCurta(usuarioEmail, idUsuario);
		verify(usuarioRepository, times(1)).salva(usuario);
	}

	@Test
	public void testMudaStatusParaPausaLonga() {
		String usuarioEmail = "email@email.com";
		UUID idUsuario = UUID.fromString("a713162f-20a9-4db9-a85b-90cd51ab18f4");
		Usuario usuario = DataHelper.createUsuario();
		when(usuarioRepository.buscaUsuarioPorEmail(usuarioEmail)).thenReturn(usuario);
		usuarioApplicationService.mudaStatusParaPausaLonga(usuarioEmail, idUsuario);
		verify(usuarioRepository, times(1)).salva(usuario);
	}
}