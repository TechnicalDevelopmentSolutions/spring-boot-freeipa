package com.techdevsolutions.dao;

import com.techdevsolutions.beans.Filter;
import com.techdevsolutions.beans.Search;

import java.util.List;

public interface DaoCrudInterface<T> {
    List<T> search(Search search) throws Exception;
    List<T> get(Filter filter) throws Exception;
    T get(Integer id) throws Exception;
    T create(T item) throws Exception;
    void remove(Integer id) throws Exception;
    void delete(Integer id) throws Exception;
    T update(T item) throws Exception;
}
