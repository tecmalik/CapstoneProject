package com.example.truckstorm.controllers;

import com.example.truckstorm.data.models.Load;
import com.example.truckstorm.data.models.LoadStatus;
import com.example.truckstorm.services.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loads")
public class LoadController {

    private final LoadService loadService;

    @Autowired
    public LoadController(LoadService loadService) {
        this.loadService = loadService;
    }

    @PostMapping
    public ResponseEntity<Load> createLoad(@RequestBody Load load) {
        Load savedLoad = loadService.postLoad(load);
        return new ResponseEntity<>(savedLoad, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Load> getLoadById(@PathVariable Long id) {
        Load load = loadService.getLoadById(id);
        return ResponseEntity.ok(load);
    }

    @GetMapping
    public ResponseEntity<List<Load>> getAllLoads() {
        List<Load> loads = loadService.getAllLoads();
        return ResponseEntity.ok(loads);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Load>> getLoadsByClientId(@PathVariable String clientId) {
        List<Load> loads = loadService.getLoadsByClientId(clientId);
        return ResponseEntity.ok(loads);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Load> updateLoadStatus(
            @PathVariable Long id,
            @RequestParam LoadStatus status) {
        Load updatedLoad = loadService.updateLoadStatus(id, status);
        return ResponseEntity.ok(updatedLoad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoad(@PathVariable Long id) {
        loadService.deleteLoad(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Load>> getLoadsByStatus(@PathVariable LoadStatus status) {
        // This would require adding the method to LoadService and LoadRepository
        // For now, return all loads - should be implemented later
        List<Load> loads = loadService.getAllLoads();
        return ResponseEntity.ok(loads);
    }
}
