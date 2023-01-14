package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.Dialog;
import ru.skillbox.model.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Long> {

    @Query("select d from dialogs d where d.companion1 = :person or d.companion2 = :person")
    List<Dialog> findAllDialogsForPerson(@Param("person") Person person);

    @Query("select d from dialogs d where d.companion1 = :person1 and d.companion2 = :person2" +
            " or d.companion1 = :person2 and d.companion2 = :person1")
    Optional<Dialog> findDialogByCompanions(@Param("person1") Person person1,
                                            @Param("person2") Person person2);
}
