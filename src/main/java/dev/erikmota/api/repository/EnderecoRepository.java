package dev.erikmota.api.repository;

import com.mysql.cj.util.StringUtils;
import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.models.Monitorador;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long>, JpaSpecificationExecutor<Endereco> {
    boolean existsByEnderecoAndNumero(String endereco, String numero);
    default List<Endereco> filtrar(String text, String estado, String cidade, Long monitorador_id) {
        return findAll((Specification<Endereco>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmptyOrWhitespaceOnly(text)){
                Predicate textPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("cep"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("endereco"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("bairro"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("cidade"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("estado"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("telefone"), "%" + text + "%"));

                predicates.add(textPredicate);
            }

            if (monitorador_id != null){
                Join<Endereco, Monitorador> monitoradorJoin = root.join("monitorador");
                predicates.add(criteriaBuilder.equal(monitoradorJoin.get("id"), monitorador_id));
            }

            if (!StringUtils.isEmptyOrWhitespaceOnly(estado))
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));

            if (!StringUtils.isEmptyOrWhitespaceOnly(cidade))
                predicates.add(criteriaBuilder.equal(root.get("cidade"), cidade));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
