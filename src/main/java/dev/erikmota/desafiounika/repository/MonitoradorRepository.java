package dev.erikmota.desafiounika.repository;

import com.mysql.cj.util.StringUtils;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

public interface MonitoradorRepository extends JpaRepository<Monitorador, Long>, JpaSpecificationExecutor<Monitorador> {
    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);
    Monitorador findByCpf(String cpf);
    Monitorador findByCnpj(String cnpj);

    default List<Monitorador> filtrar(String text, Boolean ativo, TipoPessoa tipo) {
        return findAll((Specification<Monitorador>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmptyOrWhitespaceOnly(text)){
                Predicate textPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("nome"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("razao"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("cpf"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("cnpj"), "%" + text + "%"));

                predicates.add(textPredicate);
            }

            if (ativo != null)
                predicates.add(criteriaBuilder.equal(root.get("ativo"), ativo));

            if (tipo != null)
                predicates.add(criteriaBuilder.equal(root.get("tipo"), tipo));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
