package com.mshaq.machinecoding.service;

import com.mshaq.machinecoding.Enum.ParkingSpotType;
import com.mshaq.machinecoding.Enum.SpotStatus;
import com.mshaq.machinecoding.model.ParkingLevel;
import com.mshaq.machinecoding.model.ParkingSpot;
import com.mshaq.machinecoding.repository.ParkingLevelRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Component
public class InitializeDefaultSpots {

    @Value("#{${parkinglevel.defaultSpots}}")
    private Map<String, Integer> defaultSpots;

    private final ParkingLevelRepository parkingLevelRepository;

    public InitializeDefaultSpots(ParkingLevelRepository parkingLevelRepository) {
        this.parkingLevelRepository = parkingLevelRepository;
    }

    @Transactional
    @PostConstruct
    public void init() {
        ParkingLevel level = new ParkingLevel("BASEMENT");
        log.info("Adding level {}", level.getLevelName());
        defaultSpots.forEach((type, count) -> {
            for (int i = 0; i < count; i++) {
                String spotNumber = type.charAt(0) + String.valueOf(count);
                ParkingSpot spot = new ParkingSpot(spotNumber, ParkingSpotType.valueOf(type),
                        SpotStatus.AVAILABLE);
                level.addParkingSpot(spot);
            }
        });
        parkingLevelRepository.save(level);
        log.info("Added default spots to parking level ID: {}", level.getId());
    }
}
