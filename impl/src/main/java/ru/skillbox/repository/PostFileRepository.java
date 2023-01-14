package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.PostFile;

import java.util.Optional;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Long> {
    Optional<PostFile> findByPath(String path);
}
