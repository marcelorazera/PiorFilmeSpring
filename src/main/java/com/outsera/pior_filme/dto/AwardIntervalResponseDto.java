package com.outsera.pior_filme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AwardIntervalResponseDto {

    @JsonProperty("min")
    private List<AwardIntervalDto> min;

    @JsonProperty("max")
    private List<AwardIntervalDto> max;

    public AwardIntervalResponseDto() {
    }

    public AwardIntervalResponseDto(List<AwardIntervalDto> min, List<AwardIntervalDto> max) {
        this.min = min;
        this.max = max;
    }

    public List<AwardIntervalDto> getMin() {
        return min;
    }

    public void setMin(List<AwardIntervalDto> min) {
        this.min = min;
    }

    public List<AwardIntervalDto> getMax() {
        return max;
    }

    public void setMax(List<AwardIntervalDto> max) {
        this.max = max;
    }
}
