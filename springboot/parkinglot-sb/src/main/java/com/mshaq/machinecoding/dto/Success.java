package com.mshaq.machinecoding.dto;

import lombok.Data;

import java.time.LocalDateTime;

public class Success {
    private String message;
    private String status;

    private LocalDateTime localDateTime;

    public Success(String message, String status) {
        this.message = message;
        this.status = status;
        this.localDateTime = LocalDateTime.now();
    }
}
