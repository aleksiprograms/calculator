package com.aleksiprograms.calculator.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DATABASE_CALCULATOR";
    private static final String TABLE_EQUATIONS = "TABLE_EQUATIONS";
    private static final String TABLE_VARIABLES = "TABLE_VARIABLES";
    private static final String COLUMN_EQUATION_ID = "COLUMN_EQUATION_ID";
    private static final String COLUMN_EQUATION_EXPRESSION = "COLUMN_EQUATION_EXPRESSION";
    private static final String COLUMN_EQUATION_RESULT = "COLUMN_EQUATION_RESULT";
    private static final String COLUMN_VARIABLES_ID = "COLUMN_VARIABLES_ID";
    private static final String COLUMN_VARIABLES_NAME = "COLUMN_VARIABLES_NAME";
    private static final String COLUMN_VARIABLES_VALUE = "COLUMN_VARIABLES_VALUE";

    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

    public static long insertEquation(Equation equation, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EQUATION_EXPRESSION, equation.getExpression());
        values.put(COLUMN_EQUATION_RESULT, equation.getResult());
        long i = db.insert(TABLE_EQUATIONS, null, values);
        db.close();
        return i;
    }

    public static ArrayList<Equation> getAllEquations(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        ArrayList<Equation> equations = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_EQUATIONS, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Equation equation = new Equation(
                    Long.parseLong(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2));
            equations.add(equation);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return equations;
    }

    public static int deleteAllEquations(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        int i = db.delete(TABLE_EQUATIONS, null, null);
        db.close();
        return i;
    }

    public static int deleteEquation(Equation equation, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        int i = db.delete(TABLE_EQUATIONS, COLUMN_EQUATION_ID + " = ?",
                new String[]{String.valueOf(equation.getId())});
        db.close();
        return i;
    }

    public static long insertVariable(Variable variable, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VARIABLES_NAME, variable.getName());
        values.put(COLUMN_VARIABLES_VALUE, variable.getValue());
        long i = db.insert(TABLE_VARIABLES, null, values);
        db.close();
        return i;
    }

    public static ArrayList<Variable> getAllVariables(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        ArrayList<Variable> variables = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_VARIABLES, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Variable note = new Variable(
                    Long.parseLong(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2));
            variables.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return variables;
    }

    public static int deleteAllVariables(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        int i = db.delete(TABLE_VARIABLES, null, null);
        db.close();
        return i;
    }

    public static int updateVariable(Variable variable, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VARIABLES_NAME, variable.getName());
        values.put(COLUMN_VARIABLES_VALUE, variable.getValue());
        int i = db.update(TABLE_VARIABLES, values, COLUMN_VARIABLES_ID + " = ?",
                new String[]{String.valueOf(variable.getId())});
        db.close();
        return i;
    }

    public static int deleteVariable(Variable variable, Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        int i = db.delete(TABLE_VARIABLES, COLUMN_VARIABLES_ID + " = ?",
                new String[]{String.valueOf(variable.getId())});
        db.close();
        return i;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_EQUATIONS + "("
                + COLUMN_EQUATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EQUATION_EXPRESSION + " TEXT,"
                + COLUMN_EQUATION_RESULT + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_VARIABLES + "("
                + COLUMN_VARIABLES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_VARIABLES_NAME + " TEXT,"
                + COLUMN_VARIABLES_VALUE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VARIABLES);
        onCreate(db);
    }
}