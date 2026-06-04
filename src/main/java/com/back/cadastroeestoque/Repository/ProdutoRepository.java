package com.back.cadastroeestoque.Repository;

import com.back.cadastroeestoque.Model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>,
        JpaSpecificationExecutor<Produto> {

            boolean existsByNomeIgnoreCase(String nome);
}
