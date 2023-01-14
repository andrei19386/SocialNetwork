package ru.skillbox.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.model.PostComment;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PostCommentSpecification implements Specification<PostComment> {

    public Specification<PostComment> getCommentsByPostId(Long postId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("post")
                .get("id"), postId));
    }

    public Specification<PostComment> getCommentsByIsDelete() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDelete"), false));
    }

    public Specification<PostComment> getCommentsByParentId(Long parentId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("parentId"), parentId));
    }

    public Specification<PostComment> getCommentsByAuthorIsNotDelete() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("person")
                .get("isEnabled"), true));
    }


    @Override
    public Specification<PostComment> and(Specification<PostComment> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<PostComment> or(Specification<PostComment> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<PostComment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
