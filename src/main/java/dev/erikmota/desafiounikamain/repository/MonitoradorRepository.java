package dev.erikmota.desafiounikamain.repository;

import dev.erikmota.desafiounikamain.models.Monitorador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonitoradorRepository extends JpaRepository<Monitorador, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);
    boolean existsByNome(String nome);
    boolean existsByRazaoSocial(String razaoSocial);
}
