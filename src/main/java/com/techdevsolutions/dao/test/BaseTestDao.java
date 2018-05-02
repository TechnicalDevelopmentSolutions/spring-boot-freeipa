package com.techdevsolutions.dao.test;

import com.techdevsolutions.beans.Filter;
import com.techdevsolutions.beans.auditable.Auditable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseTestDao {
    protected Logger logger = Logger.getLogger(BaseTestDao.class.getName());

    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public BaseTestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Auditable> filterAuditable(Filter filter, String key, String value, List<Auditable> list,
            List<Auditable> db) throws Exception {
        Stream<Auditable> stream = db.stream();

        if (filter.getFilterLogic().equals(Filter.FILTER_LOGIC_AND)) {
            stream = ("id".equals(key)) ? stream.filter(item -> item.getId().equals(Integer.valueOf(value))) : stream;
            stream = ("name".equals(key)) ? stream.filter(item -> item.getName().equals(value)) : stream;
            stream = ("createdBy".equals(key)) ? stream.filter(item -> item.getCreatedBy().equals(value)) : stream;
            stream = ("createdDate".equals(key)) ? stream.filter(item -> item.getCreatedDate().equals(Long.valueOf(value))) : stream;
            stream = ("updatedBy".equals(key)) ? stream.filter(item -> item.getUpdatedBy().equals(value)) : stream;
            stream = ("updatedDate".equals(key)) ? stream.filter(item -> item.getUpdatedDate().equals(Long.valueOf(value))) : stream;
            stream = ("removed".equals(key)) ? stream.filter(item -> item.getRemoved().equals(value)) : stream;
            list = stream.collect(Collectors.toList());
        } else if (filter.getFilterLogic().equals(Filter.FILTER_LOGIC_NOT)) {
            stream = ("id".equals(key)) ? stream.filter(item -> !item.getId().equals(Integer.valueOf(value))) : stream;
            stream = ("name".equals(key)) ? stream.filter(item -> !item.getName().equals(value)) : stream;
            stream = ("createdBy".equals(key)) ? stream.filter(item -> !item.getCreatedBy().equals(value)) : stream;
            stream = ("createdDate".equals(key)) ? stream.filter(item -> !item.getCreatedDate().equals(Long.valueOf(value))) : stream;
            stream = ("updatedBy".equals(key)) ? stream.filter(item -> !item.getUpdatedBy().equals(value)) : stream;
            stream = ("updatedDate".equals(key)) ? stream.filter(item -> !item.getUpdatedDate().equals(Long.valueOf(value))) : stream;
            stream = ("removed".equals(key)) ? stream.filter(item -> !item.getRemoved().equals(value)) : stream;
            list = stream.collect(Collectors.toList());
        } else if (filter.getFilterLogic().equals(Filter.FILTER_LOGIC_OR)) {
            // TODO: OR logic...
            throw new Exception("OR Logic not implemented!");
        }

        return list;
    }

    public Comparator getAuditableComparator(Filter filter, Comparator<Auditable> comparator) {
        comparator = ("id".equals(filter.getSort())) ? Comparator.comparing(Auditable::getId) : comparator;
        comparator = ("name".equals(filter.getSort())) ? Comparator.comparing(Auditable::getName) : comparator;
        comparator = ("createdBy".equals(filter.getSort())) ? Comparator.comparing(Auditable::getCreatedBy) : comparator;
        comparator = ("createdDate".equals(filter.getSort())) ? Comparator.comparing(Auditable::getCreatedDate) : comparator;
        comparator = ("updatedBy".equals(filter.getSort())) ? Comparator.comparing(Auditable::getUpdatedBy) : comparator;
        comparator = ("updatedDate".equals(filter.getSort())) ? Comparator.comparing(Auditable::getUpdatedDate) : comparator;
        comparator = ("removed".equals(filter.getSort())) ? Comparator.comparing(Auditable::getRemoved) : comparator;
        return comparator;
    }
}
