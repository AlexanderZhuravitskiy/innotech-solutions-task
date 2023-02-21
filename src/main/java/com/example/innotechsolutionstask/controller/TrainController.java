package com.example.innotechsolutionstask.controller;

import com.example.innotechsolutionstask.domain.Train;
import com.example.innotechsolutionstask.dto.TrainDto;
import com.example.innotechsolutionstask.mapper.TrainMapper;
import com.example.innotechsolutionstask.service.TrainService;
import com.example.innotechsolutionstask.web.handler.ControllerExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TrainController {
    private final TrainService trainService;

    private final TrainMapper trainMapper;

    @GetMapping
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String getTrainList(@RequestParam(required = false, defaultValue = "") String departurePointSearch,
                               @RequestParam(required = false, defaultValue = "") String arrivalPointSearch,
                               @RequestParam(required = false, defaultValue = "") String dateSearch,
                               Model model,
                               @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TrainDto> page = trainService.getTrainList(departurePointSearch, arrivalPointSearch, dateSearch, pageable);
        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("departurePointSearch", departurePointSearch);
        model.addAttribute("arrivalPointSearch", arrivalPointSearch);
        model.addAttribute("dateSearch", dateSearch);
        return "main";
    }

    @PostMapping("/main")
    public String addTrain(@Valid Train train,
                           BindingResult bindingResult,
                           Model model,
                           @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
        TrainDto trainDto = trainMapper.trainToTrainDto(train);
        if(bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerExceptionHandler.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("train", trainDto);
        } else {
            model.addAttribute("train", null);
            trainService.addTrain(trainDto);
        }
        Page<TrainDto> page = trainService.getAllTrains(pageable);
        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        return "main";
    }
}