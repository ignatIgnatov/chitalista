package com.community_centers.service.controller;

import com.community_centers.service.dto.ArticleDTO;
import com.community_centers.service.entity.TemplateType;
import com.community_centers.service.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        ArticleDTO article = articleService.getArticleById(id);
        return ResponseEntity.ok(article);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ArticleDTO>> searchArticles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TemplateType template,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleDTO> articles = articleService.getArticlesWithFilters(search, template, pageable);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/template/{template}")
    public ResponseEntity<List<ArticleDTO>> getArticlesByTemplate(@PathVariable TemplateType template) {
        return ResponseEntity.ok(articleService.getArticlesByTemplate(template));
    }

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllPublishedArticles());
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
        return ResponseEntity.ok(articleService.createArticle(articleDTO));
    }
}
