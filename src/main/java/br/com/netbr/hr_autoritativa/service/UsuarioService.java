package br.com.netbr.hr_autoritativa.service;

import br.com.netbr.hr_autoritativa.domain.Funcao;
import br.com.netbr.hr_autoritativa.domain.Usuario;
import br.com.netbr.hr_autoritativa.events.BeforeDeleteFuncao;
import br.com.netbr.hr_autoritativa.model.UsuarioDTO;
import br.com.netbr.hr_autoritativa.repos.FuncaoRepository;
import br.com.netbr.hr_autoritativa.repos.UsuarioRepository;
import br.com.netbr.hr_autoritativa.util.NotFoundException;
import java.util.HashSet;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FuncaoRepository funcaoRepository;

    public UsuarioService(final UsuarioRepository usuarioRepository,
            final FuncaoRepository funcaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.funcaoRepository = funcaoRepository;
    }

    public List<UsuarioDTO> findAll() {
        final List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("id"));
        return usuarios.stream()
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .toList();
    }

    public UsuarioDTO get(final Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UsuarioDTO usuarioDTO) {
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);
        return usuarioRepository.save(usuario).getId();
    }

    public void update(final Long id, final UsuarioDTO usuarioDTO) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(usuarioDTO, usuario);
        usuarioRepository.save(usuario);
    }

    public void delete(final Long id) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        usuarioRepository.delete(usuario);
    }

    private UsuarioDTO mapToDTO(final Usuario usuario, final UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setFuncao(usuario.getFuncao());
        usuarioDTO.setDataAdmissao(usuario.getDataAdmissao());
        usuarioDTO.setDataDemissao(usuario.getDataDemissao());
        usuarioDTO.setDataAtualizacao(usuario.getDataAtualizacao());
        usuarioDTO.setStatus(usuario.getStatus());
        usuarioDTO.setFuncoes(usuario.getFuncoes().stream()
                .map(funcao -> funcao.getId())
                .toList());
        return usuarioDTO;
    }

    private Usuario mapToEntity(final UsuarioDTO usuarioDTO, final Usuario usuario) {
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setFuncao(usuarioDTO.getFuncao());
        usuario.setDataAdmissao(usuarioDTO.getDataAdmissao());
        usuario.setDataDemissao(usuarioDTO.getDataDemissao());
        usuario.setDataAtualizacao(usuarioDTO.getDataAtualizacao());
        usuario.setStatus(usuarioDTO.getStatus());
        final List<Funcao> funcoes = funcaoRepository.findAllById(
                usuarioDTO.getFuncoes() == null ? List.of() : usuarioDTO.getFuncoes());
        if (funcoes.size() != (usuarioDTO.getFuncoes() == null ? 0 : usuarioDTO.getFuncoes().size())) {
            throw new NotFoundException("one of funcoes not found");
        }
        usuario.setFuncoes(new HashSet<>(funcoes));
        return usuario;
    }

    public boolean emailExists(final String email) {
        return usuarioRepository.existsByEmailIgnoreCase(email);
    }

    @EventListener(BeforeDeleteFuncao.class)
    public void on(final BeforeDeleteFuncao event) {
        // remove many-to-many relations at owning side
        usuarioRepository.findAllByFuncoesId(event.getId()).forEach(usuario ->
                usuario.getFuncoes().removeIf(funcao -> funcao.getId().equals(event.getId())));
    }

}
