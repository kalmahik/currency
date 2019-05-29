package com.revolut.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.revolut.Currency;
import com.revolut.MainAdapter;
import com.revolut.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainPresenter presenter = new MainPresenter(this);
    private TextView emptyDataText;
    private RecyclerView recyclerView;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emptyDataText = findViewById(R.id.empty_data);
        recyclerView = findViewById(R.id.currency_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onStop() {
        presenter.onDestroy();
        super.onStop();
    }


    @Override
    public void showEmptyData() {
        emptyDataText.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        adapter = null;
    }

    @Override
    public void showBaseCurrency(Currency baseCurrency) {
        if (adapter != null) {
            adapter.updateBaseCurrency(baseCurrency);
        }
    }


    @Override
    public void showData(List<Currency> currencyList) {
        if (adapter == null) {
            adapter = new MainAdapter(presenter, currencyList, (item) -> {
                recyclerView.scrollToPosition(0);
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateCurrencyList(currencyList);
        }
        recyclerView.setVisibility(View.VISIBLE);
        emptyDataText.setVisibility(View.GONE);
    }

    public class MyLinearLayoutManager extends LinearLayoutManager {

        public MyLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.d("error", "need to refactoring");
            }
        }
    }
}

