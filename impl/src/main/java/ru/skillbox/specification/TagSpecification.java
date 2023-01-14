package ru.skillbox.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.model.Tag;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TagSpecification implements Specification<Tag> {

    public Specification<Tag> getTagsByName(String tag) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("tag")), tag));
    }

    @Override
    public Specification<Tag> and(Specification<Tag> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Tag> or(Specification<Tag> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }

}
