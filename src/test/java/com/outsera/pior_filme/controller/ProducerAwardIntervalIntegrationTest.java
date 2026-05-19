package com.outsera.pior_filme.controller;

import com.outsera.pior_filme.dto.AwardIntervalDto;
import com.outsera.pior_filme.dto.AwardIntervalResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProducerAwardIntervalIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testProducersDataFromCSV() {
        ResponseEntity<AwardIntervalResponseDto> response = restTemplate.getForEntity(
                "/api/movies/producers/intervals", AwardIntervalResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        AwardIntervalResponseDto responseDto = response.getBody();
        
        List<AwardIntervalDto> minProducers = responseDto.getMin();
        assertNotNull(minProducers);
        assertFalse(minProducers.isEmpty(), "Should have at least one producer with minimum interval");

        List<AwardIntervalDto> maxProducers = responseDto.getMax();
        assertNotNull(maxProducers);
        assertFalse(maxProducers.isEmpty(), "Should have at least one producer with maximum interval");

        Integer minValue = minProducers.get(0).getInterval();
        Integer maxValue = maxProducers.get(0).getInterval();
        
        assertTrue(minValue > 0, "Minimum interval should be greater than 0");
        assertTrue(maxValue > 0, "Maximum interval should be greater than 0");
        assertTrue(minValue <= maxValue, "Minimum should be less than or equal to maximum");

        System.out.println("Minimum intervals (fastest consecutive wins):");
        minProducers.forEach(p -> System.out.println(
                String.format("  Producer: %s, Interval: %d, Years: %d-%d", 
                    p.getProducer(), p.getInterval(), p.getPreviousWin(), p.getFollowingWin())
        ));

        System.out.println("\nMaximum intervals (slowest consecutive wins):");
        maxProducers.forEach(p -> System.out.println(
                String.format("  Producer: %s, Interval: %d, Years: %d-%d", 
                    p.getProducer(), p.getInterval(), p.getPreviousWin(), p.getFollowingWin())
        ));
    }

    @Test
    void testProducerYearConsistency() {
        ResponseEntity<AwardIntervalResponseDto> response = restTemplate.getForEntity(
                "/api/movies/producers/intervals", AwardIntervalResponseDto.class);

        AwardIntervalResponseDto responseDto = response.getBody();

        for (AwardIntervalDto producer : responseDto.getMin()) {
            assertTrue(producer.getPreviousWin() < producer.getFollowingWin(),
                    "Previous win year should be before following win year");
            assertTrue(producer.getFollowingWin() > 1900,
                    "Year should be reasonable (after 1900)");
            assertTrue(producer.getFollowingWin() <= 2100,
                    "Year should be reasonable (before 2100)");
        }

        for (AwardIntervalDto producer : responseDto.getMax()) {
            assertTrue(producer.getPreviousWin() < producer.getFollowingWin(),
                    "Previous win year should be before following win year");
            assertTrue(producer.getFollowingWin() > 1900,
                    "Year should be reasonable (after 1900)");
            assertTrue(producer.getFollowingWin() <= 2100,
                    "Year should be reasonable (before 2100)");
        }
    }

    @Test
    void testMinMaxIntervalValues() {
        ResponseEntity<AwardIntervalResponseDto> response = restTemplate.getForEntity(
                "/api/movies/producers/intervals", AwardIntervalResponseDto.class);

        AwardIntervalResponseDto responseDto = response.getBody();

        Integer minValue = responseDto.getMin().get(0).getInterval();
        for (AwardIntervalDto dto : responseDto.getMin()) {
            assertEquals(minValue, dto.getInterval(),
                    "All minimum intervals should have the same value");
        }

        Integer maxValue = responseDto.getMax().get(0).getInterval();
        for (AwardIntervalDto dto : responseDto.getMax()) {
            assertEquals(maxValue, dto.getInterval(),
                    "All maximum intervals should have the same value");
        }

        assertLess(minValue, maxValue, "Minimum interval should be less than maximum interval");
    }

    private void assertLess(Integer min, Integer max, String message) {
        assertTrue(min < max, message);
    }
}
