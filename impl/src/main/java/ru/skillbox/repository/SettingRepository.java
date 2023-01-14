package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.Settings;

@Repository
public interface SettingRepository extends JpaRepository<Settings, Long> {
}
