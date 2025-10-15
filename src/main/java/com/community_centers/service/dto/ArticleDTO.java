package com.community_centers.service.dto;


import com.community_centers.service.entity.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private TemplateType template;
    private List<ArticleImageDTO> images;
    private LocalDateTime createdAt;
    private Boolean published;

}
