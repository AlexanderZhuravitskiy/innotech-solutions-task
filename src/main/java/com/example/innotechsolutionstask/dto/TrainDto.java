package com.example.innotechsolutionstask.dto;

import com.example.innotechsolutionstask.domain.Client;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrainDto {
    private Long id;

    private String departurePoint;

    private String arrivalPoint;

    private String departureTime;

    private String arrivalTime;

    private String date;

    private Integer price;

    private Integer freePlaces;

    private List<Client> users = new ArrayList<>();
}
