package com.mshaq.machinecoding.repository;

import com.mshaq.machinecoding.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
