package dev.erikmota.desafiounikamain.repository;

import dev.erikmota.desafiounikamain.models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    boolean existsByCep(String cep);
    List<Endereco> findByMonitoradorId(Long id);

}
