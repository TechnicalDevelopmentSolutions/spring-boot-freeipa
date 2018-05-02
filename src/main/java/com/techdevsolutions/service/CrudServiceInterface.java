package com.techdevsolutions.service;

import com.techdevsolutions.beans.Filter;
import com.techdevsolutions.beans.Search;

import java.util.List;

public interface CrudServiceInterface<T> {
    List<T> search(Search search) throws Exception;
    List<T> get(Filter search) throws Exception;
    T get(Integer id) throws Exception;
    T create(T item) throws Exception;
    void remove(Integer id) throws Exception;
    void delete(Integer id) throws Exception;
    T update(T item) throws Exception;
}