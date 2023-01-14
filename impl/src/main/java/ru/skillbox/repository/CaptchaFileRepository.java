package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.CaptchaFile;

import java.util.Optional;

@Repository
public interface CaptchaFileRepository extends JpaRepository<CaptchaFile, Long> {
    Optional<CaptchaFile> findByName(String name);
}

