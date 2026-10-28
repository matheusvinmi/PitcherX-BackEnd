package com.pitcherx.service;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.pitcherx.model.ProjetoImagem;
import com.pitcherx.model.ProjetoUsuario;
import com.pitcherx.model.ProjetoVideo;
import com.pitcherx.model.Usuario;
import com.pitcherx.specs.ProjetoSpecification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pitcherx.dto.projeto.ProjetoRequestDTO;
import com.pitcherx.dto.projeto.ProjetoResponseDTO;
import com.pitcherx.mapper.ProjetoMapper;
import com.pitcherx.model.Projeto;
import com.pitcherx.model.RiscoProjeto;
import com.pitcherx.model.TipoProjeto;
import com.pitcherx.model.TipoVinculo;
import com.pitcherx.model.TipoVinculoEnum;
import com.pitcherx.repository.ProjetoRepository;
import com.pitcherx.repository.ProjetoUsuarioRepository;
import com.pitcherx.repository.RiscoProjetoRepository;
import com.pitcherx.repository.TipoProjetoRepository;
import com.pitcherx.repository.TipoVinculoRepository;
import com.pitcherx.repository.UsuarioRepository;
import com.pitcherx.service.UsuarioService;
import com.pitcherx.utils.UploadUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProjetoService {
	
private final ProjetoRepository projetoRepository;
    private final ProjetoMapper projetoMapper;
    private final TipoProjetoRepository tipoProjetoRepository;
    private final RiscoProjetoRepository riscoProjetoRepository;
    private final UploadUtil uploadUtil;
    private final ProjetoUsuarioRepository projetoUsuarioRepository;
    private final TipoVinculoRepository tipoVinculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public ProjetoService(ProjetoRepository projetoRepository, ProjetoMapper projetoMapper,
            TipoProjetoRepository tipoProjetoRepository, RiscoProjetoRepository riscoProjetoRepository,
            UploadUtil uploadUtil, ProjetoUsuarioRepository projetoUsuarioRepository,
            TipoVinculoRepository tipoVinculoRepository, UsuarioRepository usuarioRepository,
            UsuarioService usuarioService) {
        this.projetoRepository = projetoRepository;
        this.projetoMapper = projetoMapper;
        this.tipoProjetoRepository = tipoProjetoRepository;
        this.riscoProjetoRepository = riscoProjetoRepository;
        this.uploadUtil = uploadUtil;
        this.projetoUsuarioRepository = projetoUsuarioRepository;
        this.tipoVinculoRepository = tipoVinculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }
	
	@Transactional(readOnly = true)
	public List<ProjetoResponseDTO> listarProjetos(){
		return projetoRepository.findAll().stream()
				.map(projetoMapper::toDTO)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public ProjetoResponseDTO buscarProjetoPorId(Long idProjeto, Long usuarioId) {
		Projeto projeto = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
		
		// Verificar se o usuário tem acesso ao projeto
		if (usuarioId != null) {
			validarAcessoProjeto(projeto, usuarioId);
		}
		
		return projetoMapper.toDTO(projeto);
	}

    private void validarAcessoProjeto(Projeto projeto, Long usuarioId) {
        // Buscar vínculo do usuário com este projeto
        List<ProjetoUsuario> pusu = projeto.getUsuarios().stream()
                .filter(pu -> pu.getUsuario().getIdUsuario().equals(usuarioId))
                .collect(java.util.stream.Collectors.toList());
        
        if (pusu.isEmpty()) {
            throw new SecurityException("Você não tem permissão para visualizar este projeto. É necessário aceitar os termos de vinculo.");
        }
        
        ProjetoUsuario projUser = pusu.get(0);
        
        // Usuário tem acesso se aceitou termos OR tem vínculo VISUALIZADOR ou superior
        boolean podeAcessar = projUser.getAceitouTermos() != null && projUser.getAceitouTermos()
                || projUser.getTipoVinculo().getNomeTipoVinculo().equals("VISUALIZADOR")
                || projUser.getTipoVinculo().getNomeTipoVinculo().equals("SOCIO")
                || projUser.getTipoVinculo().getNomeTipoVinculo().equals("INVESTIDOR")
                || projUser.getTipoVinculo().getNomeTipoVinculo().equals("CRIADOR");
        
        if (!podeAcessar) {
            throw new SecurityException("Você não tem permissão para visualizar este projeto. É necessário aceitar os termos de vinculo.");
        }
    }

	@Transactional(readOnly = true)
	public List<ProjetoResponseDTO> buscarProjetos(String nome, String descricao, LocalDate dataInicioDe, LocalDate dataInicioAte) {
		Specification<Projeto> spec = Specification
				.where(ProjetoSpecification.comNome(nome))
				.and(ProjetoSpecification.comDescricao(descricao))
				.and(ProjetoSpecification.comDataInicioApartirDe(dataInicioDe))
				.and(ProjetoSpecification.comDataInicioEntre(dataInicioDe, dataInicioAte));

		return projetoRepository.findAll(spec).stream()
				.map(projetoMapper::toDTO)
				.toList();
	}
	
@Transactional
    public ProjetoResponseDTO criarProjeto(Long idUsuario, ProjetoRequestDTO projetoRequestDTO) {
        TipoProjeto tipoProjeto = tipoProjetoRepository.findById(projetoRequestDTO.tipoProjetoId())
                .orElseThrow(() -> new EntityNotFoundException("Sem tipo de projeto com o ID informado!"));
        
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Sem usuário com o ID informado!"));
        
        Projeto projeto = projetoMapper.toEntity(projetoRequestDTO);
        projeto.setTipoProjeto(tipoProjeto);
        projeto.setRiscos(buscarRiscos(projetoRequestDTO.riscoProjetoIds()));
        projeto.setUsuarioCriador(usuario);
       
        Projeto salvo = projetoRepository.save(projeto);

        TipoVinculo tipoCriador = tipoVinculoRepository.findByNomeTipoVinculo(TipoVinculoEnum.CRIADOR.toString())
                .orElseThrow(() -> new EntityNotFoundException("Sem tipo de vínculo CRIADOR!"));
       
        ProjetoUsuario projetoUsuario = new ProjetoUsuario();
        projetoUsuario.setProjeto(salvo);
        projetoUsuario.setUsuario(usuario);
        projetoUsuario.setTipoVinculo(tipoCriador);
        projetoUsuario.setAceitouTermos(false);
        projetoUsuario.setDataAceite(null);
       
        projetoUsuarioRepository.save(projetoUsuario);
       
        return projetoMapper.toDTO(salvo);
    }
	
@Transactional
    public ProjetoResponseDTO atualizarProjeto(Long idProjeto, ProjetoRequestDTO projetoRequestDTO) {
        Projeto projeto = projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
        
        TipoProjeto tipoProjeto = tipoProjetoRepository.findById(projetoRequestDTO.tipoProjetoId())
                .orElseThrow(() -> new EntityNotFoundException("Sem tipo de projeto com o ID informado!"));
        
        projetoMapper.toUpdate(projetoRequestDTO, projeto);
        projeto.setTipoProjeto(tipoProjeto);
        projeto.setRiscos(buscarRiscos(projetoRequestDTO.riscoProjetoIds()));
        
        Projeto salvo = projetoRepository.save(projeto);
        return projetoMapper.toDTO(salvo);
    }
    
    @Transactional
    public Double atualizarMetaFinanceira(Long idProjeto, Double novaMeta) {
        Projeto projeto = projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
        
        projeto.setMetaFinanceira(novaMeta);
        projetoRepository.save(projeto);
        return projeto.getMetaFinanceira();
    }
    
    @Transactional
    public Double atualizarValorArrecadado(Long idProjeto, Double novoValor) {
        Projeto projeto = projetoRepository.findById(idProjeto)
                .orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
        
        projeto.setValorArrecadado(novoValor);
        projetoRepository.save(projeto);
        return projeto.getValorArrecadado();
    }
    
    @Transactional
    public void deletarProjeto(Long idProjeto) {
		Projeto projeto = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
		try {
			List<String> urlsAntigasImagens = obterUrlsImagem(projeto);
			List<String> urlsAntigasVideos = obterUrlsVideo(projeto);
			projetoRepository.delete(projeto);
			uploadUtil.limparAposTransacaoImagens(urlsAntigasImagens, List.of());
			uploadUtil.limparAposTransacaoVideos(urlsAntigasVideos, List.of());
		} catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Não é possível deletar este projeto, pois ele está associado a outras entidades.");
		}
	}

	@Transactional
	public ProjetoResponseDTO substituirImagens(Long idProjeto, Usuario usuarioLogado, List<MultipartFile> arquivos) {
		Projeto projeto = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
		validarAcessoImagens(projeto, usuarioLogado);

		List<String> urlsNovas = uploadUtil.salvarImagens(arquivos);
		List<String> urlsAntigas = obterUrlsImagem(projeto);
		uploadUtil.limparAposTransacaoImagens(urlsAntigas, urlsNovas);
		projeto.getImagens().clear();
		projetoRepository.flush();
		atualizarImagens(projeto, urlsNovas);
		return projetoMapper.toDTO(projetoRepository.save(projeto));
	}

	@Transactional
	public void removerImagens(Long idProjeto, Usuario usuarioLogado) {
		Projeto projeto = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
		validarAcessoImagens(projeto, usuarioLogado);

		List<String> urlsAntigas = obterUrlsImagem(projeto);
		uploadUtil.limparAposTransacaoImagens(urlsAntigas, List.of());
		projeto.getImagens().clear();
		projeto.setUrlImagemProjeto(null);
		projetoRepository.save(projeto);
	}

	@Transactional
	public ProjetoResponseDTO substituirVideos(Long idProjeto, Usuario usuarioLogado, List<MultipartFile> arquivos) {
		Projeto projeto = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
		validarAcessoImagens(projeto, usuarioLogado);

		List<String> urlsNovas = uploadUtil.salvarVideos(arquivos);
		List<String> urlsAntigas = obterUrlsVideo(projeto);
		uploadUtil.limparAposTransacaoVideos(urlsAntigas, urlsNovas);
		projeto.getVideos().clear();
		projetoRepository.flush();
		atualizarVideos(projeto, urlsNovas);
		return projetoMapper.toDTO(projetoRepository.save(projeto));
	}

	@Transactional
	public void removerVideos(Long idProjeto, Usuario usuarioLogado) {
		Projeto projeto = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
		validarAcessoImagens(projeto, usuarioLogado);

		List<String> urlsAntigas = obterUrlsVideo(projeto);
		uploadUtil.limparAposTransacaoVideos(urlsAntigas, List.of());
		projeto.getVideos().clear();
		projeto.setUrlVideoProjeto(null);
		projetoRepository.save(projeto);
	}

	private void validarAcessoImagens(Projeto projeto, Usuario usuarioLogado) {
		boolean administrador = usuarioLogado.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch("ROLE_ADMIN"::equals);
		boolean vinculadoAoProjeto = projeto.getUsuarios().stream()
				.map(ProjetoUsuario::getUsuario)
				.anyMatch(usuario -> usuario.getIdUsuario().equals(usuarioLogado.getIdUsuario()));
		if (!administrador && !vinculadoAoProjeto) {
			throw new SecurityException("Você não tem permissão para alterar as imagens deste projeto.");
		}
	}

	private List<String> obterUrlsImagem(Projeto projeto) {
		List<String> urls = projeto.getImagens().stream()
				.map(ProjetoImagem::getUrlImagem)
				.collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
		if (projeto.getUrlImagemProjeto() != null && !urls.contains(projeto.getUrlImagemProjeto())) {
			urls.add(projeto.getUrlImagemProjeto());
		}
		return urls;
	}

	private void atualizarImagens(Projeto projeto, List<String> urls) {
		for (int i = 0; i < urls.size(); i++) {
			ProjetoImagem imagem = new ProjetoImagem();
			imagem.setProjeto(projeto);
			imagem.setUrlImagem(urls.get(i));
			imagem.setOrdem(i);
			projeto.getImagens().add(imagem);
		}
		projeto.setUrlImagemProjeto(urls.get(0));
	}

	private List<String> obterUrlsVideo(Projeto projeto) {
		List<String> urls = projeto.getVideos().stream()
				.map(ProjetoVideo::getUrlVideo)
				.collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
		if (projeto.getUrlVideoProjeto() != null && !urls.contains(projeto.getUrlVideoProjeto())) {
			urls.add(projeto.getUrlVideoProjeto());
		}
		return urls;
	}

	private void atualizarVideos(Projeto projeto, List<String> urls) {
		for (int i = 0; i < urls.size(); i++) {
			ProjetoVideo video = new ProjetoVideo();
			video.setProjeto(projeto);
			video.setUrlVideo(urls.get(i));
			video.setOrdem(i);
			projeto.getVideos().add(video);
		}
		projeto.setUrlVideoProjeto(urls.get(0));
	}

	private Set<RiscoProjeto> buscarRiscos(List<Long> idsRiscos) {
		if (idsRiscos == null) {
			throw new IllegalArgumentException("A lista de riscos do projeto é obrigatória.");
		}
		if (idsRiscos.stream().distinct().count() != idsRiscos.size()) {
			throw new IllegalArgumentException("A lista de riscos não pode conter IDs duplicados.");
		}
		List<RiscoProjeto> riscos = riscoProjetoRepository.findAllById(idsRiscos);
		if (riscos.size() != idsRiscos.size()) {
			throw new EntityNotFoundException("Um ou mais riscos informados não foram encontrados.");
		}
		return riscos.stream().collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
	}

	@Transactional
	public void visualizarProjeto(Long idProjeto, Long usuarioId) {
		Projeto projeto = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
		projeto.setVisualizacoes((projeto.getVisualizacoes() == null ? 0 : projeto.getVisualizacoes()) + 1);
		projetoRepository.save(projeto);
		if (projeto.getUsuarioCriador() != null) {
			usuarioService.recalcularNivel(projeto.getUsuarioCriador());
		}
	}

	@Transactional
	public void seguirProjeto(Long idProjeto, Long usuarioId) {
		Projeto projeto = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
		projeto.setSeguidores((projeto.getSeguidores() == null ? 0 : projeto.getSeguidores()) + 1);
		projetoRepository.save(projeto);
		if (projeto.getUsuarioCriador() != null) {
			usuarioService.recalcularNivel(projeto.getUsuarioCriador());
		}
	}

}
