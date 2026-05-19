package com.outsera.pior_filme;

import com.outsera.pior_filme.dto.AwardIntervalResponseDto;
import com.outsera.pior_filme.dto.MovieDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PiorFilmeApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testGetAllMovies() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/movies"))
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, MovieDto.class);
		List<MovieDto> movies = objectMapper.readValue(content, listType);

		assertNotNull(movies);
		assertTrue(movies.size() > 0);
		
		MovieDto firstMovie = movies.get(0);
		assertNotNull(firstMovie.getYear());
		assertNotNull(firstMovie.getTitle());
		assertNotNull(firstMovie.getStudios());
		assertNotNull(firstMovie.getProducers());
		assertNotNull(firstMovie.getWinner());
	}

	@Test
	void testGetWinners() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/movies/winners"))
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, MovieDto.class);
		List<MovieDto> winners = objectMapper.readValue(content, listType);

		assertNotNull(winners);
		assertTrue(winners.size() > 0);
		
		for (MovieDto movie : winners) {
			assertTrue(movie.getWinner());
		}
	}

	@Test
	void testGetProducersWithAwardIntervals() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/movies/producers/intervals"))
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		AwardIntervalResponseDto response = objectMapper.readValue(content, AwardIntervalResponseDto.class);

		assertNotNull(response);
		assertNotNull(response.getMin());
		assertNotNull(response.getMax());
		assertTrue(response.getMin().size() > 0);
		assertTrue(response.getMax().size() > 0);

		Integer minInterval = response.getMin().get(0).getInterval();
		for (var interval : response.getMin()) {
			assertEquals(minInterval, interval.getInterval());
			assertTrue(interval.getPreviousWin() < interval.getFollowingWin());
			assertEquals(minInterval, interval.getFollowingWin() - interval.getPreviousWin());
		}

		Integer maxInterval = response.getMax().get(0).getInterval();
		for (var interval : response.getMax()) {
			assertEquals(maxInterval, interval.getInterval());
			assertTrue(interval.getPreviousWin() < interval.getFollowingWin());
			assertEquals(maxInterval, interval.getFollowingWin() - interval.getPreviousWin());
		}

		assertTrue(minInterval <= maxInterval);
	}

	@Test
	void testDataLoadedCorrectly() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/movies"))
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, MovieDto.class);
		List<MovieDto> movies = objectMapper.readValue(content, listType);

		boolean hasMoviesFrom1980 = movies.stream().anyMatch(m -> m.getYear() == 1980);
		assertTrue(hasMoviesFrom1980, "Should have movies from 1980");

		boolean hasWinners = movies.stream().anyMatch(MovieDto::getWinner);
		assertTrue(hasWinners, "Should have at least one winner");
	}

	@Test
	void testProducersHaveNames() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/movies"))
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, MovieDto.class);
		List<MovieDto> movies = objectMapper.readValue(content, listType);

		for (MovieDto movie : movies) {
			assertTrue(movie.getProducers().size() > 0, "Movie should have at least one producer: " + movie.getTitle());
			for (String producer : movie.getProducers()) {
				assertNotNull(producer);
				assertFalse(producer.isBlank(), "Producer name should not be blank");
			}
		}
	}

	@Test
	void testYearsAreCorrect() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/movies"))
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, MovieDto.class);
		List<MovieDto> movies = objectMapper.readValue(content, listType);

		for (MovieDto movie : movies) {
			assertTrue(movie.getYear() >= 1980, "Year should be 1980 or later: " + movie.getYear());
			assertTrue(movie.getYear() <= 2024, "Year should be 2024 or earlier: " + movie.getYear());
		}
	}

}

