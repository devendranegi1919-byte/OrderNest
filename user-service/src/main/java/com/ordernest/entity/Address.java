package com.ordernest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;
    @Column(nullable = false)
    private String houseNumber;
    @Column(nullable = false)
    private String block;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String zip;
    @Column(nullable = false)
    private String country;
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
