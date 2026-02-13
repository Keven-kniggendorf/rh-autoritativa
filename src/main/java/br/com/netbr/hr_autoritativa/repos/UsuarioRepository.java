package br.com.netbr.hr_autoritativa.repos;

import br.com.netbr.hr_autoritativa.domain.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findAllByFuncoesId(Long id);

    boolean existsByEmailIgnoreCase(String email);

}
