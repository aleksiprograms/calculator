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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.aleksiprograms.calculator.MainActivity;
import com.aleksiprograms.calculator.R;
import com.aleksiprograms.calculator.adapters.VariablesAdapter;
import com.aleksiprograms.calculator.tools.DatabaseHelper;
import com.aleksiprograms.calculator.tools.Equation;
import com.aleksiprograms.calculator.tools.Variable;

import java.util.ArrayList;
import java.util.Objects;

public class VariablesFragment extends Fragment {

    private VariablesListener variablesListener;
    private static ArrayList<Variable> variablesList;
    private ListView listViewVariables;
    private ListAdapter listAdapterVariables;
    private EditText editTextName;
    private EditText editTextValue;
    private Equation equationFromHistory = null;
    private Variable variableToEdit = null;

    public VariablesFragment() {
    }

    public interface VariablesListener {
        void sendVariableFromVariablesToCalculator(Variable variable);
    }

    public void receiveEquationFromHistoryToVariables(Equation equation) {
        equationFromHistory = equation;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            variablesListener = (VariablesFragment.VariablesListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString());
        }
    }

    @Override
    public void onResume() {
        if (equationFromHistory != null) {
            editTextValue.setText(equationFromHistory.getResult());
            equationFromHistory = null;
        }
        super.onResume();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_variables, container, false);

        variablesList = DatabaseHelper.getAllVariables(getContext());
        listViewVariables = (ListView) view.findViewById(R.id.variablesListView);
        listAdapterVariables = new VariablesAdapter(getContext(), variablesList);
        listViewVariables.setAdapter(listAdapterVariables);
        listViewVariables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                variablesListener.sendVariableFromVariablesToCalculator(
                        variablesList.get(position));
                MainActivity.changePage(1);
            }
        });
        registerForContextMenu(listViewVariables);

        editTextName = (EditText) view.findViewById(R.id.variableEditTextName);
        editTextValue = (EditText) view.findViewById(R.id.variableEditTextValue);
        Button buttonAddVariable = (Button) view.findViewById(R.id.variableButtonSave);
        buttonAddVariable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(editTextName.getText()).equals("")) {
                    Toast.makeText(
                            getContext(),
                            getResources().getString(R.string.variablesEmptyName),
                            Toast.LENGTH_LONG)
                            .show();
                } else if (doesVariableNameExist(String.valueOf(editTextName.getText()))) {
                    Toast.makeText(
                            getContext(),
                            getResources().getString(R.string.variablesNameExist),
                            Toast.LENGTH_LONG)
                            .show();
                } else if (isVariableNameFunctionName(String.valueOf(editTextName.getText()))) {
                    Toast.makeText(
                            getContext(),
                            getResources().getString(R.string.variablesNameFunction),
                            Toast.LENGTH_LONG)
                            .show();
                } else if (String.valueOf(editTextValue.getText()).equals("")) {
                    Toast.makeText(
                            getContext(),
                            getResources().getString(R.string.variablesEmptyValue),
                            Toast.LENGTH_LONG)
                            .show();
                } else {
                    if (variableToEdit != null) {
                        variableToEdit.setName(String.valueOf(editTextName.getText()));
                        variableToEdit.setValue(String.valueOf(editTextValue.getText()));
                        DatabaseHelper.updateVariable(variableToEdit, getContext());
                        variableToEdit = null;
                    } else {
                        Variable variable = new Variable(
                                String.valueOf(editTextName.getText()),
                                String.valueOf(editTextValue.getText()));
                        DatabaseHelper.insertVariable(variable, getContext());
                        variablesList.add(variable);
                    }
                    listViewVariables.invalidateViews();
                    editTextName.setText("");
                    editTextValue.setText("");
                    listToEnd();
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateContextMenu(
            ContextMenu contextMenu,
            View view,
            ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, view, menuInfo);
        if (view.getId() == R.id.variablesListView) {
            MenuInflater menuInflater = Objects.requireNonNull(getActivity()).getMenuInflater();
            menuInflater.inflate(R.menu.context_menu_variables, contextMenu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.contextMenuVariablesUse:
                variablesListener.sendVariableFromVariablesToCalculator(
                        variablesList.get(info.position));
                MainActivity.changePage(1);
                return true;
            case R.id.contextMenuVariablesEdit:
                variableToEdit = variablesList.get(info.position);
                editTextName.setText(variableToEdit.getName());
                editTextValue.setText(variableToEdit.getValue());
                return true;
            case R.id.contextMenuVariablesDelete:
                DatabaseHelper.deleteVariable(variablesList.get(info.position), getContext());
                variablesList.remove(info.position);
                listViewVariables.invalidateViews();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void listToEnd() {
        listViewVariables.post(new Runnable() {
            @Override
            public void run() {
                listViewVariables.setSelection(listAdapterVariables.getCount() - 1);
            }
        });
    }

    public void clearVariables() {
        variablesList.clear();
        listViewVariables.invalidateViews();
    }

    private boolean doesVariableNameExist(String name) {
        for (int i = 0; i < variablesList.size(); i++) {
            if (variablesList.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVariableNameFunctionName(String name) {
        for (int i = 0; i < CalculatorFragment.FUNCTIONS.length; i++) {
            if (CalculatorFragment.FUNCTIONS[i][0].equals(name)) {
                return true;
            }
        }
        return false;
    }
}