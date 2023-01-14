package ru.skillbox.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "countries")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private List<City> cities;
}
