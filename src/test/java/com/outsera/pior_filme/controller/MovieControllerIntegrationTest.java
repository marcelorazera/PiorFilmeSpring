package com.outsera.pior_filme.controller;

import com.outsera.pior_filme.dto.AwardIntervalDto;
import com.outsera.pior_filme.dto.AwardIntervalResponseDto;
import com.outsera.pior_filme.dto.MovieDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetAllMovies() {
        ResponseEntity<MovieDto[]> response = restTemplate.getForEntity("/api/movies", MovieDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void testGetWinners() {
        ResponseEntity<MovieDto[]> response = restTemplate.getForEntity("/api/movies/winners", MovieDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

       for (MovieDto movie : response.getBody()) {
            assertTrue(movie.getWinner(), "Movie " + movie.getTitle() + " should be a winner");
        }
    }

    @Test
    void testGetProducersWithAwardIntervals() {
        ResponseEntity<AwardIntervalResponseDto> response = restTemplate.getForEntity(
                "/api/movies/producers/intervals", AwardIntervalResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        AwardIntervalResponseDto responseDto = response.getBody();
        
        assertNotNull(responseDto.getMin());
        assertNotNull(responseDto.getMax());
        
        assertTrue(responseDto.getMin().size() > 0, "Should have at least one producer with minimum interval");
        assertTrue(responseDto.getMax().size() > 0, "Should have at least one producer with maximum interval");

        Integer minInterval = responseDto.getMin().get(0).getInterval();
        for (AwardIntervalDto dto : responseDto.getMin()) {
            assertEquals(minInterval, dto.getInterval(), 
                    "All min intervals should have the same value");
        }

        Integer maxInterval = responseDto.getMax().get(0).getInterval();
        for (AwardIntervalDto dto : responseDto.getMax()) {
            assertEquals(maxInterval, dto.getInterval(), 
                    "All max intervals should have the same value");
        }

        assertTrue(minInterval < maxInterval, 
                "Minimum interval should be less than maximum interval");
    }

    @Test
    void testProducerIntervalDataConsistency() {
        ResponseEntity<AwardIntervalResponseDto> response = restTemplate.getForEntity(
                "/api/movies/producers/intervals", AwardIntervalResponseDto.class);

        AwardIntervalResponseDto responseDto = response.getBody();

        for (AwardIntervalDto interval : responseDto.getMin()) {
            assertNotNull(interval.getProducer(), "Producer name should not be null");
            assertNotNull(interval.getInterval(), "Interval should not be null");
            assertNotNull(interval.getPreviousWin(), "Previous win year should not be null");
            assertNotNull(interval.getFollowingWin(), "Following win year should not be null");

            int calculatedInterval = interval.getFollowingWin() - interval.getPreviousWin();
            assertEquals(interval.getInterval(), calculatedInterval,
                    "Interval should be the difference between following and previous win years");
        }

        for (AwardIntervalDto interval : responseDto.getMax()) {
            assertNotNull(interval.getProducer(), "Producer name should not be null");
            assertNotNull(interval.getInterval(), "Interval should not be null");
            assertNotNull(interval.getPreviousWin(), "Previous win year should not be null");
            assertNotNull(interval.getFollowingWin(), "Following win year should not be null");

            int calculatedInterval = interval.getFollowingWin() - interval.getPreviousWin();
            assertEquals(interval.getInterval(), calculatedInterval,
                    "Interval should be the difference between following and previous win years");
        }
    }

    @Test
    void testMovieWinnerDataConsistency() {
        ResponseEntity<MovieDto[]> allMoviesResponse = restTemplate.getForEntity("/api/movies", MovieDto[].class);
        ResponseEntity<MovieDto[]> winnersResponse = restTemplate.getForEntity("/api/movies/winners", MovieDto[].class);

        MovieDto[] allMovies = allMoviesResponse.getBody();
        MovieDto[] winners = winnersResponse.getBody();

        assertTrue(winners.length <= allMovies.length, 
                "Number of winners should be less than or equal to total movies");

        for (MovieDto winner : winners) {
            assertTrue(winner.getWinner(), "All returned winners should have winner = true");
        }
    }
}
