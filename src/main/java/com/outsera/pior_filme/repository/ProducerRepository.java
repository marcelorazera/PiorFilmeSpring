package com.outsera.pior_filme.repository;

import com.outsera.pior_filme.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

    Optional<Producer> findByName(String name);
}
