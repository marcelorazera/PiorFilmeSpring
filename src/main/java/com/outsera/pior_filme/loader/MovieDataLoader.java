package com.outsera.pior_filme.loader;

import com.outsera.pior_filme.model.Movie;
import com.outsera.pior_filme.model.Producer;
import com.outsera.pior_filme.repository.MovieRepository;
import com.outsera.pior_filme.repository.ProducerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class MovieDataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final ProducerRepository producerRepository;

    public MovieDataLoader(MovieRepository movieRepository, ProducerRepository producerRepository) {
        this.movieRepository = movieRepository;
        this.producerRepository = producerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() > 0) {
            return;
        }

        Map<String, Producer> producerCache = new HashMap<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dados/Movielist.csv");
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            boolean isFirstLine = true;

            while ((line = bufferedReader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                String[] fields = line.split(";");
                if (fields.length < 5) {
                    continue;
                }

                Integer year = Integer.parseInt(fields[0].trim());
                String title = fields[1].trim();
                String studios = fields[2].trim();
                String producersStr = fields[3].trim();
                Boolean winner = !fields[4].trim().isEmpty() && fields[4].trim().equalsIgnoreCase("yes");

                Movie movie = new Movie(year, title, studios, winner);

                // Parse producers
                String[] producersArray = producersStr.split(",\\s*and\\s*|,\\s*");
                for (String producerName : producersArray) {
                    String cleanedName = producerName.trim();
                    if (!cleanedName.isEmpty()) {
                        Producer producer = producerCache.computeIfAbsent(cleanedName, name -> {
                            Producer newProducer = new Producer(name);
                            return producerRepository.save(newProducer);
                        });
                        movie.addProducer(producer);
                    }
                }

                movieRepository.save(movie);
            }
        }
    }
}
