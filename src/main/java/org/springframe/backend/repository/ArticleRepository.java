package org.springframe.backend.repository;

import org.springframe.backend.domain.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByStatusOrderByCreateTimeDesc(Integer status, Pageable pageable);

    @Query("SELECT a FROM Article  a WHERE  a.status =:status AND a.id = :id")
    Article findPublicArticleById(@Param("status") Integer status, @Param("id") Integer id);
}
