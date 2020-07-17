package com.example.myapplication.presenter.main;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.AutoCompleteTextView;
import com.example.myapplication.R;
import com.example.myapplication.domain.Result;
import com.example.myapplication.presenter.main.adapter.SuggestionSearchAdapter;
import com.example.myapplication.domain.Filter;
import com.example.myapplication.domain.FilterSearch;
import com.example.myapplication.presenter.main.interfaces.IHomeView;
import com.example.myapplication.presenter.main.adapter.PaginationListener;
import com.example.myapplication.presenter.main.adapter.PresenterHome;
import com.example.myapplication.presenter.main.adapter.RecyclerAdapter;
import com.example.myapplication.presenter.dialogs.DialogFragmentLoading;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IHomeView, SearchView.OnQueryTextListener, SuggestionSearchAdapter.OnSuggestionItemClick {

    private Context ctx;
    private DialogFragment dialogFragment;
    private SearchView searchView;
    private Chip chipSelection;
    private SuggestionSearchAdapter suggestionSearchAdapter;
    private RecyclerView rcvItems;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter adapter;
    private List<Result> mList;
    private PresenterHome presenterHome;
    private static int PAGE_START = 1;
    private int TOTAL_PAGE = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private FilterSearch filterSearch;
    private String querySearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        filterSearch = new FilterSearch();
        presenterHome = new PresenterHome(this, this);

        initViews();

    }

    private void initViews() {

        initChips();
        initSearchView();
        initRecyclerView();
        //loadStartPage(1,filterSearch,querySearch);
    }

    private void initRecyclerView() {
        rcvItems = findViewById(R.id.rcv);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvItems.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(this);
        rcvItems.setAdapter(adapter);
        rcvItems.setItemAnimator(new DefaultItemAnimator());
        rcvItems.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage < TOTAL_PAGE)
                            loadNextPage(currentPage);
                    }
                }, 1000);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void initSearchView() {
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        autoCompleteTextView.setThreshold(0);
        suggestionSearchAdapter = new SuggestionSearchAdapter(this, refreshCursor(), this);
        searchView.setSuggestionsAdapter(suggestionSearchAdapter);
    }

    private void initChips() {
        ChipGroup chipGroup = findViewById(R.id.chipGroup);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, @IdRes int checkedId) {

                chipSelection = group.findViewById(checkedId);

            }
        });
    }


    public void loadStartPage(int page, @Filter.Type FilterSearch filterSearch, String querySearch) {
        presenterHome.searchQuery(page, filterSearch, querySearch);

    }

    private void loadNextPage(int page) {
        adapter.removeLoading();
        isLoading = false;
        presenterHome.searchQuery(page, filterSearch, querySearch);


    }


    @Override
    public void showItems(int pages, List<Result> mListItems) {
        adapter.addItems(mListItems);

        if (currentPage == 1) {
            TOTAL_PAGE = pages;
            if (currentPage < TOTAL_PAGE) {
                adapter.addLoading();
            } else
                isLastPage = true;
        } else {
            if (currentPage < TOTAL_PAGE)
                adapter.addLoading();
            else isLastPage = true;

        }
    }

    @Override
    public void showProgressDialog() {
        if (currentPage == 1) {
            dialogFragment = DialogFragmentLoading.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "dialog");

        }
    }

    @Override
    public void dismissProgressDialog() {
        if (currentPage == 1) {
            if (dialogFragment.isVisible()) {
                dialogFragment.dismiss();
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.isEmpty()) {
            querySearch = query;
            adapter.clear();
            loadFilter();
            presenterHome.saveSuggestion(query);
            searchView.setQuery("", false);
            searchView.clearFocus();
            suggestionSearchAdapter = new SuggestionSearchAdapter(this, refreshCursor(), this);
            searchView.setSuggestionsAdapter(suggestionSearchAdapter);
            PAGE_START = 1;
            currentPage = PAGE_START;
            loadStartPage(PAGE_START, filterSearch, query);
        }
        return true;
    }

    private void loadFilter() {

        String name = "";

        filterSearch.clearFilter();
        if (chipSelection != null) {
            String nameViewFull = getResources().getResourceName(chipSelection.getId());
            name = nameViewFull.substring(nameViewFull.lastIndexOf("/") + 1);
        }

        switch (name) {
            case "chipStatus":
                filterSearch.addElement(Filter.STATUS);
                break;

            case "chipGender":
                filterSearch.addElement(Filter.GENDER);
                break;

            case "chipName":
                filterSearch.addElement(Filter.NAME);
                break;

            case "chipSpecies":
                filterSearch.addElement(Filter.SPECIE);
                break;

            default:
                filterSearch.addElement(Filter.NAME);
                break;
        }


    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    public Cursor refreshCursor() {

        return presenterHome.getSuggestions();
    }

    @Override
    public void onClickItem(String itemSuggestion) {
        searchView.setQuery(itemSuggestion, false);
    }


}
