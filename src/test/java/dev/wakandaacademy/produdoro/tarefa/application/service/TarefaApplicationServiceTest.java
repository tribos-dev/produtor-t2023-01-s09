package dev.wakandaacademy.produdoro.tarefa.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Assertions;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.tarefa.application.api.EditaTarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.StatusAtivacaoTarefa;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;
import org.junit.jupiter.api.DisplayName;

@ExtendWith(MockitoExtension.class)
class TarefaApplicationServiceTest {
	
    //	@Autowired
    @InjectMocks
    TarefaApplicationService tarefaApplicationService;
    
    @Mock
    UsuarioRepository usuarioRepository;
    
    //	@MockBean
    @Mock
    TarefaRepository tarefaRepository;
    
    @InjectMocks
    DataHelper dataHelper;
    
    Tarefa tarefa;
    Usuario usuario;
    UUID idTarefa;
    UUID idUsuario;
    String email;
    
    @BeforeEach
    void setup() {
        tarefa = DataHelper.createTarefa();
        usuario = DataHelper.createUsuario();
        idTarefa = tarefa.getIdTarefa();
        idUsuario = usuario.getIdUsuario();
        email = usuario.getEmail();
        when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(any(UUID.class))).thenReturn(Optional.of(tarefa));
    }

    @Test
    void deveRetornarIdTarefaNovaCriada() {
        TarefaRequest request = getTarefaRequest();
        when(tarefaRepository.salva(any())).thenReturn(new Tarefa(request));

        TarefaIdResponse response = tarefaApplicationService.criaNovaTarefa(request);

        assertNotNull(response);
        assertEquals(TarefaIdResponse.class, response.getClass());
        assertEquals(UUID.class, response.getIdTarefa().getClass());
    }

    public TarefaRequest getTarefaRequest() {
        TarefaRequest request = new TarefaRequest("tarefa 1", UUID.randomUUID(), null, null, 0);
        return request;
    }
    
    @Test
    void deveRetornarListaTarefasPorUsuario() {
    	Usuario usuario = DataHelper.createUsuario();
    	List<Tarefa> tarefas = DataHelper.createListTarefa();
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(DataHelper.createUsuario());
        when(tarefaRepository.buscaTarefasPorUsuario(any(UUID.class))).thenReturn(tarefas);
        List<TarefaDetalhadoResponse> response = tarefaApplicationService.buscarTarefasPorUsuario(usuario.getEmail(), usuario.getIdUsuario());
        assertEquals(8, response.size());
        assertNotNull(response);
    }
    
    @Test
    void deveRetornarExceptionAoListarTarefasPorUsuario() {
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(DataHelper.createUsuario());
        
        APIException ex = Assertions.assertThrows(APIException.class, () -> tarefaApplicationService.buscarTarefasPorUsuario("invalido@email.com", UUID.randomUUID()));
        assertEquals("Usuário não autorizado", ex.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusException());
    }
    
    void testeDeletaTarefa() {
    	UUID idTarefa = UUID.randomUUID();
    	String usuario = "exemplo@usuario.com";
    	Usuario usuarioMock = DataHelper.createUsuario();
    	Tarefa tarefaMock = DataHelper.createTarefa();
    	when (usuarioRepository.buscaUsuarioPorEmail(usuario)).thenReturn(usuarioMock);
    	when (tarefaRepository.buscaTarefaPorId(idTarefa)).thenReturn(Optional.of(tarefaMock));
    	tarefaApplicationService.deletaTarefa(idTarefa, usuario);
     	verify(tarefaRepository, times(1)).deletaTarefaPorId(tarefaMock);
    }

    @Test
    void deveIncrementaUmPomodoroAUmaTarefa() {
    	Usuario usuario = DataHelper.createUsuario();
    	Tarefa tarefa = DataHelper.createTarefa();
    	when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.of(tarefa));
    	tarefaApplicationService.incrementaPomodoro(tarefa.getIdTarefa(), usuario.getEmail());
    	verify(usuarioRepository, times(1)).buscaUsuarioPorEmail(usuario.getEmail());
        verify(tarefaRepository, times(1)).buscaTarefaPorId(tarefa.getIdTarefa());
    	assertEquals(2, tarefa.getContagemPomodoro());
    }
    
    @Test
    @DisplayName("Teste Incrementa Pomodoro - Usuario Não é dono da tarefa")
    void incrementaPomodoro_Usuario_Nao_E_Dono_Da_Tarefa() {
    	//DADO 
    	Tarefa tarefa = DataHelper.createTarefa();
    	UUID idTarefa = tarefa.getIdTarefa();
    	Usuario usuario2 = DataHelper.createUsuario();
    	String usuarioEmail = usuario2.getEmail();
    	//QUANDO
    	when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario2);
    	when(tarefaRepository.buscaTarefaPorId(any(UUID.class))).thenReturn(Optional.of(tarefa));
    	APIException ex = assertThrows(APIException.class, () -> {
    	tarefaApplicationService.incrementaPomodoro(idTarefa, usuarioEmail);
    	});
    	assertEquals("Usuário não é dono da Tarefa solicitada!", ex.getMessage());
    }

    @Test
        void deveAtivarTarefaDoUsuario() {
    	Usuario usuario = DataHelper.createUsuario();
    	Tarefa tarefa = DataHelper.createTarefa();
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        when(tarefaRepository.buscaTarefaPorId(any())).thenReturn(Optional.of(tarefa));
        tarefaApplicationService.ativaTarefa(tarefa.getIdTarefa(), usuario.getEmail());
        verify(usuarioRepository, times(1)).buscaUsuarioPorEmail(usuario.getEmail());
        verify(tarefaRepository, times(1)).buscaTarefaPorId(tarefa.getIdTarefa());
        assertEquals(StatusAtivacaoTarefa.ATIVA, tarefa.getStatusAtivacao());
    }
    
    @Test
    void deveRetornarErroAoAtivarTarefa() {
    	Usuario usuario = DataHelper.createUsuario();
        when(usuarioRepository.buscaUsuarioPorEmail(any())).thenReturn(usuario);
        APIException ex = Assertions.assertThrows(APIException.class, () -> tarefaApplicationService
        		.ativaTarefa(UUID.randomUUID(), usuario.getEmail()));
        assertEquals("id da tarefa invalido", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusException());
    }
           
    @Test
    void testEditaTarefa() {
        EditaTarefaRequest request = dataHelper.createEditaTarefaRequest();
        tarefaApplicationService.editaTarefa(email, idTarefa, request);
        verify(tarefaRepository, times(1)).salva(tarefa);
        
    }
    
    @Test
        public void testConcluiTarefa() {
    	String usuario = "email@email.com";
    	UUID idTarefa = UUID.fromString("06fb5521-9d5a-461a-82fb-e67e3bedc6eb");
    	Usuario usuarioMock = mock(Usuario.class);
    	Tarefa tarefaMock = mock(Tarefa.class);
    	when(usuarioRepository.buscaUsuarioPorEmail(usuario)).thenReturn(usuarioMock);
        when(tarefaRepository.buscaTarefaPorId(idTarefa)).thenReturn(Optional.of(tarefaMock));
        tarefaApplicationService.concluiTarefa(usuario, idTarefa);
        verify(usuarioRepository).buscaUsuarioPorEmail(usuario);
        verify(tarefaRepository).buscaTarefaPorId(idTarefa);
        verify(tarefaMock).pertenceAoUsuario(usuarioMock);
        verify(tarefaMock).concluiTarefa();
        verify(tarefaRepository).salva(tarefaMock);
        }
}
