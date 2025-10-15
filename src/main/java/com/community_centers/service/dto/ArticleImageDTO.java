package com.community_centers.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleImageDTO {
    private Long id;
    private String imageUrl;
    private String altText;
    private Integer displayOrder;
}
