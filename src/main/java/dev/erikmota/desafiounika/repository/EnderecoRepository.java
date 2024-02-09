package dev.erikmota.desafiounika.repository;

import dev.erikmota.desafiounika.models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    boolean existsByEnderecoAndNumero(String endereco, String numero);
}
