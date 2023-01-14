package ru.skillbox.model;


import lombok.*;

import javax.persistence.*;

@Entity(name = "cities")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Country country;
}

