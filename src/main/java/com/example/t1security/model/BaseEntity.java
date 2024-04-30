package com.example.t1security.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
@Access(AccessType.FIELD)
public class BaseEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="IdGenerator")
    @GenericGenerator(name="IdGenerator", strategy="increment", parameters = {
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
    })
    private Long id;

}
