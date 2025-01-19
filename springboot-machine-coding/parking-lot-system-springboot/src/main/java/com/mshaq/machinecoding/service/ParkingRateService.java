package com.mshaq.machinecoding.service;

import com.mshaq.machinecoding.Enum.ParkingSpotType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ParkingRateService {

    @Value("#{${parkingspot.baseRates}}")
    private Map<ParkingSpotType, Double> baseRates;


    public Double calculateRate(ParkingSpotType spotType, long durationInMinutes) {
        Double spotPrice = baseRates.get(spotType);
        double hours = durationInMinutes / 60.0;
        return Math.ceil(hours) * spotPrice;
    }
}
