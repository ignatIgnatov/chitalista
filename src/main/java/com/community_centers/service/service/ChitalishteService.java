package com.community_centers.service.service;

import com.community_centers.service.entity.Chitalishte;
import com.community_centers.service.entity.InformationCard;
import com.community_centers.service.repository.ChitalishteRepository;
import com.community_centers.service.repository.InformationCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChitalishteService {

    private final ChitalishteRepository chitalishteRepository;
    private final InformationCardRepository informationCardRepository;

    // Chitalishte CRUD operations

    public Page<Chitalishte> getAllChitalista(Pageable pageable) {
        return chitalishteRepository.findAll(pageable);
    }

    public List<Chitalishte> getAllChitalista() {
        return chitalishteRepository.findAll();
    }

    public Chitalishte getChitalishteById(Integer id) {
        return chitalishteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chitalishte not found with id: " + id));
    }

    public Chitalishte createChitalishte(Chitalishte chitalishte) {
        // Check if bulstat already exists
        if (chitalishte.getBulstat() != null &&
                chitalishteRepository.findByBulstat(chitalishte.getBulstat()).isPresent()) {
            throw new RuntimeException("Chitalishte with bulstat " + chitalishte.getBulstat() + " already exists");
        }

        return chitalishteRepository.save(chitalishte);
    }

    public Chitalishte updateChitalishte(Integer id, Chitalishte chitalishteDetails) {
        Chitalishte chitalishte = chitalishteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chitalishte not found with id: " + id));

        // Check if bulstat is being changed and if new bulstat already exists
        if (chitalishteDetails.getBulstat() != null &&
                !chitalishteDetails.getBulstat().equals(chitalishte.getBulstat()) &&
                chitalishteRepository.findByBulstat(chitalishteDetails.getBulstat()).isPresent()) {
            throw new RuntimeException("Chitalishte with bulstat " + chitalishteDetails.getBulstat() + " already exists");
        }

        // Update fields
        updateChitalishteFields(chitalishte, chitalishteDetails);

        return chitalishteRepository.save(chitalishte);
    }

    public void deleteChitalishte(Integer id) {
        Chitalishte chitalishte = chitalishteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chitalishte not found with id: " + id));

        // Delete all associated information cards first
        List<InformationCard> informationCards = informationCardRepository.findByChitalishteId(id);
        informationCardRepository.deleteAll(informationCards);

        chitalishteRepository.delete(chitalishte);
    }

    // InformationCard CRUD operations

    public List<InformationCard> getInformationCardsByChitalishteId(Integer chitalishteId) {
        return informationCardRepository.findByChitalishteId(chitalishteId);
    }

    public InformationCard getInformationCardById(Integer cardId) {
        return informationCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Information card not found with id: " + cardId));
    }

    public InformationCard createInformationCard(Integer chitalishteId, InformationCard informationCard) {
        Chitalishte chitalishte = chitalishteRepository.findById(chitalishteId)
                .orElseThrow(() -> new RuntimeException("Chitalishte not found with id: " + chitalishteId));

        informationCard.setChitalishte(chitalishte);
        return informationCardRepository.save(informationCard);
    }

    public InformationCard updateInformationCard(Integer cardId, InformationCard cardDetails) {
        InformationCard informationCard = informationCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Information card not found with id: " + cardId));

        // Update fields
        updateInformationCardFields(informationCard, cardDetails);

        return informationCardRepository.save(informationCard);
    }

    public void deleteInformationCard(Integer cardId) {
        InformationCard informationCard = informationCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Information card not found with id: " + cardId));
        informationCardRepository.delete(informationCard);
    }

    // Search methods

    public Page<Chitalishte> searchChitalishtaAdvanced(String name, String region, String municipality, String town, Pageable pageable) {
        if (name != null || region != null || municipality != null || town != null) {
            return chitalishteRepository.findBySearchCriteria(
                    Optional.ofNullable(name).orElse(""),
                    Optional.ofNullable(region).orElse(""),
                    Optional.ofNullable(municipality).orElse(""),
                    Optional.ofNullable(town).orElse(""),
                    pageable
            );
        }

        return chitalishteRepository.findAll(pageable);
    }

    // Utility methods

    private void updateChitalishteFields(Chitalishte chitalishte, Chitalishte chitalishteDetails) {
        if (chitalishteDetails.getName() != null) {
            chitalishte.setName(chitalishteDetails.getName());
        }
        if (chitalishteDetails.getChitalishtaUrl() != null) {
            chitalishte.setChitalishtaUrl(chitalishteDetails.getChitalishtaUrl());
        }
        if (chitalishteDetails.getStatus() != null) {
            chitalishte.setStatus(chitalishteDetails.getStatus());
        }
        if (chitalishteDetails.getBulstat() != null) {
            chitalishte.setBulstat(chitalishteDetails.getBulstat());
        }
        if (chitalishteDetails.getChairman() != null) {
            chitalishte.setChairman(chitalishteDetails.getChairman());
        }
        if (chitalishteDetails.getSecretary() != null) {
            chitalishte.setSecretary(chitalishteDetails.getSecretary());
        }
        if (chitalishteDetails.getPhone() != null) {
            chitalishte.setPhone(chitalishteDetails.getPhone());
        }
        if (chitalishteDetails.getEmail() != null) {
            chitalishte.setEmail(chitalishteDetails.getEmail());
        }
        if (chitalishteDetails.getRegion() != null) {
            chitalishte.setRegion(chitalishteDetails.getRegion());
        }
        if (chitalishteDetails.getMunicipality() != null) {
            chitalishte.setMunicipality(chitalishteDetails.getMunicipality());
        }
        if (chitalishteDetails.getTown() != null) {
            chitalishte.setTown(chitalishteDetails.getTown());
        }
        if (chitalishteDetails.getAddress() != null) {
            chitalishte.setAddress(chitalishteDetails.getAddress());
        }
        if (chitalishteDetails.getUrlToLibrariesSite() != null) {
            chitalishte.setUrlToLibrariesSite(chitalishteDetails.getUrlToLibrariesSite());
        }
        if (chitalishteDetails.getRegistrationNumber() != 0) {
            chitalishte.setRegistrationNumber(chitalishteDetails.getRegistrationNumber());
        }
    }

    private void updateInformationCardFields(InformationCard informationCard, InformationCard cardDetails) {
        if (cardDetails.getUrl() != null) {
            informationCard.setUrl(cardDetails.getUrl());
        }
        if (cardDetails.getYear() != null) {
            informationCard.setYear(cardDetails.getYear());
        }
        if (cardDetails.getTotalMembersCount() != null) {
            informationCard.setTotalMembersCount(cardDetails.getTotalMembersCount());
        }
        if (cardDetails.getMembershipApplications() != null) {
            informationCard.setMembershipApplications(cardDetails.getMembershipApplications());
        }
        if (cardDetails.getNewMembers() != null) {
            informationCard.setNewMembers(cardDetails.getNewMembers());
        }
        if (cardDetails.getRejectedMembers() != null) {
            informationCard.setRejectedMembers(cardDetails.getRejectedMembers());
        }
        if (cardDetails.getParticipationInLiveHumanTreasuresRegional() != null) {
            informationCard.setParticipationInLiveHumanTreasuresRegional(cardDetails.getParticipationInLiveHumanTreasuresRegional());
        }
        if (cardDetails.getParticipationInLiveHumanTreasuresNational() != null) {
            informationCard.setParticipationInLiveHumanTreasuresNational(cardDetails.getParticipationInLiveHumanTreasuresNational());
        }
        if (cardDetails.getWorkshopsClubsArtsText() != null) {
            informationCard.setWorkshopsClubsArtsText(cardDetails.getWorkshopsClubsArtsText());
        }
        if (cardDetails.getWorkshopsClubsArts() != null) {
            informationCard.setWorkshopsClubsArts(cardDetails.getWorkshopsClubsArts());
        }
        if (cardDetails.getLanguageCoursesText() != null) {
            informationCard.setLanguageCoursesText(cardDetails.getLanguageCoursesText());
        }
        if (cardDetails.getLanguageCourses() != null) {
            informationCard.setLanguageCourses(cardDetails.getLanguageCourses());
        }
        if (cardDetails.getKraeznanieClubsText() != null) {
            informationCard.setKraeznanieClubsText(cardDetails.getKraeznanieClubsText());
        }
        if (cardDetails.getKraeznanieClubs() != null) {
            informationCard.setKraeznanieClubs(cardDetails.getKraeznanieClubs());
        }
        if (cardDetails.getMuseumCollectionsText() != null) {
            informationCard.setMuseumCollectionsText(cardDetails.getMuseumCollectionsText());
        }
        if (cardDetails.getMuseumCollections() != null) {
            informationCard.setMuseumCollections(cardDetails.getMuseumCollections());
        }
        if (cardDetails.getFolkloreFormations() != null) {
            informationCard.setFolkloreFormations(cardDetails.getFolkloreFormations());
        }
        if (cardDetails.getTheatreFormations() != null) {
            informationCard.setTheatreFormations(cardDetails.getTheatreFormations());
        }
        if (cardDetails.getDancingGroups() != null) {
            informationCard.setDancingGroups(cardDetails.getDancingGroups());
        }
        if (cardDetails.getModernBallet() != null) {
            informationCard.setModernBallet(cardDetails.getModernBallet());
        }
        if (cardDetails.getVocalGroups() != null) {
            informationCard.setVocalGroups(cardDetails.getVocalGroups());
        }
        if (cardDetails.getAmateurArts() != null) {
            informationCard.setAmateurArts(cardDetails.getAmateurArts());
        }
        if (cardDetails.getOtherClubs() != null) {
            informationCard.setOtherClubs(cardDetails.getOtherClubs());
        }
        informationCard.setHasPcAndInternetServices(cardDetails.isHasPcAndInternetServices());
        if (cardDetails.getParticipationInEvents() != null) {
            informationCard.setParticipationInEvents(cardDetails.getParticipationInEvents());
        }
        if (cardDetails.getProjectsParticipationLeading() != null) {
            informationCard.setProjectsParticipationLeading(cardDetails.getProjectsParticipationLeading());
        }
        if (cardDetails.getProjectsParticipationPartner() != null) {
            informationCard.setProjectsParticipationPartner(cardDetails.getProjectsParticipationPartner());
        }
        if (cardDetails.getDisabilitiesAndVolunteers() != null) {
            informationCard.setDisabilitiesAndVolunteers(cardDetails.getDisabilitiesAndVolunteers());
        }
        if (cardDetails.getOtherActivities() != null) {
            informationCard.setOtherActivities(cardDetails.getOtherActivities());
        }
        if (cardDetails.getSubsidiaryCount() != null) {
            informationCard.setSubsidiaryCount(cardDetails.getSubsidiaryCount());
        }
        if (cardDetails.getEmployeesCount() != null) {
            informationCard.setEmployeesCount(cardDetails.getEmployeesCount());
        }
        if (cardDetails.getEmployeesWithHigherEducation() != null) {
            informationCard.setEmployeesWithHigherEducation(cardDetails.getEmployeesWithHigherEducation());
        }
        if (cardDetails.getEmployeesSpecialized() != null) {
            informationCard.setEmployeesSpecialized(cardDetails.getEmployeesSpecialized());
        }
        if (cardDetails.getAdministrativePositions() != null) {
            informationCard.setAdministrativePositions(cardDetails.getAdministrativePositions());
        }
        if (cardDetails.getSupportingEmployees() != null) {
            informationCard.setSupportingEmployees(cardDetails.getSupportingEmployees());
        }
        if (cardDetails.getParticipationInTrainings() != null) {
            informationCard.setParticipationInTrainings(cardDetails.getParticipationInTrainings());
        }
        if (cardDetails.getSanctionsFor31And33() != null) {
            informationCard.setSanctionsFor31And33(cardDetails.getSanctionsFor31And33());
        }
        if (cardDetails.getBulstat() != null) {
            informationCard.setBulstat(cardDetails.getBulstat());
        }
        if (cardDetails.getRegNumber() != null) {
            informationCard.setRegNumber(cardDetails.getRegNumber());
        }
        if (cardDetails.getTownPopulation() != null) {
            informationCard.setTownPopulation(cardDetails.getTownPopulation());
        }
        if (cardDetails.getTownUsers() != null) {
            informationCard.setTownUsers(cardDetails.getTownUsers());
        }
        if (cardDetails.getLibraryActivity() != null) {
            informationCard.setLibraryActivity(cardDetails.getLibraryActivity());
        }
        if (cardDetails.getEmail() != null) {
            informationCard.setEmail(cardDetails.getEmail());
        }
        if (cardDetails.getWebpage() != null) {
            informationCard.setWebpage(cardDetails.getWebpage());
        }
    }

    // Additional methods

    public Long getTotalCount() {
        return chitalishteRepository.count();
    }

    public List<InformationCard> getAllInformationCards() {
        return informationCardRepository.findAll();
    }

    public boolean chitalishteExists(Integer id) {
        return chitalishteRepository.existsById(id);
    }

    public boolean informationCardExists(Integer cardId) {
        return informationCardRepository.existsById(cardId);
    }
}