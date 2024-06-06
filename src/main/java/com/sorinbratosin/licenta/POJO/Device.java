package com.sorinbratosin.licenta.POJO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String serialNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
