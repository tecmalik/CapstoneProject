package com.example.truckstorm.dtos.response;

import com.example.truckstorm.data.models.LoadStatus;
import com.example.truckstorm.data.models.LoadType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoadResponse {
    private int PostResponseId;
    private int LoadId;
    private Boolean loadUpdated;
    private String pickupLocation;
    private String deliveryLocation;
    private Double weight;
    private LoadType loadType;
    private LocalDateTime createdAt;
    private LoadStatus loadStatus;
    private int clientId;
}


