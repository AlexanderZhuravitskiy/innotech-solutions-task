package com.example.innotechsolutionstask.mapper;

import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.dto.TrainDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainMapper {
    TrainDto trainToTrainDto(Train train);

    Train trainDtoToTrain(TrainDto trainDto);
}
