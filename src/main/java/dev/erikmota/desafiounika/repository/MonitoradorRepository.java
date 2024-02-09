package dev.erikmota.desafiounika.repository;

import dev.erikmota.desafiounika.models.Monitorador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoradorRepository extends JpaRepository<Monitorador, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);
    Monitorador findByCpf(String cpf);
    Monitorador findByCnpj(String cnpj);
}
