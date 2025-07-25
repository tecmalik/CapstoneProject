package com.example.truckstorm.data.repository;

import com.example.truckstorm.data.models.AccountStatus;
import com.example.truckstorm.data.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findByAccountStatus(AccountStatus status);
}
