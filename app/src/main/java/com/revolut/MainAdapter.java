package com.revolut;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.revolut.mvp.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.BaseCurrencyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Currency item);
    }

    private MainPresenter presenter;
    private List<Currency> currencyList = new ArrayList<>();
    private final OnItemClickListener listener;


    public MainAdapter(MainPresenter presenter, List<Currency> currencyList, OnItemClickListener listener) {
        this.presenter = presenter;
        this.listener = listener;
        updateCurrencyList(currencyList);
    }

    @Override
    public BaseCurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View baseCurrencyViewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item_editable, parent, false);
                return new EditableCurrencyViewHolder(baseCurrencyViewItem);
            case 1:
            default:
                View currencyViewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false);
                return new CurrencyViewHolder(currencyViewItem);
        }
    }


    @Override
    public void onBindViewHolder(final BaseCurrencyViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                holder.bind(currencyList.get(position));
                break;

            case 1:
                ((CurrencyViewHolder) holder).bind(currencyList.get(position), position, listener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public void updateCurrencyList(List<Currency> currencyList) {
        List<Currency> tempList = new ArrayList<>();
        tempList.add(presenter.getBaseCurrency());
        tempList.addAll(currencyList);
        this.currencyList = tempList;
        notifyItemRangeChanged(1, currencyList.size());
    }

    public void updateBaseCurrency(Currency baseCurrency) {
        currencyList.add(0, baseCurrency);
        notifyItemChanged(0);
    }

    class BaseCurrencyViewHolder extends RecyclerView.ViewHolder {
        TextView currencyName;

        BaseCurrencyViewHolder(View itemView) {
            super(itemView);
            currencyName = itemView.findViewById(R.id.currency_name);

        }

        void bind(final Currency item) {
            currencyName.setText(item.getName());
        }
    }

    class EditableCurrencyViewHolder extends BaseCurrencyViewHolder {
        EditText currencyValue;

        EditableCurrencyViewHolder(View itemView) {
            super(itemView);
            currencyValue = itemView.findViewById(R.id.currency_value);
            currencyValue.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }

        public void bind(final Currency item) {
            super.bind(item);
            currencyValue.setText(String.format("%.02f", item.getValue()));
            currencyValue.setSelection(currencyValue.getText().length());
            currencyValue.requestFocus();
            currencyValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }


                @Override
                public void afterTextChanged(Editable s) {
                    presenter.onBaseCurrencyValueChanged(s.toString());
                }
            });
        }
    }

    class CurrencyViewHolder extends BaseCurrencyViewHolder {
        TextView currencyValue;

        CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyValue = itemView.findViewById(R.id.currency_value);
            currencyValue.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }

        void bind(final Currency item, int position, final OnItemClickListener listener) {
            super.bind(item);
            currencyValue.setText(String.format("%.02f", item.getValue()));
            currencyValue.clearFocus();
            itemView.setOnClickListener(v -> {
                listener.onItemClick(item);
                notifyItemMoved(position, 0);
                presenter.onBaseCurrencySelected(item);
            });
        }
    }
}