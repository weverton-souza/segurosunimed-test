package com.example.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String street;

    @NotEmpty
    @Column(nullable = false)
    private String neighborhood;

    @NotEmpty
    @Column(nullable = false)
    private String city;

    @NotEmpty
    @Size(min = 2, max = 2)
    @Column(nullable = false)
    private String state;

    @NotEmpty
    @Column(nullable = false)
    private String zip;

    @NotEmpty
    @Column(nullable = false)
    private String type;

}
