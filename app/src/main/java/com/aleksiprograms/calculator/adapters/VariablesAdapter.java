package com.aleksiprograms.calculator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aleksiprograms.calculator.R;
import com.aleksiprograms.calculator.tools.Variable;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VariablesAdapter extends ArrayAdapter<Variable> {

    public VariablesAdapter(@NonNull Context context, ArrayList<Variable> items) {
        super(context, R.layout.history_row, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.variables_row, null);
        Variable item = getItem(position);
        TextView tvRowName = (TextView) view.findViewById(R.id.variableRowName);
        TextView tvRowValue = (TextView) view.findViewById(R.id.variableRowValue);
        tvRowName.setText(item.getName());
        tvRowValue.setText(String.valueOf(item.getValue()));
        return view;
    }
}