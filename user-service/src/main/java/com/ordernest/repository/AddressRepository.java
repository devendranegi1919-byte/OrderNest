package com.ordernest.repository;

import com.ordernest.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    public List<Address> findByUser_UserId(UUID userId);
}
