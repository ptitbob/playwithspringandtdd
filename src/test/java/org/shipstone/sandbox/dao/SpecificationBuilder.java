package org.shipstone.sandbox.dao;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author François Robert
 */
public class SpecificationBuilder<E> {

  private final List<Predicate> predicateList;

  public SpecificationBuilder() {
    this.predicateList = new ArrayList<>();
  }

  public SpecificationBuilder predicate(Object value, Predicate predicateValue) {
    if (value != null) {
      predicateList.add(predicateValue);
    }
    return this;
  }

  public Specification<E> build() {
    return (root, criteriaQuery, criteriaBuilder) -> {
      if (predicateList.size() > 0) {
        Predicate predicate = predicateList.remove(0);
        for (Predicate nextPredicate : predicateList) {
          predicate = criteriaBuilder.and(nextPredicate, predicate);
        }
        return predicate;
      }
      return null;
    };
  }

}
