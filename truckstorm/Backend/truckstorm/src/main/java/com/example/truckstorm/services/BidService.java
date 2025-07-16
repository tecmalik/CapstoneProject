package com.example.truckstorm.services;

import com.example.truckstorm.data.models.Bid;
import com.example.truckstorm.data.models.BidStatus;
import com.example.truckstorm.data.models.Driver;
import com.example.truckstorm.data.models.Load;

import java.util.List;

public interface BidService {
    // Driver assignment functionality
    List<Driver> findCompatibleDriversForLoad(Load load);
    Driver assignDriverToLoad(Long loadId, Long driverId);
    
    // Bidding functionality
    Bid createBid(Bid bid);
    Bid getBidById(Long bidId);
    List<Bid> getBidsByLoadId(Long loadId);
    List<Bid> getBidsByDriverId(Long driverId);
    List<Bid> getBidsByClientId(Long clientId);
    Bid updateBidStatus(Long bidId, BidStatus status);
    Bid acceptBid(Long bidId);
    Bid rejectBid(Long bidId);
    void deleteBid(Long bidId);
}
