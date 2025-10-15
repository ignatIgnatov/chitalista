package com.community_centers.service.controller;

import com.community_centers.service.entity.Article;
import com.community_centers.service.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SitemapController {

    private final ArticleRepository articleRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @GetMapping("/sitemap.xml")
    public ResponseEntity<String> generateSitemap() {
        try {
            List<Article> articles = articleRepository.findByPublishedTrueOrderByCreatedAtDesc();
            String sitemap = generateSitemapXML(articles);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);

            return new ResponseEntity<>(sitemap, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String generateSitemapXML(List<Article> articles) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Главна страница
        xml.append("  <url>\n");
        xml.append("    <loc>").append(baseUrl).append("</loc>\n");
        xml.append("    <changefreq>daily</changefreq>\n");
        xml.append("    <priority>1.0</priority>\n");
        xml.append("  </url>\n");

        // Страница със статии
        xml.append("  <url>\n");
        xml.append("    <loc>").append(baseUrl).append("/articles</loc>\n");
        xml.append("    <changefreq>weekly</changefreq>\n");
        xml.append("    <priority>0.8</priority>\n");
        xml.append("  </url>\n");

        // Всички статии
        for (Article article : articles) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(baseUrl).append("/articles/").append(article.getId()).append("</loc>\n");
            xml.append("    <lastmod>").append(article.getUpdatedAt().format(formatter)).append("</lastmod>\n");
            xml.append("    <changefreq>monthly</changefreq>\n");
            xml.append("    <priority>0.6</priority>\n");
            xml.append("  </url>\n");
        }

        xml.append("</urlset>");
        return xml.toString();
    }
}
