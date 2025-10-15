package com.community_centers.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "information_card")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "chitalishte")
public class InformationCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chitalishte_id", referencedColumnName = "registration_number")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Chitalishte chitalishte;

    @Column(name = "administrative_positions")
    private Integer administrativePositions;

    @Column(name = "amateur_arts")
    private Integer amateurArts;

    @Column(name = "dancing_groups")
    private Integer dancingGroups;

    @Column(name = "disabilities_and_volunteers")
    private Integer disabilitiesAndVolunteers;

    @Column(name = "employees_count")
    private Double employeesCount;

    @Column(name = "employees_specialized")
    private Integer employeesSpecialized;

    @Column(name = "employees_with_higher_education")
    private Integer employeesWithHigherEducation;

    @Column(name = "folklore_formations")
    private Integer folkloreFormations;

    @Column(name = "has_pc_and_internet_services")
    private Boolean hasPcAndInternetServices;

    @Column(name = "kraeznanie_clubs")
    private Integer kraeznanieClubs;

    @Column(name = "language_courses")
    private Integer languageCourses;

    @Column(name = "library_activity")
    private String libraryActivity;

    @Column(name = "membership_applications")
    private Integer membershipApplications;

    @Column(name = "modern_ballet")
    private Integer modernBallet;

    @Column(name = "museum_collections")
    private Integer museumCollections;

    @Column(name = "new_members")
    private Integer newMembers;

    @Column(name = "other_activities")
    private String otherActivities;

    @Column(name = "other_clubs")
    private Integer otherClubs;

    @Column(name = "participation_in_events")
    private Integer participationInEvents;

    @Column(name = "participation_in_live_human_treasures_national")
    private Integer participationInLiveHumanTreasuresNational;

    @Column(name = "participation_in_live_human_treasures_regional")
    private Integer participationInLiveHumanTreasuresRegional;

    @Column(name = "participation_in_trainings")
    private Integer participationInTrainings;

    @Column(name = "projects_participation_leading")
    private Integer projectsParticipationLeading;

    @Column(name = "projects_participation_partner")
    private Integer projectsParticipationPartner;

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "rejected_members")
    private Integer rejectedMembers;

    @Column(name = "subsidiary_count")
    private Double subsidiaryCount;

    @Column(name = "supporting_employees")
    private Integer supportingEmployees;

    @Column(name = "theatre_formations")
    private Integer theatreFormations;

    @Column(name = "total_members_count")
    private Integer totalMembersCount;

    @Column(name = "town_population")
    private Integer townPopulation;

    @Column(name = "town_users")
    private Integer townUsers;

    @Column(name = "vocal_groups")
    private Integer vocalGroups;

    @Column(name = "workshops_clubs_arts")
    private Integer workshopsClubsArts;

    @Column(name = "year")
    private Integer year;

    @Column(name = "kraeznanie_clubs_text")
    private String kraeznanieClubsText;

    @Column(name = "language_courses_text")
    private String languageCoursesText;

    @Column(name = "museum_collections_text")
    private String museumCollectionsText;

    @Column(name = "sanctions_for31and33")
    private String sanctionsFor31and33;

    @Column(name = "url")
    private String url;

    @Column(name = "webpage")
    private String webpage;

    @Column(name = "workshops_clubs_arts_text")
    private String workshopsClubsArtsText;
}
