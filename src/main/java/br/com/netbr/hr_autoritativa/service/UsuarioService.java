package br.com.netbr.hr_autoritativa.service;

import br.com.netbr.hr_autoritativa.domain.Usuario;
import br.com.netbr.hr_autoritativa.model.UsuarioDTO;
import br.com.netbr.hr_autoritativa.repos.UsuarioRepository;
import br.com.netbr.hr_autoritativa.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(final UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
//        usuarioDTO.setDataAdmissao(usuario.getDataAdmissao());
//        usuarioDTO.setDataDemissao(usuario.getDataDemissao());
//        usuarioDTO.setDataAtualizacao(usuario.getDataAtualizacao());
        usuarioDTO.setStatus(usuario.getStatus());

        return usuarioDTO;
    }

    private Usuario mapToEntity(final UsuarioDTO usuarioDTO, final Usuario usuario) {
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setFuncao(usuarioDTO.getFuncao());
//        usuario.setDataAdmissao(usuarioDTO.getDataAdmissao());
//        usuario.setDataDemissao(usuarioDTO.getDataDemissao());
//        usuario.setDataAtualizacao(usuarioDTO.getDataAtualizacao());
        usuario.setStatus(usuarioDTO.getStatus());

        return usuario;
    }


    public boolean emailExists(final String email) {
        return usuarioRepository.existsByEmailIgnoreCase(email);
    }



}