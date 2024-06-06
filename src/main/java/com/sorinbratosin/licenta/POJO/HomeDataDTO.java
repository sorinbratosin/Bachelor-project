package com.sorinbratosin.licenta.POJO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HomeDataDTO {
    private String lastName;
    private LocalDateTime dataIrigare;
    private int umiditate;
    private LocalDateTime dataAdaugarii;
}
