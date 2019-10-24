package com.aleksiprograms.calculator.tools;

public class Equation {

    private long id;
    private String expression;
    private String result;

    public Equation(String expression, String result) {
        this.expression = expression;
        this.result = result;
    }

    public Equation(long id, String expression, String result) {
        this.id = id;
        this.expression = expression;
        this.result = result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}