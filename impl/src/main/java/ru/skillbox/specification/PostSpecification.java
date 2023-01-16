package ru.skillbox.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.enums.Type;
import ru.skillbox.model.Post;
import ru.skillbox.model.Tag;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


public class PostSpecification implements Specification<Post> {

    public Specification<Post> getPostsByDateFrom(Long dateFrom) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("time"), dateFrom));
    }

    public Specification<Post> getPostsByDateTo(Long dateTo) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("time"), dateTo));
    }

    public Specification<Post> getPostsByTitle(String word) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                "%" + word + "%"));
    }

    public Specification<Post> getPostsByText(String word) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("postText")),
                "%" + word + "%"));
    }

    public Specification<Post> getPostsByTitleOrText(String word) {
        return getPostsByTitle(word).or(getPostsByText(word));
    }

    public Specification<Post> getPostsByFirstName(String firstName) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("person").get("firstName")), firstName));
    }

    public Specification<Post> getPostsByLastName(String lastName) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("person").get("lastName")), lastName));
    }

    public Specification<Post> getPostsByPersonId(Long id) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("person").get("id"), id));
    }

    public Specification<Post> getPostsNotIsDeletePerson() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("person")
                .get("isEnabled"), true));
    }

    public Specification<Post> getPostsByPersonIds(List<Long> ids) {
        Specification<Post> specification = new PostSpecification();
        for (Long id : ids) {
            specification = specification.or(getPostsByPersonId(id));
        }
        return specification;
    }

    public Specification<Post> getPostsByTwoNames(String first, String last) {
        return getPostsByFirstName(first).and(getPostsByLastName(last));
    }

    public Specification<Post> getPostsByTag(Tag tag) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(tag, root.get("tags"));
    }

    public Specification<Post> getPostsByIsDelete(boolean isDelete) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDelete"), isDelete);
    }

    public Specification<Post> getPostsByType(Type type) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
    }

    public Specification<Post> getFalse(){
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(criteriaBuilder.literal(true));
    }

    @Override
    public Specification<Post> and(Specification<Post> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Post> or(Specification<Post> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
