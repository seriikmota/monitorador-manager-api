package dev.erikmota.desafiounikamain.repository;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    boolean existsByCep(String cep);
    List<Endereco> findByMonitoradorId(Long id);
}
