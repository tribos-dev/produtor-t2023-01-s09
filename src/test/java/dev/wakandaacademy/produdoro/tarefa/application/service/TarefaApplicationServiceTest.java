package dev.wakandaacademy.produdoro.tarefa.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import dev.wakandaacademy.produdoro.DataHelper;
import dev.wakandaacademy.produdoro.handler.APIException;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaDetalhadoResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaIdResponse;
import dev.wakandaacademy.produdoro.tarefa.application.api.TarefaRequest;
import dev.wakandaacademy.produdoro.tarefa.application.repository.TarefaRepository;
import dev.wakandaacademy.produdoro.tarefa.domain.Tarefa;
import dev.wakandaacademy.produdoro.usuario.application.repository.UsuarioRepository;
import dev.wakandaacademy.produdoro.usuario.domain.Usuario;

@ExtendWith(MockitoExtension.class)
class TarefaApplicationServiceTest {

    //	@Autowired
    @InjectMocks
    TarefaApplicationService tarefaApplicationService;

    //	@MockBean
    @Mock
    TarefaRepository tarefaRepository;
    
    @Mock
    UsuarioRepository usuarioRepository;

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
        assertEquals("Usuário não encontrado", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusException());
    }
    
    
    
    
    
    
    
}
