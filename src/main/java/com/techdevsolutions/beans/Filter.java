package com.techdevsolutions.beans;

import java.util.Objects;

public class Filter {
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";
    public static final String FILTER_LOGIC_AND = "and";
    public static final String FILTER_LOGIC_OR = "or";
    public static final String FILTER_LOGIC_NOT = "not";

    public static final Integer DEFAULT_SIZE = 10;
    public static final Integer DEFAULT_PAGE = 0;
    public static final String DEFAULT_ORDER = Filter.SORT_DESC;
    public static final String DEFAULT_SORT = "updatedDate";
    public static final String DEFAULT_FILTER_LOGIC = Filter.FILTER_LOGIC_AND;

    private Integer size = Filter.DEFAULT_SIZE;
    private Integer page = Filter.DEFAULT_PAGE;
    private String sort = Filter.DEFAULT_SORT;
    private String order = Filter.DEFAULT_ORDER;
    private String filters = "";
    private String filterLogic = Filter.FILTER_LOGIC_AND;

    public Filter() {
    }

    public Filter(Integer size, Integer page, String sort, String order, String filters, String filterLogic) {
        this.size = size;
        this.page = page;
        this.sort = sort;
        this.order = order;
        this.filters = filters;
        this.filterLogic = filterLogic;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getFilterLogic() {
        return filterLogic;
    }

    public void setFilterLogic(String filterLogic) {
        this.filterLogic = filterLogic;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "size=" + size +
                ", page=" + page +
                ", sort='" + sort + '\'' +
                ", order='" + order + '\'' +
                ", filters='" + filters + '\'' +
                ", filterLogic='" + filterLogic + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return Objects.equals(size, filter.size) &&
                Objects.equals(page, filter.page) &&
                Objects.equals(sort, filter.sort) &&
                Objects.equals(order, filter.order) &&
                Objects.equals(filters, filter.filters) &&
                Objects.equals(filterLogic, filter.filterLogic);
    }

    @Override
    public int hashCode() {

        return Objects.hash(size, page, sort, order, filters, filterLogic);
    }
}
