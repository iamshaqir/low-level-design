package com.mshaq.machinecoding.service;

import com.mshaq.machinecoding.Enum.ParkingSpotType;
import com.mshaq.machinecoding.Enum.SpotStatus;
import com.mshaq.machinecoding.dto.Success;
import com.mshaq.machinecoding.model.ParkingLevel;
import com.mshaq.machinecoding.model.ParkingSpot;
import com.mshaq.machinecoding.repository.ParkingLevelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class ParkingLevelService {
    private final ParkingLevelRepository levelRepository;

    public ParkingLevelService(ParkingLevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    public Success addSpots(Long levelId, Collection<ParkingSpot> spots) {
        return saveSpots(levelId, spots);
    }

    @Transactional
    private Success saveSpots(Long levelId, Collection<ParkingSpot> spots) {
        log.info("Adding spots to parking level ID: {}", levelId);
        ParkingLevel parkingLevel = getParkingLevel(levelId);
        parkingLevel.addAllParkingSpots(spots);
        ParkingLevel save = levelRepository.save(parkingLevel);
        String message = String.format("Total number of spots created %d", save.getParkingSpots().size());
        return new Success(message, HttpStatus.CREATED.name());
    }

    private ParkingLevel getParkingLevel(Long levelId) {
        return levelRepository.findById(levelId)
                .orElseThrow(() -> {
                    log.info("Parking level with ID: {} not found", levelId);
                    return new NoSuchElementException("Parking level not found");
                });
    }

    public Optional<ParkingSpot> findParkingSpot(Long levelId, ParkingSpotType parkingSpotType) {
        ParkingLevel level = getParkingLevel(levelId);
        return level.getParkingSpots().stream()
                .filter(spot -> spot.getSpotType().equals(parkingSpotType))
                .filter(spot -> spot.getStatus().equals(SpotStatus.AVAILABLE))
                .findFirst();
    }


}
