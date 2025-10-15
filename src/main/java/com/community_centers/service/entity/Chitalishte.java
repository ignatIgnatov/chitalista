package com.community_centers.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chitalishte")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "informationCards")
public class Chitalishte {

    @Id
    @Column(name = "registration_number")
    @EqualsAndHashCode.Include
    private Integer registrationNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "bulstat")
    private String bulstat;

    @Column(name = "chairman")
    private String chairman;

    @Column(name = "chitalishta_url")
    private String chitalishtaUrl;

    @Column(name = "email")
    private String email;

    @Column(name = "municipality")
    private String municipality;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "region")
    private String region;

    @Column(name = "secretary")
    private String secretary;

    @Column(name = "status")
    private String status;

    @Column(name = "town")
    private String town;

    @Column(name = "url_to_libraries_site")
    private String urlToLibrariesSite;

    @OneToMany(mappedBy = "chitalishte", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InformationCard> informationCards = new ArrayList<>();
}