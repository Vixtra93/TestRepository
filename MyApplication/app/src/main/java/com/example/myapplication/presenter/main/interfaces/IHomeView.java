package com.example.myapplication.presenter.main.interfaces;

import com.example.myapplication.domain.Result;

import java.util.List;

public interface IHomeView {

    void showItems(int pages, List<Result> mListItems);

    void showProgressDialog();

    void dismissProgressDialog();
}
