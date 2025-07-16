package com.example.truckstorm.services;

import com.example.truckstorm.data.models.*;
import com.example.truckstorm.data.repository.BidRepository;
import com.example.truckstorm.data.repository.DriverRepository;
import com.example.truckstorm.data.repository.LoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BidServiceImpl implements BidService {
    
    private final DriverRepository driverRepository;
    private final LoadRepository loadRepository;
    private final BidRepository bidRepository;
    private final LoadService loadService;
    private final DriverService driverService;

    @Autowired
    public BidServiceImpl(DriverRepository driverRepository, 
                         LoadRepository loadRepository,
                         BidRepository bidRepository,
                         LoadService loadService,
                         DriverService driverService) {
        this.driverRepository = driverRepository;
        this.loadRepository = loadRepository;
        this.bidRepository = bidRepository;
        this.loadService = loadService;
        this.driverService = driverService;
    }

    // Driver assignment functionality
    @Override
    public List<Driver> findCompatibleDriversForLoad(Load load) {
        List<Driver> nearbyDrivers = driverRepository.findByCurrentLocationAndAvailable(
            load.getPickupLocation(), true);

        return nearbyDrivers.stream()
                .filter(driver -> driver.getMaxLoadCapacity() >= load.getWeight())
                .filter(driver -> isTruckTypeCompatible(driver.getTruckType(), load.getLoadType()))
                .collect(Collectors.toList());
    }

    @Override
    public Driver assignDriverToLoad(Long loadId, Long driverId) {
        Load load = loadService.getLoadById(loadId);
        Driver driver = driverService.getDriverById(driverId);

        if (!findCompatibleDriversForLoad(load).contains(driver)) {
            throw new IllegalArgumentException("Driver is not compatible with this load");
        }

        // Update load status
        load.setLoadStatus(LoadStatus.ASSIGNED);
        loadRepository.save(load);

        // Update driver availability
        driver.setAvailable(false);
        return driverRepository.save(driver);
    }

    // Bidding functionality
    @Override
    public Bid createBid(Bid bid) {
        if (bid == null) {
            throw new IllegalArgumentException("Bid cannot be null");
        }
        
        // Set default values if not provided
        if (bid.getBidStatus() == null) {
            bid.setBidStatus(BidStatus.PENDING);
        }
        if (bid.getBidTimestamp() == null) {
            bid.setBidTimestamp(Instant.now());
        }
        
        return bidRepository.save(bid);
    }

    @Override
    public Bid getBidById(Long bidId) {
        if (bidId == null) {
            throw new IllegalArgumentException("Bid ID cannot be null");
        }
        
        return bidRepository.findById(bidId)
                .orElseThrow(() -> new IllegalArgumentException("Bid not found with id: " + bidId));
    }

    @Override
    public List<Bid> getBidsByLoadId(Long loadId) {
        if (loadId == null) {
            throw new IllegalArgumentException("Load ID cannot be null");
        }
        return bidRepository.findByLoadId(loadId);
    }

    @Override
    public List<Bid> getBidsByDriverId(Long driverId) {
        if (driverId == null) {
            throw new IllegalArgumentException("Driver ID cannot be null");
        }
        return bidRepository.findByDriverId(driverId);
    }

    @Override
    public List<Bid> getBidsByClientId(Long clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID cannot be null");
        }
        return bidRepository.findByClientId(clientId);
    }

    @Override
    public Bid updateBidStatus(Long bidId, BidStatus status) {
        if (bidId == null) {
            throw new IllegalArgumentException("Bid ID cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        
        Bid bid = getBidById(bidId);
        bid.setBidStatus(status);
        return bidRepository.save(bid);
    }

    @Override
    public Bid acceptBid(Long bidId) {
        Bid bid = updateBidStatus(bidId, BidStatus.ACCEPTED);
        
        // When a bid is accepted, assign the driver to the load
        if (bid.getLoad() != null && bid.getDriver() != null) {
            assignDriverToLoad(bid.getLoad().getId(), bid.getDriver().getUserID());
        }
        
        return bid;
    }

    @Override
    public Bid rejectBid(Long bidId) {
        return updateBidStatus(bidId, BidStatus.REJECTED);
    }

    @Override
    public void deleteBid(Long bidId) {
        if (bidId == null) {
            throw new IllegalArgumentException("Bid ID cannot be null");
        }
        
        Bid bid = getBidById(bidId); // This will throw exception if not found
        bidRepository.delete(bid);
    }

    private boolean isTruckTypeCompatible(TruckType truckType, LoadType loadType) {
        // Refrigerated loads require refrigerated trucks
        if (LoadType.REFRIGERATED.equals(loadType)) {
            return TruckType.REFRIGERATED.equals(truckType);
        }
        
        // Hazardous materials require special truck types
        if (LoadType.HAZARDOUS.equals(loadType)) {
            return TruckType.TANKER.equals(truckType) || TruckType.FLATBED.equals(truckType);
        }
        
        // All other loads can be handled by any truck type
        return true;
    }
}
