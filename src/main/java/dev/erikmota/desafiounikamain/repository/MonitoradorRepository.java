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

    /*@Query("SELECT m FROM Monitorador m WHERE (:nome IS NULL OR m.nome LIKE CONCAT('%', :nome, '%')) AND (:ativo IS NULL OR m.ativo = :ativo)")
    List<Monitorador> filtrar(String nome, Boolean ativo);*/


    //List<Monitorador> filtrar(String nome, String cpf, String cnpj, Boolean ativo, TipoPessoa tipoPessoa);

    default List<Monitorador> filtrar(String nome, String cpf, String cnpj, Boolean ativo, TipoPessoa tipoPessoa) {
        return findAll((Specification<Monitorador>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmptyOrWhitespaceOnly(nome)) {
                predicates.add(criteriaBuilder.like(root.get("nome"), "%" + nome + "%"));
                System.out.println("Tem nome: " + nome);
            }

            if (!StringUtils.isEmptyOrWhitespaceOnly(cpf)) {
                predicates.add(criteriaBuilder.like(root.get("cpf"), "%" + cpf + "%"));
                System.out.println("Tem cpf: " + cpf);
            }

            if (!StringUtils.isEmptyOrWhitespaceOnly(cnpj)) {
                predicates.add(criteriaBuilder.like(root.get("cnpj"), "%" + cnpj + "%"));
                System.out.println("Tem cnpj: " + cnpj);
            }

            if (ativo != null) {
                predicates.add(criteriaBuilder.equal(root.get("ativo"), ativo));
                System.out.println("Tem ativo: " + ativo);
            }

            if (tipoPessoa != null) {
                predicates.add(criteriaBuilder.equal(root.get("tipoPessoa"), tipoPessoa));
                System.out.println("Tem tipo: " + tipoPessoa);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
