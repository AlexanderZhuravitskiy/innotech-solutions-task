package com.example.innotechsolutionstask.service;

import com.example.innotechsolutionstask.dto.TrainDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrainService {

    Page<TrainDto> getAllTrains(Pageable pageable);

    Page<TrainDto> getTrainList(String departurePointSearch,
                             String arrivalPointSearch,
                             String dateSearch,
                             Pageable pageable);

    TrainDto getTrainById(Long id);

    Page<TrainDto> getTrainsByDeparturePointAndArrivalPointAndDate(String departurePoint,
                                                               String arrivalPoint,
                                                               String date,
                                                               Pageable pageable);

    void addTrain(TrainDto trainDto);

    void updateTrain(TrainDto trainDto);

    void deleteTrain(TrainDto trainDto);
}
