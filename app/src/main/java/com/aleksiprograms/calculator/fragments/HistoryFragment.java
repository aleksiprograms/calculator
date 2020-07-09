package com.aleksiprograms.calculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.aleksiprograms.calculator.MainActivity;
import com.aleksiprograms.calculator.R;
import com.aleksiprograms.calculator.adapters.HistoryAdapter;
import com.aleksiprograms.calculator.tools.DatabaseHelper;
import com.aleksiprograms.calculator.tools.Equation;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private HistoryListener historyListener;
    private static ArrayList<Equation> equationsList;
    private ListView listViewHistory;
    private ListAdapter listAdapterHistory;

    public HistoryFragment() {
    }

    public interface HistoryListener {
        void sendEquationFromHistoryToCalculator(Equation equation);
        void sendEquationFromHistoryToVariables(Equation equation);
    }

    public void receiveEquationFromCalculatorToHistory(Equation equation) {
        DatabaseHelper.insertEquation(equation, getContext());
        equationsList.add(equation);
        listViewHistory.invalidateViews();
        listToEnd();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            historyListener = (HistoryFragment.HistoryListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString());
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        equationsList = DatabaseHelper.getAllEquations(getContext());
        listViewHistory = (ListView) view.findViewById(R.id.historyListView);
        listAdapterHistory = new HistoryAdapter(getContext(), equationsList);
        listViewHistory.setAdapter(listAdapterHistory);
        listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                historyListener.sendEquationFromHistoryToCalculator(equationsList.get(position));
                MainActivity.changePage(1);
            }
        });
        registerForContextMenu(listViewHistory);

        return view;
    }

    private void listToEnd() {
        listViewHistory.post(new Runnable() {
            @Override
            public void run() {
                listViewHistory.setSelection(listAdapterHistory.getCount() - 1);
            }
        });
    }

    @Override
    public void onCreateContextMenu(
            ContextMenu contextMenu,
            View view,
            ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, view, menuInfo);
        if (view.getId() == R.id.historyListView) {
            MenuInflater menuInflater = getActivity().getMenuInflater();
            menuInflater.inflate(R.menu.context_menu_history, contextMenu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.contextMenuHistoryCopy:
                historyListener.sendEquationFromHistoryToCalculator(
                        equationsList.get(info.position));
                MainActivity.changePage(1);
                return true;
            case R.id.contextMenuHistoryMakeVariable:
                historyListener.sendEquationFromHistoryToVariables(
                        equationsList.get(info.position));
                MainActivity.changePage(2);
                return true;
            case R.id.contextMenuHistoryDelete:
                DatabaseHelper.deleteEquation(
                        equationsList.get(info.position).getId(), getContext());
                equationsList.remove(info.position);
                listViewHistory.invalidateViews();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void clearHistory() {
        equationsList.clear();
        listViewHistory.invalidateViews();
    }
}