package com.techdevsolutions.beans;


import java.util.Objects;

public class Search extends Filter {
    private String term = "";

    public Search() {
    }

    public Search(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "Search{" +
                "term='" + term + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Search search = (Search) o;
        return Objects.equals(term, search.term);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), term);
    }
}
