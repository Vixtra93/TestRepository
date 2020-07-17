package com.example.myapplication.presenter.main.interfaces;


import android.database.Cursor;

import com.example.myapplication.domain.Filter;
import com.example.myapplication.domain.FilterSearch;

public interface IHomePresenter {

    void searchQuery(int pages, @Filter.Type FilterSearch filter, String querySearch);

    Cursor getSuggestions();

    void saveSuggestion(String suggestion);
}
