package dev.wakandaacademy.produdoro.usuario.application.service;

class UsuarioApplicationServiceTest {
//	@Mock
//	private UsuarioRepository usuarioRepository;
//
//	@InjectMocks
//	private UsuarioApplicationService usuarioApplicationService;
//
//	private Usuario usuario;
//
//	@BeforeEach
//	public void setUp() {
//	    MockitoAnnotations.initMocks(this);
//	    usuario = DataHelper.createUsuario();
//	    when(usuarioRepository.buscaUsuarioPorEmail(anyString())).thenReturn(usuario);
//	}
//
//	@Test
//	void mudaStatusParaFocoTest() {
//	    //chama o metodo.
//	    usuarioApplicationService.mudaStatusParaFoco("teste12@gmail.com", usuario.getIdUsuario());
//
//	    //verifica se os metodos foram chamados uma vez cada um.
//	    verify(usuarioRepository, times(1)).buscaUsuarioPorEmail("teste12@gmail.com");
//	    verify(usuarioRepository, times(1)).salva(usuario);
//
//	    //verifica se status foi alterado para foco.
//	    assertEquals(StatusUsuario.FOCO, usuario.getStatus());
//	}
//
//	@Test
//	void naoMudaStatusParaFocoTest() {
//	    // cria um id não autorizado
//	    UUID idUsuario = UUID.randomUUID();
//
//	    //Chama o método e lança a APIException
//	    APIException exception = assertThrows(APIException.class, () -> {
//	    usuarioApplicationService.mudaStatusParaFoco("teste12@gmail.com", idUsuario);});
//
//	    //verifica se a mensagem e o Retorno estão corretos.
//	    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
//	    assertEquals("Usuário não autorizado!", exception.getMessage());
//
//	    // Verifica que o método salva não foi chamado após a exceção
//	    verify(usuarioRepository, never()).salva(any(Usuario.class));
//	}
}