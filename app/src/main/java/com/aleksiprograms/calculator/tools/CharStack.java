package com.aleksiprograms.calculator.tools;

public class CharStack {

    private char[] data;
    private int top;

    public CharStack() {
        data = new char[100];
        top = -1;
    }

    public void push(char character) {
        data[++top] = character;
    }

    public char pop() {
        if (top < 0) {
            return '(';
        }
        return data[top--];
    }

    public boolean empty() {
        return top == -1;
    }

    public char peek() {
        if (top < 0) {
            return '(';
        }
        return data[top];

    }

    public void clear() {
        top = -1;
    }
}