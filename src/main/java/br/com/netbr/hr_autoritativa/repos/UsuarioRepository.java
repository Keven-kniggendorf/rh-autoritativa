package br.com.netbr.hr_autoritativa.repos;

import br.com.netbr.hr_autoritativa.domain.Usuario;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByEmailIgnoreCase(String email);

}
