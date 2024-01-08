package dev.erikmota.desafiounikamain.repository;

import com.mysql.cj.util.StringUtils;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

public interface MonitoradorRepository extends JpaRepository<Monitorador, Long>, JpaSpecificationExecutor<Monitorador> {
    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);
    boolean existsByNome(String nome);
    boolean existsByRazaoSocial(String razaoSocial);
    List<Monitorador> findByCpfContains(String cpf);
    List<Monitorador> findByCnpjContains(String cnpj);
    List<Monitorador> findByNomeContains(String nome);
    List<Monitorador> findByAtivo(boolean ativo);
    List<Monitorador> findByTipoPessoa(TipoPessoa tipoPessoa);

    default List<Monitorador> filtrar(String text, Boolean ativo, TipoPessoa tipoPessoa) {
        return findAll((Specification<Monitorador>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmptyOrWhitespaceOnly(text)){
                Predicate textPredicate = criteriaBuilder.or(
                criteriaBuilder.like(root.get("nome"), "%" + text + "%"),
                criteriaBuilder.like(root.get("razaoSocial"), "%" + text + "%"),
                criteriaBuilder.like(root.get("cpf"), "%" + text + "%"),
                criteriaBuilder.like(root.get("cnpj"), "%" + text + "%"));

                predicates.add(textPredicate);
            }

            if (ativo != null)
                predicates.add(criteriaBuilder.equal(root.get("ativo"), ativo));

            if (tipoPessoa != null)
                predicates.add(criteriaBuilder.equal(root.get("tipoPessoa"), tipoPessoa));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
