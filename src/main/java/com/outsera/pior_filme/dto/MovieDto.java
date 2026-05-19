package com.outsera.pior_filme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;

public class MovieDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("title")
    private String title;

    @JsonProperty("studios")
    private String studios;

    @JsonProperty("producers")
    private Set<String> producers;

    @JsonProperty("winner")
    private Boolean winner;

    public MovieDto() {
    }

    public MovieDto(Long id, Integer year, String title, String studios, Set<String> producers, Boolean winner) {
        this.id = id;
        this.year = year;
        this.title = title;
        this.studios = studios;
        this.producers = producers;
        this.winner = winner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudios() {
        return studios;
    }

    public void setStudios(String studios) {
        this.studios = studios;
    }

    public Set<String> getProducers() {
        return producers;
    }

    public void setProducers(Set<String> producers) {
        this.producers = producers;
    }

    public Boolean getWinner() {
        return winner;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }
}
