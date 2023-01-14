package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.model.City;

public interface CityRepository extends JpaRepository<City, Long> {
    City findByTitle(String title);
}