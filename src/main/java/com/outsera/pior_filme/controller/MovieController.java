package com.outsera.pior_filme.controller;

import com.outsera.pior_filme.dto.AwardIntervalResponseDto;
import com.outsera.pior_filme.dto.MovieDto;
import com.outsera.pior_filme.service.AwardIntervalService;
import com.outsera.pior_filme.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final AwardIntervalService awardIntervalService;

    public MovieController(MovieService movieService, AwardIntervalService awardIntervalService) {
        this.movieService = movieService;
        this.awardIntervalService = awardIntervalService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/winners")
    public ResponseEntity<List<MovieDto>> getWinners() {
        return ResponseEntity.ok(movieService.getAllWinners());
    }

    @GetMapping("/producers/intervals")
    public ResponseEntity<AwardIntervalResponseDto> getProducersWithAwardIntervals() {
        return ResponseEntity.ok(awardIntervalService.getProducersWithAwardIntervals());
    }
}
