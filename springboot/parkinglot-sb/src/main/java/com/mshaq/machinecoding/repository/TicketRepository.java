package com.mshaq.machinecoding.repository;

import com.mshaq.machinecoding.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
