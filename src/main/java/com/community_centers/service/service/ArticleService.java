package com.community_centers.service.service;

import com.community_centers.service.dto.ArticleDTO;
import com.community_centers.service.dto.ArticleImageDTO;
import com.community_centers.service.entity.Article;
import com.community_centers.service.entity.ArticleImage;
import com.community_centers.service.entity.TemplateType;
import com.community_centers.service.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
        return convertToDTO(article);
    }

    public Page<ArticleDTO> getArticlesWithFilters(String searchTerm, TemplateType template, Pageable pageable) {
        Page<Article> articles = articleRepository.findByFilters(searchTerm, template, pageable);
        return articles.map(this::convertToDTO);
    }

    public List<ArticleDTO> getArticlesByTemplate(TemplateType template) {
        return articleRepository.findByTemplateAndPublishedTrue(template)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ArticleDTO> getAllPublishedArticles() {
        return articleRepository.findByPublishedTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        Article article = convertToEntity(articleDTO);
        Article saved = articleRepository.save(article);
        return convertToDTO(saved);
    }

    private ArticleDTO convertToDTO(Article article) {
        List<ArticleImageDTO> images = article.getImages().stream().map(this::convertToImageDto).toList();
        return ArticleDTO.builder()
                .id(article.getId())
                .content(article.getContent())
                .title(article.getTitle())
                .images(images)
                .template(article.getTemplate())
                .createdAt(article.getCreatedAt())
                .published(article.getPublished())
                .build();
    }

    private Article convertToEntity(ArticleDTO dto) {
        List<ArticleImage> images = dto.getImages().stream().map(this::convertImageToEntity).toList();
        return Article.builder()
                .images(images)
                .title(dto.getTitle())
                .content(dto.getContent())
                .template(dto.getTemplate())
                .createdAt(LocalDateTime.now())
                .published(dto.getPublished())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private ArticleImageDTO convertToImageDto(ArticleImage image) {
        return ArticleImageDTO.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .altText(image.getAltText())
                .displayOrder(image.getDisplayOrder())
                .build();
    }

    private ArticleImage convertImageToEntity(ArticleImageDTO dto) {
        return ArticleImage.builder()
                .imageUrl(dto.getImageUrl())
                .altText(dto.getAltText())
                .displayOrder(dto.getDisplayOrder())
                .build();
    }
}
