package com.example.truckstorm.services;

import com.example.truckstorm.data.models.Load;
import com.example.truckstorm.data.models.LoadStatus;
import com.example.truckstorm.data.repository.LoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoadServiceImpl implements LoadService {

    private final LoadRepository loadRepository;

    @Autowired
    public LoadServiceImpl(LoadRepository loadRepository) {
        this.loadRepository = loadRepository;
    }

    @Override
    public Load postLoad(Load load) {
        // Set default status if not provided
        if (load.getStatus() == null) {
            load.setStatus(LoadStatus.PENDING);
        }
        return loadRepository.save(load);
    }

    @Override
    public Load getLoadById(Long id) {
        Optional<Load> load = loadRepository.findById(id);
        return load.orElseThrow(() -> new RuntimeException("Load not found with id: " + id));
    }

    @Override
    public List<Load> getAllLoads() {
        return loadRepository.findAll();
    }

    @Override
    public List<Load> getLoadsByClientId(String clientId) {
        return loadRepository.findByClientId(clientId);
    }

    @Override
    public Load updateLoadStatus(Long loadId, LoadStatus status) {
        Load load = getLoadById(loadId);
        load.setStatus(status);
        return loadRepository.save(load);
    }

    @Override
    public void deleteLoad(Long id) {
        if (!loadRepository.existsById(id)) {
            throw new RuntimeException("Load not found with id: " + id);
        }
        loadRepository.deleteById(id);
    }
}
