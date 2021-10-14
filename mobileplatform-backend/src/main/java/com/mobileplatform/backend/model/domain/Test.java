package com.mobileplatform.backend.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test")
@SequenceGenerator(name = "seq_test", sequenceName = "seq_test", allocationSize = 1)
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_test")
    private Integer id;

    private String name;
}
