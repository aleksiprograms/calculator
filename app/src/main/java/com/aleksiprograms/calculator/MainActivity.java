package com.aleksiprograms.calculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.aleksiprograms.calculator.adapters.CustomPagerAdapter;
import com.aleksiprograms.calculator.fragments.CalculatorFragment;
import com.aleksiprograms.calculator.fragments.HistoryFragment;
import com.aleksiprograms.calculator.fragments.VariablesFragment;
import com.aleksiprograms.calculator.tools.DatabaseHelper;
import com.aleksiprograms.calculator.tools.Equation;
import com.aleksiprograms.calculator.tools.Variable;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class MainActivity
        extends AppCompatActivity
        implements CalculatorFragment.CalculatorListener, VariablesFragment.VariablesListener, HistoryFragment.HistoryListener {

    private static ViewPager viewPager;
    private CustomPagerAdapter pagerAdapter;
    private Menu optionsMenu;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(getResources().getString(R.string.titleCalculator));
        }

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new HistoryFragment(), getResources().getString(R.string.viewPagerTitleHistory));
        pagerAdapter.addFragment(new CalculatorFragment(), getResources().getString(R.string.viewPagerTitleCalculator));
        pagerAdapter.addFragment(new VariablesFragment(), getResources().getString(R.string.viewPagerTitleVariables));
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void sendEquationFromCalculatorToHistory(Equation equation) {
        HistoryFragment historyFragment = (HistoryFragment) pagerAdapter.getItem(0);
        historyFragment.receiveEquationFromCalculatorToHistory(equation);
    }

    @Override
    public void sendEquationFromHistoryToCalculator(Equation equation) {
        CalculatorFragment calculatorFragment = (CalculatorFragment) pagerAdapter.getItem(1);
        calculatorFragment.receiveEquationFromHistoryToCalculator(equation);
    }

    @Override
    public void sendEquationFromHistoryToVariables(Equation equation) {
        VariablesFragment variablesFragment = (VariablesFragment) pagerAdapter.getItem(2);
        variablesFragment.receiveEquationFromHistoryToVariables(equation);
    }

    @Override
    public void sendVariableFromVariablesToCalculator(Variable variable) {
        CalculatorFragment calculatorFragment = (CalculatorFragment) pagerAdapter.getItem(1);
        calculatorFragment.receiveVariableFromVariablesToCalculator(variable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        optionsMenu = menu;
        if (sharedPreferences.getBoolean(getResources().getString(R.string.sharedPreferencesAngleRadians), true)) {
            optionsMenu.findItem(R.id.mainMenuAngle).setTitle(getResources().getString(R.string.mainMenuAngleTitleRadians));
        } else {
            optionsMenu.findItem(R.id.mainMenuAngle).setTitle(getResources().getString(R.string.mainMenuAngleTitleDegrees));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuAngleRadians:
                optionsMenu.findItem(R.id.mainMenuAngle).setTitle(getResources().getString(R.string.mainMenuAngleTitleRadians));
                sharedPreferences.edit().putBoolean(getResources().getString(R.string.sharedPreferencesAngleRadians), true).apply();
                return true;
            case R.id.mainMenuAngleDegrees:
                optionsMenu.findItem(R.id.mainMenuAngle).setTitle(getResources().getString(R.string.mainMenuAngleTitleDegrees));
                sharedPreferences.edit().putBoolean(getResources().getString(R.string.sharedPreferencesAngleRadians), false).apply();
                return true;
            case R.id.mainMenuClearHistory:
                DatabaseHelper.deleteAllEquations(getApplicationContext());
                HistoryFragment historyFragment = (HistoryFragment) pagerAdapter.getItem(0);
                historyFragment.clearHistory();
                return true;
            case R.id.mainMenuClearVariables:
                DatabaseHelper.deleteAllVariables(getApplicationContext());
                VariablesFragment variablesFragment = (VariablesFragment) pagerAdapter.getItem(2);
                variablesFragment.clearVariables();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void changePage(int position) {
        viewPager.setCurrentItem(position);
    }
}