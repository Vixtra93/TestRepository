package com.example.myapplication.presenter.main.adapter;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.example.myapplication.domain.Response;
import com.example.myapplication.domain.SuggestionsDatabase;
import com.example.myapplication.data.ItemService;
import com.example.myapplication.data.ApiClient;
import com.example.myapplication.domain.Filter;
import com.example.myapplication.domain.FilterSearch;
import com.example.myapplication.presenter.main.interfaces.IHomePresenter;
import com.example.myapplication.presenter.main.interfaces.IHomeView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


public class PresenterHome implements IHomePresenter {

    private Context ctx;
    private IHomeView view;
    private SuggestionsDatabase suggestionsDatabase;

    public PresenterHome(Context ctx, IHomeView view) {
        this.ctx = ctx;
        this.view = view;
        suggestionsDatabase = new SuggestionsDatabase(ctx);
    }


    @Override
    public void searchQuery(int page, FilterSearch filterSearch, String querySearch) {

        Map queryMap = queryToSearch(filterSearch, querySearch);

        Call call = ApiClient.getInstance().createService(ctx, ItemService.class).getItems(page, queryMap);
        view.showProgressDialog();
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response != null && response.isSuccessful()) {

                    Response mItems = response.body();

                    view.showItems(mItems.getInfo().getPages(), mItems.getResults());
                } else {
                    Toast.makeText(ctx, "La busqueda no arrojo resultados.", Toast.LENGTH_SHORT).show();
                }

                view.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(ctx, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();

                view.dismissProgressDialog();
            }
        });
    }

    @Override
    public Cursor getSuggestions() {
        return suggestionsDatabase.getSuggestions();
    }

    @Override
    public void saveSuggestion(String suggestion) {
        suggestionsDatabase.insertSuggestion(suggestion);
    }

    private Map queryToSearch(FilterSearch filterSearch, String textQuerySearch) {

        Map<String, String> mapQuery = new HashMap<>();


        if (filterSearch != null) {
            for (String filter : filterSearch.getListFilter()) {
                mapQuery.put(filter, textQuerySearch);
            }
        } else {
            mapQuery.put(Filter.NAME, textQuerySearch);
        }


        return mapQuery;

    }
}
