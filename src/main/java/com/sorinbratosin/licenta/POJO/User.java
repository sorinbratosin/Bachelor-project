package com.sorinbratosin.licenta.POJO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email,lastName,firstName,password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Device> devices;
}

