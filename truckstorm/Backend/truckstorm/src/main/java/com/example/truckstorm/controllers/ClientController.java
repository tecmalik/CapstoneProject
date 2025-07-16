package com.example.truckstorm.controllers;

import com.example.truckstorm.data.models.Load;
import com.example.truckstorm.services.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final LoadService loadService;

    @Autowired
    public ClientController(LoadService loadService) {
        this.loadService = loadService;
    }

    @GetMapping("/{clientId}/loads")
    public ResponseEntity<List<Load>> getClientLoads(@PathVariable String clientId) {
        List<Load> loads = loadService.getLoadsByClientId(clientId);
        return ResponseEntity.ok(loads);
    }

    @PostMapping("/{clientId}/loads")
    public ResponseEntity<Load> createLoadForClient(
            @PathVariable String clientId, 
            @RequestBody Load load) {
        load.setClientId(clientId);
        Load savedLoad = loadService.postLoad(load);
        return ResponseEntity.ok(savedLoad);
    }

    @GetMapping("/{clientId}/loads/{loadId}")
    public ResponseEntity<Load> getClientLoad(
            @PathVariable String clientId,
            @PathVariable Long loadId) {
        Load load = loadService.getLoadById(loadId);
        // In a real application, you'd verify the load belongs to the client
        if (!clientId.equals(load.getClientId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(load);
    }
}
