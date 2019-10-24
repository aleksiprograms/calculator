package com.aleksiprograms.calculator.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import com.aleksiprograms.calculator.R;
import com.aleksiprograms.calculator.tools.Calculator;
import com.aleksiprograms.calculator.tools.DatabaseHelper;
import com.aleksiprograms.calculator.tools.Equation;
import com.aleksiprograms.calculator.tools.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.fragment.app.Fragment;

public class CalculatorFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private EditText editTextCalculation;
    private GridLayout gridLayoutButtons;
    private final int ROWS = 6;
    private final int COLUMNS = 4;
    public static final String[][] FUNCTIONS = {
            {"asin", "z"},
            {"acos", "k"},
            {"atan", "d"},
            {"sin", "s"},
            {"cos", "c"},
            {"tan", "t"},
            {"sqrt", "r"},
            {"abs", "a"},
            {"log", "l"},
            {"ln", "n"}};

    public class BI {
        String text;
        String color;
        BI(String text, String color) {
            this.text = text;
            this.color = color;
        }
    }
    private final BI[][] BUTTON_INFO_PRIMARY = {
            {new BI("<-", "r"), new BI("->", "r"), new BI("<X", "r"), new BI("C", "r")},
            {new BI("7", "b"), new BI("8", "b"), new BI("9", "b"), new BI("/", "g")},
            {new BI("4", "b"), new BI("5", "b"), new BI("6", "b"), new BI("*", "g")},
            {new BI("1", "b"), new BI("2", "b"), new BI("3", "b"), new BI("-", "g")},
            {new BI(".", "g"), new BI("0", "b"), new BI("%", "g"), new BI("+", "g")},
            {new BI("MORE", "G"), new BI("(", "g"), new BI(")", "g"), new BI("=", "B")}};
    private final BI[][] BUTTON_INFO_SECONDARY = {
            {new BI("<-", "r"), new BI("->", "r"), new BI("<X", "r"), new BI("C", "r")},
            {new BI("asin()", "b"), new BI("acos()", "b"), new BI("atan()", "b"), new BI("/", "g")},
            {new BI("sin()", "b"), new BI("cos()", "b"), new BI("tan()", "b"), new BI("*", "g")},
            {new BI("ln()", "b"), new BI("log()", "b"), new BI("abs()", "b"), new BI("-", "g")},
            {new BI("sqrt()", "b"), new BI("^()", "b"), new BI("pi", "b"), new BI("+", "g")},
            {new BI("MORE", "G"), new BI("(", "g"), new BI(")", "g"), new BI("=", "B")}};
    private Button[][] buttons = new Button[ROWS][COLUMNS];
    private boolean primaryButtons = true;

    CalculatorListener calculatorListener;
    public interface CalculatorListener {
        void sendEquationFromCalculatorToHistory(Equation equation);
    }

    public CalculatorFragment() {}

    public void receiveEquationFromHistoryToCalculator(Equation equation) {
        editTextCalculation.getText().insert(editTextCalculation.getSelectionStart(), equation.getExpression());
    }

    public void receiveVariableFromVariablesToCalculator(Variable variable) {
        editTextCalculation.getText().insert(editTextCalculation.getSelectionStart(), variable.getName());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            calculatorListener = (CalculatorListener)getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.sharedPreferences), Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        editTextCalculation = (EditText)view.findViewById(R.id.calculatorEditTextExpression);
        editTextCalculation.requestFocus();
        gridLayoutButtons = (GridLayout)view.findViewById(R.id.calculatorGridLayoutButtons);
        gridLayoutButtons.setRowCount(ROWS);
        gridLayoutButtons.setColumnCount(COLUMNS);

        Button button;
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                button = new Button(getContext());
                final int finalRow = row;
                final int finalColumn = column;
                final Button finalButton = button;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!finalButton.getText().equals("=")) {
                            editTextCalculation.setHint("");
                        }

                        if (finalButton.getText().equals("<-")) {
                            if (editTextCalculation.getSelectionStart() != 0) {
                                editTextCalculation.setSelection(editTextCalculation.getSelectionStart() - 1);
                            }
                        } else if (finalButton.getText().equals("->")) {
                            if (editTextCalculation.getSelectionStart() != editTextCalculation.getText().length()) {
                                editTextCalculation.setSelection(editTextCalculation.getSelectionStart() + 1);
                            }
                        } else if (finalButton.getText().equals("<X")) {
                            if (editTextCalculation.getSelectionStart() != 0) {
                                editTextCalculation.getText().delete(editTextCalculation.getSelectionStart() - 1, editTextCalculation.getSelectionStart());
                            }
                        } else if (finalButton.getText().equals("C")) {
                            editTextCalculation.setText("");
                        } else if (finalButton.getText().equals("MORE")) {
                            changeButtons();
                        } else if (finalButton.getText().equals("=")) {
                            if (editTextCalculation.getText().length() != 0) {
                                String expression = String.valueOf(editTextCalculation.getText());
                                expression = variableNamesToValues(expression);
                                expression = functionsToShorter(expression);
                                Calculator calculator = new Calculator(expression, sharedPreferences.getBoolean(getResources().getString(R.string.sharedPreferencesAngleRadians), true));
                                calculatorListener.sendEquationFromCalculatorToHistory(new Equation(String.valueOf(editTextCalculation.getText()), calculator.getResult(0)));
                                editTextCalculation.setText("");
                                editTextCalculation.setHint(calculator.getResult(0));
                                if (calculator.getResult(0).contains("ERROR")) {
                                    editTextCalculation.setHintTextColor(getResources().getColor(R.color.colorBad));
                                } else {
                                    editTextCalculation.setHintTextColor(getResources().getColor(R.color.colorGood));
                                }
                            }
                        } else if (String.valueOf(finalButton.getText()).contains("()")) {
                            editTextCalculation.getText().insert(editTextCalculation.getSelectionStart(), finalButton.getText());
                            editTextCalculation.setSelection(editTextCalculation.getSelectionStart() - 1);
                        } else {
                            editTextCalculation.getText().insert(editTextCalculation.getSelectionStart(), finalButton.getText());
                        }
                    }
                });

                button.setText(BUTTON_INFO_PRIMARY[row][column].text);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                button.setTransformationMethod(null);
                if (BUTTON_INFO_PRIMARY[row][column].color.equals("B")) {
                    button.setTextColor(getResources().getColor(R.color.colorBlack));
                    button.setBackground(getResources().getDrawable(R.drawable.color_button_blue_fill));
                } else if (BUTTON_INFO_PRIMARY[row][column].color.equals("G")) {
                    button.setTextColor(getResources().getColor(R.color.colorBlack));
                    button.setBackground(getResources().getDrawable(R.drawable.color_button_green_fill));
                } else if (BUTTON_INFO_PRIMARY[row][column].color.equals("b")){
                    button.setTextColor(getResources().getColor(R.color.colorWhite));
                    button.setBackground(getResources().getDrawable(R.drawable.color_button_blue));
                } else if (BUTTON_INFO_PRIMARY[row][column].color.equals("g")){
                    button.setTextColor(getResources().getColor(R.color.colorWhite));
                    button.setBackground(getResources().getDrawable(R.drawable.color_button_green));
                } else if (BUTTON_INFO_PRIMARY[row][column].color.equals("r")){
                    button.setTextColor(getResources().getColor(R.color.colorWhite));
                    button.setBackground(getResources().getDrawable(R.drawable.color_button_red));
                }
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = pdTOpx(55);
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(column, 1.0f);
                if (column < COLUMNS - 1) {
                    if (row == ROWS - 1) {
                        params.setMargins(pdTOpx(0), pdTOpx(0), pdTOpx(6), pdTOpx(0));
                    } else {
                        params.setMargins(pdTOpx(0), pdTOpx(0), pdTOpx(6), pdTOpx(6));
                    }
                }
                button.setLayoutParams(params);
                gridLayoutButtons.addView(button);
                buttons[row][column] = button;
            }
        }

        return view;
    }

    private int pdTOpx(int pd) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                pd,
                getResources().getDisplayMetrics());
    }

    private String variableNamesToValues(String expression) {
        ArrayList<Variable> variables = DatabaseHelper.getAllVariables(getContext());
        Collections.sort(variables, new Comparator<Variable>() {
            @Override
            public int compare(Variable v1, Variable v2) {
                if (v1.getName().length() > v2.getName().length()) {
                    return -1;
                } else {
                    return v1.getName().compareTo(v2.getName());
                }
            }
        });
        for (int i = 0; i < variables.size(); i++) {
            expression = expression.replace(variables.get(i).getName(), variables.get(i).getValue());
        }
        return expression;
    }

    private String functionsToShorter(String expression) {
        for (String[] function : FUNCTIONS) {
            expression = expression.replace(function[0], function[1]);
        }
        return expression;
    }

    private void changeButtons() {
        BI[][] buttonLayout;
        if (primaryButtons) {
            buttonLayout = BUTTON_INFO_SECONDARY;
        } else {
            buttonLayout = BUTTON_INFO_PRIMARY;
        }
        primaryButtons = !primaryButtons;
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                buttons[row][column].setText(buttonLayout[row][column].text);
                if (buttonLayout[row][column].color.equals("B")) {
                    buttons[row][column].setTextColor(getResources().getColor(R.color.colorBlack));
                    buttons[row][column].setBackground(getResources().getDrawable(R.drawable.color_button_blue_fill));
                } else if (buttonLayout[row][column].color.equals("G")) {
                    buttons[row][column].setTextColor(getResources().getColor(R.color.colorBlack));
                    buttons[row][column].setBackground(getResources().getDrawable(R.drawable.color_button_green_fill));
                } else if (buttonLayout[row][column].color.equals("b")){
                    buttons[row][column].setTextColor(getResources().getColor(R.color.colorWhite));
                    buttons[row][column].setBackground(getResources().getDrawable(R.drawable.color_button_blue));
                } else if (buttonLayout[row][column].color.equals("g")){
                    buttons[row][column].setTextColor(getResources().getColor(R.color.colorWhite));
                    buttons[row][column].setBackground(getResources().getDrawable(R.drawable.color_button_green));
                } else if (buttonLayout[row][column].color.equals("r")){
                    buttons[row][column].setTextColor(getResources().getColor(R.color.colorWhite));
                    buttons[row][column].setBackground(getResources().getDrawable(R.drawable.color_button_red));
                }
            }
        }
    }
}