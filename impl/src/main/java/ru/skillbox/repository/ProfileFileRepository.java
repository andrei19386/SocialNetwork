package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.ProfileFile;

import java.util.Optional;

@Repository
public interface ProfileFileRepository extends JpaRepository<ProfileFile, Long> {
    Optional<ProfileFile> findByPath(String path);
}
