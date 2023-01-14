package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.enums.StatusCode;
import ru.skillbox.model.Friendship;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friendship, Integer>, JpaSpecificationExecutor<Friendship> {


    Optional<Friendship> findBySrcPersonIdAndDstPersonId(Long srcPersonId, Long dstPersonId);

    Optional<List<Friendship>> findAllBySrcPersonIdOrDstPersonId(Long srcPersonId, Long dstPersonId);

    Optional<List<Friendship>> findAllByStatusCodeAndSrcPersonId(StatusCode statusCode, Long srcPerson);

    Optional<Friendship> findByDstPersonIdAndSrcPersonIdAndStatusCodeIs(Long currentUser, Long otherPersonId, StatusCode code);

    Optional<List<Friendship>> findAllByStatusCodeLikeAndDstPersonId(StatusCode code, Long id);
/*

    @Query(value = "SELECT * FROM friendship\n" +
            "WHERE status = 'REQUEST_TO'\n" +
            "AND dst_person_id = ?1;", nativeQuery = true)
    List<Friendship> findRequestToFriendshipToUserId(long id);

    @Query(value = "SELECT * FROM friendship\n" +
            "WHERE status = 'BLOCKED'\n" +
            "AND src_person_id = ?1;", nativeQuery = true)
    List<Friendship> findBlockedFriendshipForUserId(long id);
*/
}
