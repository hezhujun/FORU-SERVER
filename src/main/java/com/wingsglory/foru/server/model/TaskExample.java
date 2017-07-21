package com.wingsglory.foru.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer offset;

    protected Integer rows;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public TaskExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPublisherIdIsNull() {
            addCriterion("publisher_id is null");
            return (Criteria) this;
        }

        public Criteria andPublisherIdIsNotNull() {
            addCriterion("publisher_id is not null");
            return (Criteria) this;
        }

        public Criteria andPublisherIdEqualTo(Integer value) {
            addCriterion("publisher_id =", value, "publisherId");
            return (Criteria) this;
        }

        public Criteria andPublisherIdNotEqualTo(Integer value) {
            addCriterion("publisher_id <>", value, "publisherId");
            return (Criteria) this;
        }

        public Criteria andPublisherIdGreaterThan(Integer value) {
            addCriterion("publisher_id >", value, "publisherId");
            return (Criteria) this;
        }

        public Criteria andPublisherIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("publisher_id >=", value, "publisherId");
            return (Criteria) this;
        }

        public Criteria andPublisherIdLessThan(Integer value) {
            addCriterion("publisher_id <", value, "publisherId");
            return (Criteria) this;
        }

        public Criteria andPublisherIdLessThanOrEqualTo(Integer value) {
            addCriterion("publisher_id <=", value, "publisherId");
            return (Criteria) this;
        }

        public Criteria andPublisherIdIn(List<Integer> values) {
            addCriterion("publisher_id in", values, "publisherId");
            return (Criteria) this;
        }

        public Criteria andPublisherIdNotIn(List<Integer> values) {
            addCriterion("publisher_id not in", values, "publisherId");
            return (Criteria) this;
        }

        public Criteria andPublisherIdBetween(Integer value1, Integer value2) {
            addCriterion("publisher_id between", value1, value2, "publisherId");
            return (Criteria) this;
        }

        public Criteria andPublisherIdNotBetween(Integer value1, Integer value2) {
            addCriterion("publisher_id not between", value1, value2, "publisherId");
            return (Criteria) this;
        }

        public Criteria andRecipientIdIsNull() {
            addCriterion("recipient_id is null");
            return (Criteria) this;
        }

        public Criteria andRecipientIdIsNotNull() {
            addCriterion("recipient_id is not null");
            return (Criteria) this;
        }

        public Criteria andRecipientIdEqualTo(Integer value) {
            addCriterion("recipient_id =", value, "recipientId");
            return (Criteria) this;
        }

        public Criteria andRecipientIdNotEqualTo(Integer value) {
            addCriterion("recipient_id <>", value, "recipientId");
            return (Criteria) this;
        }

        public Criteria andRecipientIdGreaterThan(Integer value) {
            addCriterion("recipient_id >", value, "recipientId");
            return (Criteria) this;
        }

        public Criteria andRecipientIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("recipient_id >=", value, "recipientId");
            return (Criteria) this;
        }

        public Criteria andRecipientIdLessThan(Integer value) {
            addCriterion("recipient_id <", value, "recipientId");
            return (Criteria) this;
        }

        public Criteria andRecipientIdLessThanOrEqualTo(Integer value) {
            addCriterion("recipient_id <=", value, "recipientId");
            return (Criteria) this;
        }

        public Criteria andRecipientIdIn(List<Integer> values) {
            addCriterion("recipient_id in", values, "recipientId");
            return (Criteria) this;
        }

        public Criteria andRecipientIdNotIn(List<Integer> values) {
            addCriterion("recipient_id not in", values, "recipientId");
            return (Criteria) this;
        }

        public Criteria andRecipientIdBetween(Integer value1, Integer value2) {
            addCriterion("recipient_id between", value1, value2, "recipientId");
            return (Criteria) this;
        }

        public Criteria andRecipientIdNotBetween(Integer value1, Integer value2) {
            addCriterion("recipient_id not between", value1, value2, "recipientId");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(String value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(String value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(String value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(String value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(String value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(String value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("state like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotLike(String value) {
            addCriterion("state not like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<String> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<String> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(String value1, String value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(String value1, String value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andContentIdIsNull() {
            addCriterion("content_id is null");
            return (Criteria) this;
        }

        public Criteria andContentIdIsNotNull() {
            addCriterion("content_id is not null");
            return (Criteria) this;
        }

        public Criteria andContentIdEqualTo(Integer value) {
            addCriterion("content_id =", value, "contentId");
            return (Criteria) this;
        }

        public Criteria andContentIdNotEqualTo(Integer value) {
            addCriterion("content_id <>", value, "contentId");
            return (Criteria) this;
        }

        public Criteria andContentIdGreaterThan(Integer value) {
            addCriterion("content_id >", value, "contentId");
            return (Criteria) this;
        }

        public Criteria andContentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("content_id >=", value, "contentId");
            return (Criteria) this;
        }

        public Criteria andContentIdLessThan(Integer value) {
            addCriterion("content_id <", value, "contentId");
            return (Criteria) this;
        }

        public Criteria andContentIdLessThanOrEqualTo(Integer value) {
            addCriterion("content_id <=", value, "contentId");
            return (Criteria) this;
        }

        public Criteria andContentIdIn(List<Integer> values) {
            addCriterion("content_id in", values, "contentId");
            return (Criteria) this;
        }

        public Criteria andContentIdNotIn(List<Integer> values) {
            addCriterion("content_id not in", values, "contentId");
            return (Criteria) this;
        }

        public Criteria andContentIdBetween(Integer value1, Integer value2) {
            addCriterion("content_id between", value1, value2, "contentId");
            return (Criteria) this;
        }

        public Criteria andContentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("content_id not between", value1, value2, "contentId");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherIsNull() {
            addCriterion("evaluation_to_publisher is null");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherIsNotNull() {
            addCriterion("evaluation_to_publisher is not null");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherEqualTo(Integer value) {
            addCriterion("evaluation_to_publisher =", value, "evaluationToPublisher");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherNotEqualTo(Integer value) {
            addCriterion("evaluation_to_publisher <>", value, "evaluationToPublisher");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherGreaterThan(Integer value) {
            addCriterion("evaluation_to_publisher >", value, "evaluationToPublisher");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherGreaterThanOrEqualTo(Integer value) {
            addCriterion("evaluation_to_publisher >=", value, "evaluationToPublisher");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherLessThan(Integer value) {
            addCriterion("evaluation_to_publisher <", value, "evaluationToPublisher");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherLessThanOrEqualTo(Integer value) {
            addCriterion("evaluation_to_publisher <=", value, "evaluationToPublisher");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherIn(List<Integer> values) {
            addCriterion("evaluation_to_publisher in", values, "evaluationToPublisher");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherNotIn(List<Integer> values) {
            addCriterion("evaluation_to_publisher not in", values, "evaluationToPublisher");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherBetween(Integer value1, Integer value2) {
            addCriterion("evaluation_to_publisher between", value1, value2, "evaluationToPublisher");
            return (Criteria) this;
        }

        public Criteria andEvaluationToPublisherNotBetween(Integer value1, Integer value2) {
            addCriterion("evaluation_to_publisher not between", value1, value2, "evaluationToPublisher");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientIsNull() {
            addCriterion("evaluation_to_recipient is null");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientIsNotNull() {
            addCriterion("evaluation_to_recipient is not null");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientEqualTo(Integer value) {
            addCriterion("evaluation_to_recipient =", value, "evaluationToRecipient");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientNotEqualTo(Integer value) {
            addCriterion("evaluation_to_recipient <>", value, "evaluationToRecipient");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientGreaterThan(Integer value) {
            addCriterion("evaluation_to_recipient >", value, "evaluationToRecipient");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientGreaterThanOrEqualTo(Integer value) {
            addCriterion("evaluation_to_recipient >=", value, "evaluationToRecipient");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientLessThan(Integer value) {
            addCriterion("evaluation_to_recipient <", value, "evaluationToRecipient");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientLessThanOrEqualTo(Integer value) {
            addCriterion("evaluation_to_recipient <=", value, "evaluationToRecipient");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientIn(List<Integer> values) {
            addCriterion("evaluation_to_recipient in", values, "evaluationToRecipient");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientNotIn(List<Integer> values) {
            addCriterion("evaluation_to_recipient not in", values, "evaluationToRecipient");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientBetween(Integer value1, Integer value2) {
            addCriterion("evaluation_to_recipient between", value1, value2, "evaluationToRecipient");
            return (Criteria) this;
        }

        public Criteria andEvaluationToRecipientNotBetween(Integer value1, Integer value2) {
            addCriterion("evaluation_to_recipient not between", value1, value2, "evaluationToRecipient");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNull() {
            addCriterion("gmt_create is null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNotNull() {
            addCriterion("gmt_create is not null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateEqualTo(Date value) {
            addCriterion("gmt_create =", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotEqualTo(Date value) {
            addCriterion("gmt_create <>", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThan(Date value) {
            addCriterion("gmt_create >", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_create >=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThan(Date value) {
            addCriterion("gmt_create <", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThanOrEqualTo(Date value) {
            addCriterion("gmt_create <=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIn(List<Date> values) {
            addCriterion("gmt_create in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotIn(List<Date> values) {
            addCriterion("gmt_create not in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateBetween(Date value1, Date value2) {
            addCriterion("gmt_create between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotBetween(Date value1, Date value2) {
            addCriterion("gmt_create not between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}