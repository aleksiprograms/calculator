package com.aleksiprograms.calculator.tools;

public class DoubleStack {

    private double[] data;
    private int top;

    public DoubleStack() {
        data = new double[100];
        top = -1;
    }

    public void push(double number) {
        data[++top] = number;
    }

    public double pop() {
        return data[top--];
    }

    public boolean empty() {
        return top == -1;
    }

    public void clear() {
        top = -1;
    }
}