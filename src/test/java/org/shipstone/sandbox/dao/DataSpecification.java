package org.shipstone.sandbox.dao;

import org.hibernate.cfg.NotYetImplementedException;
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
public class DataSpecification<T> implements Specification<T> {

  private final List<ValueFieldCondition> valueFieldConditionList;

  public DataSpecification() {
    valueFieldConditionList = new ArrayList<>();
  }

  public DataSpecification<T> condition(String field, ValueOperation operation, Object value) {
    if (value != null) {
      valueFieldConditionList.add(new ValueFieldCondition(field, operation, value));
    }
    return this;
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
    if (valueFieldConditionList.size() > 0) {
      ValueFieldCondition valueFieldCondition = valueFieldConditionList.remove(0);
      Predicate predicate = getPredicateFrom(valueFieldCondition, criteriaBuilder, root);
      for (ValueFieldCondition nextValueFieldCondition : valueFieldConditionList) {
        predicate = criteriaBuilder.and(predicate, getPredicateFrom(nextValueFieldCondition, criteriaBuilder, root));
      }
      return predicate;
    }
    return null;
  }

  private Predicate getPredicateFrom(ValueFieldCondition valueFieldCondition, CriteriaBuilder criteriaBuilder, Root<T> root) {
    switch (valueFieldCondition.Operation) {
      case EQUAL:
        return criteriaBuilder.equal(root.get(valueFieldCondition.field), valueFieldCondition.value);
      case LIKE:
        if (String.class.isAssignableFrom(valueFieldCondition.value.getClass())) {
          return criteriaBuilder.like(root.get(valueFieldCondition.field), "%" + (String) valueFieldCondition.value + "%");
        } else {
          throw new IllegalArgumentException("L'opération like ne peut se faire que sur une chaine");
        }
      default:
        throw new NotYetImplementedException("pas de chance ... Do it yourself");
    }
  }

  private class ValueFieldCondition {
    private final String field;
    private final ValueOperation Operation;
    private final Object value;

    public ValueFieldCondition(String field, ValueOperation operation, Object value) {
      this.field = field;
      Operation = operation;
      this.value = value;
    }
  }

  public enum ValueOperation {
    EQUAL, LIKE;
  }
}
