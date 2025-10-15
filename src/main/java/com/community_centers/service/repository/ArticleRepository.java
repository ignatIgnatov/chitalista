package com.community_centers.service.repository;

import com.community_centers.service.entity.Article;
import com.community_centers.service.entity.TemplateType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByPublishedTrueOrderByCreatedAtDesc();
    List<Article> findByTemplateAndPublishedTrue(TemplateType template);

    @Query(value = "SELECT * FROM articles a WHERE " +
            "(:template IS NULL OR a.template = :template) AND " +
            "(:searchTerm IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "a.content LIKE CONCAT('%', :searchTerm, '%')) AND " +
            "a.published = true",
            countQuery = "SELECT count(*) FROM articles a WHERE " +
                    "(:template IS NULL OR a.template = :template) AND " +
                    "(:searchTerm IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                    "a.content LIKE CONCAT('%', :searchTerm, '%')) AND " +
                    "a.published = true",
            nativeQuery = true)
    Page<Article> findByFilters(@Param("searchTerm") String searchTerm,
                                @Param("template") TemplateType template,
                                Pageable pageable);
}
