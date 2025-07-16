package com.example.truckstorm.data.repository;

import com.example.truckstorm.data.models.Bid;
import com.example.truckstorm.data.models.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByLoadId(Long loadId);
    List<Bid> findByDriverId(Long driverId);
    List<Bid> findByClientId(Long clientId);
    List<Bid> findByBidStatus(BidStatus bidStatus);
    List<Bid> findByLoadIdAndBidStatus(Long loadId, BidStatus bidStatus);
    List<Bid> findByDriverIdAndBidStatus(Long driverId, BidStatus bidStatus);
}