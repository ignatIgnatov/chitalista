package com.community_centers.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Chitalishte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String chitalishtaUrl;

    private String status; //Действащо/Закрито

    @Column(unique = true)
    private String bulstat;

    private String chairman;

    private String secretary;

    private String phone;

    private String email;

    private String region;

    private String municipality;

    private String town;

    private String address;

    private String urlToLibrariesSite;

    private int registrationNumber;
}