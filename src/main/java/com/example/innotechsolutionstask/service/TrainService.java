package com.example.innotechsolutionstask.service;

import com.example.innotechsolutionstask.domain.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrainService {

    Page<Train> getAllTrains(Pageable pageable);

    Page<Train> getTrainList(String departurePointSearch,
                             String arrivalPointSearch,
                             String dateSearch,
                             Pageable pageable);

    Train getTrainById(Long id);

    Page<Train> getTrainsByDeparturePointAndArrivalPointAndDate(String departurePoint,
                                                               String arrivalPoint,
                                                               String date,
                                                               Pageable pageable);

    void addTrain(Train train);

    void updateTrain(Train train);

    void deleteTrain(Train train);
}
