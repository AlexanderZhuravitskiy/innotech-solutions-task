package com.example.innotechsolutionstask.service.implementation;

import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.exceptions.NotFoundException;
import com.example.innotechsolutionstask.exceptions.ValueEntryException;
import com.example.innotechsolutionstask.repos.TrainRepo;
import com.example.innotechsolutionstask.service.TrainService;
import com.example.innotechsolutionstask.web.constant.WebConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
    private final TrainRepo trainRepo;

    @Override
    public Page<Train> getAllTrains(Pageable pageable) {
        log.info("Fetching a list of trains");
        return trainRepo.findAll(pageable);
    }

    @Override
    public Page<Train> getTrainList(String departurePointSearch,
                                    String arrivalPointSearch,
                                    String dateSearch,
                                    Pageable pageable) {
        if (!departurePointSearch.isBlank() && !arrivalPointSearch.isBlank() && !dateSearch.isBlank()) {
            return getTrainsByDeparturePointAndArrivalPointAndDate(departurePointSearch, arrivalPointSearch,
                    dateSearch, pageable);
        } else {
            return getAllTrains(pageable);
        }
    }

    @Override
    public Train getTrainById(Long id) {
        log.info("Fetching a train by id: {}", id);
        return trainRepo.findById(id).orElseThrow(() -> new NotFoundException("Train with id: " + id + " not found"));
    }

    @Override
    public Page<Train> getTrainsByDeparturePointAndArrivalPointAndDate(String departurePoint, String arrivalPoint,
                                                                      String date, Pageable pageable) {
        log.info("Fetching a trains by departurePoint: {}, arrivalPoint: {} and date: {}", departurePoint, arrivalPoint, date);
        return trainRepo.findByDeparturePointAndArrivalPointAndDate(departurePoint, arrivalPoint, date, pageable);
    }

    @Override
    @Transactional
    public void addTrain(Train train) {
        trainValid(train);
        log.info("Saving train: {}", train);
        trainRepo.save(train);
    }

    @Override
    @Transactional
    public void updateTrain(Train train) {
        log.info("Saving updated train: {}", train);
        trainRepo.save(train);
    }

    @Override
    @Transactional
    public void deleteTrain(Train train) {
        log.info("Deleting train: {}", train);
        trainRepo.delete(train);
    }

    private void trainValid(Train train){
        if (train.getPrice() <= 0 || train.getFreePlaces() <= 0){
            throw new ValueEntryException(WebConstant.WRONG_ENTRY_MESSAGE);
        }
    }
}
