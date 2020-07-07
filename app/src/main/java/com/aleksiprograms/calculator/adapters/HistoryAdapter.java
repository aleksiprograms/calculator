package com.aleksiprograms.calculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aleksiprograms.calculator.R;
import com.aleksiprograms.calculator.tools.Equation;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<Equation> {

    public HistoryAdapter(@NonNull Context context, ArrayList<Equation> items) {
        super(context, R.layout.history_row, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.history_row, null);
        Equation item = getItem(position);
        TextView tvRowCalculation = (TextView) view.findViewById(R.id.historyRowExpression);
        TextView tvRowResult = (TextView) view.findViewById(R.id.historyRowResult);
        tvRowCalculation.setText(item.getExpression());
        tvRowResult.setText(item.getResult());
        if (item.getResult().contains("ERROR")) {
            tvRowResult.setTextColor(getContext().getResources().getColor(R.color.colorBad));
        } else {
            tvRowResult.setTextColor(getContext().getResources().getColor(R.color.colorGood));
        }
        return view;
    }
}