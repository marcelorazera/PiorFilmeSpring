package com.outsera.pior_filme.service;

import com.outsera.pior_filme.dto.MovieDto;
import com.outsera.pior_filme.model.Movie;
import com.outsera.pior_filme.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAllOrderByYearAsc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getAllWinners() {
        return movieRepository.findAllWinners().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MovieDto convertToDto(Movie movie) {
        Set<String> producerNames = movie.getProducers().stream()
                .map(producer -> producer.getName())
                .collect(Collectors.toSet());

        return new MovieDto(
                movie.getId(),
                movie.getYear(),
                movie.getTitle(),
                movie.getStudios(),
                producerNames,
                movie.getWinner()
        );
    }
}
