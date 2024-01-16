package dev.erikmota.desafiounikamain.repository;

import com.mysql.cj.util.StringUtils;
import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long>, JpaSpecificationExecutor<Endereco> {

    boolean existsByCep(String cep);
    default List<Endereco> filtrarE(String text, String cidade, String estado, Long monitorador) {
        return findAll((Specification<Endereco>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmptyOrWhitespaceOnly(text)){
                Predicate textPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("cep"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("endereco"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("cidade"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("estado"), "%" + text + "%"),
                        criteriaBuilder.like(root.get("bairro"), "%" + text + "%"));

                predicates.add(textPredicate);
            }

            if (monitorador != null){
                Join<Endereco, Monitorador> monitoradorJoin = root.join("monitorador");
                predicates.add(criteriaBuilder.equal(monitoradorJoin.get("id"), monitorador));
            }

            if (cidade != null)
                predicates.add(criteriaBuilder.equal(root.get("cidade"), cidade));

            if (estado != null)
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
