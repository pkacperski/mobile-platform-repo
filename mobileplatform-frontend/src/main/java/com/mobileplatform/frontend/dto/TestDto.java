package com.mobileplatform.frontend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class TestDto implements Serializable  {
    private Integer id;
    private String name;
}
