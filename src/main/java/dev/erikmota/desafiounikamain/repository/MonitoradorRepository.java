package dev.erikmota.desafiounikamain.repository;

import dev.erikmota.desafiounikamain.models.Monitorador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoradorRepository extends JpaRepository<Monitorador, Long> {
    boolean existsByCpfOrCnpj(String cpf, String cnpj);
}
