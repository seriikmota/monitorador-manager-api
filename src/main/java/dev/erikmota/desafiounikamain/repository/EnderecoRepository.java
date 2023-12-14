package dev.erikmota.desafiounikamain.repository;

import dev.erikmota.desafiounikamain.models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    @Query("SELECT e FROM Endereco e JOIN FETCH e.monitorador")
    List<Endereco> findAllWithMonitorador();
    boolean existsByCep(String cep);
}
