package com.example.myapplication.domain;

import java.util.ArrayList;
import java.util.List;

public class FilterSearch {

    private List<String> mListFilter;

    public FilterSearch() {
        mListFilter = new ArrayList<>();
    }

    public List<String> getListFilter() {
        return mListFilter;
    }

    public void setListFilter(List<String> mListFilter) {
        this.mListFilter = mListFilter;
    }

    public void addElement(@Filter.Type String filter) {

        if (mListFilter != null) {
            mListFilter.add(filter);
        }
    }

    public void clearFilter() {
        if (mListFilter != null) {
            mListFilter.clear();
        }
    }
}
