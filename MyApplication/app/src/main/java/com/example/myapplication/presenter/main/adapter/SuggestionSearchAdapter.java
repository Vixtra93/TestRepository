package com.example.myapplication.presenter.main.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.example.myapplication.R;
import com.example.myapplication.domain.SuggestionsDatabase;

public class SuggestionSearchAdapter extends CursorAdapter {

    private OnSuggestionItemClick onSuggestionItemClick;

    public SuggestionSearchAdapter(Context context, Cursor cursor, OnSuggestionItemClick onSuggestionItemClick) {
        super(context, cursor, 0);
        this.onSuggestionItemClick = onSuggestionItemClick;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final TextView tvBody = (TextView) view.findViewById(R.id.tvBody);

        int indexColumnSuggestion = cursor.getColumnIndex(SuggestionsDatabase.FIELD_SUGGESTION);

        String body = cursor.getString(indexColumnSuggestion);

        tvBody.setText(body);

        tvBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSuggestionItemClick.onClickItem(tvBody.getText().toString());
            }
        });

    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        return super.swapCursor(newCursor);
    }


    public interface OnSuggestionItemClick {
        void onClickItem(String itemSuggestion);
    }
}
