package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByTitle(String title);
}
