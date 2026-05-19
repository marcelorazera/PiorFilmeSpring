package com.outsera.pior_filme.service;

import com.outsera.pior_filme.dto.AwardIntervalDto;
import com.outsera.pior_filme.dto.AwardIntervalResponseDto;
import com.outsera.pior_filme.model.Movie;
import com.outsera.pior_filme.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AwardIntervalService {

    private final MovieRepository movieRepository;

    public AwardIntervalService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public AwardIntervalResponseDto getProducersWithAwardIntervals() {
        List<Movie> winningMovies = movieRepository.findAllWinners();

         Map<String, List<Integer>> producerWinYears = new TreeMap<>();

        for (Movie movie : winningMovies) {
            movie.getProducers().forEach(producer -> {
                producerWinYears.computeIfAbsent(producer.getName(), k -> new ArrayList<>())
                        .add(movie.getYear());
            });
        }

         List<AwardIntervalDto> intervals = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry : producerWinYears.entrySet()) {
            String producerName = entry.getKey();
            List<Integer> years = entry.getValue();

            Collections.sort(years);

            if (years.size() < 2) {
                continue;
            }

             for (int i = 0; i < years.size() - 1; i++) {
                int previousWin = years.get(i);
                int followingWin = years.get(i + 1);
                int interval = followingWin - previousWin;

                intervals.add(new AwardIntervalDto(producerName, interval, previousWin, followingWin));
            }
        }

        List<AwardIntervalDto> minIntervals = intervals.stream()
                .filter(interval -> interval.getInterval().equals(
                        intervals.stream().map(AwardIntervalDto::getInterval).min(Integer::compareTo).orElse(Integer.MAX_VALUE)
                ))
                .sorted(Comparator.comparing(AwardIntervalDto::getProducer))
                .collect(Collectors.toList());

        List<AwardIntervalDto> maxIntervals = intervals.stream()
                .filter(interval -> interval.getInterval().equals(
                        intervals.stream().map(AwardIntervalDto::getInterval).max(Integer::compareTo).orElse(Integer.MIN_VALUE)
                ))
                .sorted(Comparator.comparing(AwardIntervalDto::getProducer))
                .collect(Collectors.toList());

        return new AwardIntervalResponseDto(minIntervals, maxIntervals);
    }
}
