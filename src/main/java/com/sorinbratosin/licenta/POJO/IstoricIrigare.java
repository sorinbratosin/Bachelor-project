package com.sorinbratosin.licenta.POJO;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "istoric_irigare")
public class IstoricIrigare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_senzor")
    private DateSenzori dateSenzori;

    @Column(name = "data_adaugarii")
    private LocalDateTime dataAdaugarii;
}
