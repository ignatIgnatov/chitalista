package com.community_centers.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class InformationCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "chitalishte_id", nullable = false)
    private Chitalishte chitalishte;

    private String url;

    private Integer year;

    private Integer totalMembersCount;

    private Integer membershipApplications;

    private Integer newMembers;

    private Integer rejectedMembers;

    private Integer participationInLiveHumanTreasuresRegional;

    private Integer participationInLiveHumanTreasuresNational;

    private String workshopsClubsArtsText;

    private Integer workshopsClubsArts;

    private String languageCoursesText;

    private Integer languageCourses;

    private String kraeznanieClubsText;

    private Integer kraeznanieClubs;

    private String museumCollectionsText;

    private Integer museumCollections;

    private Integer folkloreFormations;

    private Integer theatreFormations;

    private Integer dancingGroups;

    private Integer modernBallet;

    private Integer vocalGroups;

    private Integer amateurArts;

    private Integer otherClubs;

    private boolean hasPcAndInternetServices;

    private Integer participationInEvents;

    private Integer projectsParticipationLeading;

    private Integer projectsParticipationPartner;

    private Integer disabilitiesAndVolunteers;

    private Integer otherActivities;

    private Double subsidiaryCount;

    private Double employeesCount;

    private Integer employeesWithHigherEducation;

    private Integer employeesSpecialized;

    private Integer administrativePositions;

    private Integer supportingEmployees;

    private Integer participationInTrainings;

    private String sanctionsFor31And33;

    //additional
    private String bulstat;

    private Integer regNumber;

    private Integer townPopulation;

    private Integer townUsers;

    private Integer libraryActivity;

    private String email;

    private String webpage;
}
