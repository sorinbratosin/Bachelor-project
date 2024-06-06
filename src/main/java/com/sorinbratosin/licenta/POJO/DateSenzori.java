package com.sorinbratosin.licenta.POJO;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "date_senzori")
public class DateSenzori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int umiditate;

    @ManyToOne
    @JoinColumn(name = "id_device")
    private Device device;

    @Column(name = "data_adaugarii")
    private LocalDateTime dataAdaugarii;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
