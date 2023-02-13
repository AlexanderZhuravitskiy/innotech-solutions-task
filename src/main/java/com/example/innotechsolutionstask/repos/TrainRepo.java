package com.example.innotechsolutionstask.repos;

import com.example.innotechsolutionstask.domain.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepo extends JpaRepository<Train, Long> {

    Page<Train> findAll(Pageable pageable);

    Page<Train> findByDeparturePointAndArrivalPointAndDate(String departurePoint, String arrivalPoint,
                                                           String date, Pageable pageable);
}
