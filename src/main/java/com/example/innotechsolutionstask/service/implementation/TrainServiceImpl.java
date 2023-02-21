package com.example.innotechsolutionstask.service.implementation;

import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.dto.TrainDto;
import com.example.innotechsolutionstask.exceptions.NotFoundException;
import com.example.innotechsolutionstask.exceptions.ValueEntryException;
import com.example.innotechsolutionstask.mapper.TrainMapper;
import com.example.innotechsolutionstask.repos.TrainRepo;
import com.example.innotechsolutionstask.service.TrainService;
import com.example.innotechsolutionstask.web.constant.WebConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
    private final TrainRepo trainRepo;

    private final TrainMapper trainMapper;

    @Override
    public Page<TrainDto> getAllTrains(Pageable pageable) {
        log.info("Fetching a list of trains");
        Page<Train> trainPage = trainRepo.findAll(pageable);
        List<TrainDto> trainDtoList = trainPage.stream()
                .filter(Objects::nonNull)
                .map(trainMapper::trainToTrainDto)
                .collect(Collectors.toList());
        log.info("Received a list of trains: {}", trainDtoList);
        return new PageImpl<>(trainDtoList, pageable, trainPage.getTotalElements());
    }

    @Override
    public Page<TrainDto> getTrainList(String departurePointSearch,
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
    public TrainDto getTrainById(Long id) {
        log.info("Fetching a train by id: {}", id);
        Train train = trainRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Train with id: " + id + " not found"));
        log.info("Received train: {}", train);
        return trainMapper.trainToTrainDto(train);
    }

    @Override
    public Page<TrainDto> getTrainsByDeparturePointAndArrivalPointAndDate(String departurePoint,
                                                                          String arrivalPoint,
                                                                          String date,
                                                                          Pageable pageable) {
        log.info("Fetching a trains by departurePoint: {}, arrivalPoint: {} and date: {}",
                departurePoint, arrivalPoint, date);
        Page<Train> trainPage = trainRepo.findByDeparturePointAndArrivalPointAndDate(departurePoint,
                arrivalPoint, date, pageable);
        List<TrainDto> trainDtoList = trainPage.stream()
                .filter(Objects::nonNull)
                .map(trainMapper::trainToTrainDto)
                .collect(Collectors.toList());
        log.info("Received a list of trains: {}", trainDtoList);
        return new PageImpl<>(trainDtoList, pageable, trainPage.getTotalElements());
    }

    @Override
    @Transactional
    public void addTrain(TrainDto trainDto) {
        Train train = trainMapper.trainDtoToTrain(trainDto);
        trainValid(train);
        trainRepo.save(train);
        log.info("Saving train: {}", train);
    }

    @Override
    @Transactional
    public void updateTrain(TrainDto trainDto) {
        Train train = trainMapper.trainDtoToTrain(trainDto);
        trainValid(train);
        trainRepo.save(train);
        log.info("Saving updated train: {}", train);
    }

    @Override
    @Transactional
    public void deleteTrain(TrainDto trainDto) {
        Train train = trainMapper.trainDtoToTrain(trainDto);
        trainRepo.delete(train);
        log.info("Deleting train: {}", train);
    }

    private void trainValid(Train train){
        if (train.getPrice() <= 0 || train.getFreePlaces() <= 0){
            throw new ValueEntryException(WebConstant.WRONG_ENTRY_MESSAGE);
        }
    }
}
