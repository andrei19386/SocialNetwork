package ru.skillbox.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.Post;
import ru.skillbox.model.PostComment;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long>,
        JpaSpecificationExecutor<PostComment> {
    Page<PostComment> findByPostAndIsDeleteAndParentId(Post post, Boolean isDelete,
                                                       Long parentId, Pageable pageable);
}
