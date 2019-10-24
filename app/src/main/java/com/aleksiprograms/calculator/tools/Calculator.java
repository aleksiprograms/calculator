package com.aleksiprograms.calculator.tools;

public class Calculator {

    private DoubleStack memory;
    private CharStack operators;
    private String postfix;
    private double numbers[];
    private String infix;
    private boolean radians;

    public Calculator(String expression, boolean radians) {
        memory = new DoubleStack();
        operators = new CharStack();
        numbers = new double[30];
        infix = expression;
        this.radians = radians;
        postfix = infixToPostfix(infix);
    }

    public String getResult(double x) {
        int i = 0;
        memory.clear();
        for(int n = 0; n < postfix.length(); n++) {
            char ch = postfix.charAt(n);
            switch(ch) {
                case 'x':
                    memory.push(x);
                    break;
                case '#':
                    memory.push(numbers[i++]);
                    break;
                case '+':
                    double b = memory.pop();
                    double a = memory.pop();
                    memory.push(a + b);
                    break;
                case '$':
                    memory.push(-memory.pop());
                    break;
                case '-':
                    b = memory.pop();
                    a = memory.pop();
                    memory.push(a - b);
                    break;
                case '*':
                    b = memory.pop();
                    a = memory.pop();
                    memory.push(a * b);
                    break;
                case '/':
                    b = memory.pop();
                    a = memory.pop();
                    memory.push(a / b);
                    break;
                case '^':
                    b = memory.pop();
                    a = memory.pop();
                    memory.push(Math.pow(a, b));
                    break;
                case '%':
                    b = memory.pop();
                    a = memory.pop();
                    memory.push(a % b);
                    break;
                case 'a':
                    memory.push(Math.abs(memory.pop()));
                    break;
                case 'r':
                    memory.push(Math.sqrt(memory.pop()));
                    break;
                case 'e':
                    memory.push(Math.exp(memory.pop()));
                    break;
                case 'l':
                    memory.push(Math.log(memory.pop()));
                    break;
                case 's':
                    if (radians)
                        memory.push(Math.sin(memory.pop()));
                    else
                        memory.push(Math.sin(Math.toRadians(memory.pop())));
                    break;
                case 'c':
                    if (radians)
                        memory.push(Math.cos(memory.pop()));
                    else
                        memory.push(Math.cos(Math.toRadians(memory.pop())));
                    break;
                case 't':
                    if (radians)
                        memory.push(Math.tan(memory.pop()));
                    else
                        memory.push(Math.tan(Math.toRadians(memory.pop())));
                    break;
                case 'z':
                    if (radians)
                        memory.push(Math.asin(memory.pop()));
                    else
                        memory.push(Math.asin(Math.toRadians(memory.pop())));
                    break;
                case 'k':
                    if (radians)
                        memory.push(Math.acos(memory.pop()));
                    else
                        memory.push(Math.acos(Math.toRadians(memory.pop())));
                    break;
                case 'd':
                    if (radians)
                        memory.push(Math.atan(memory.pop()));
                    else
                        memory.push(Math.atan(Math.toRadians(memory.pop())));
                    break;
            }
        }
        return String.valueOf(memory.pop());
    }

    private String infixToPostfix(String infix) {
        int n = 0;
        postfix = "";
        infix += " ";
        operators.clear();
        boolean firstTime = true;
        for(int i = 0; i < infix.length(); i++) {
            char ch = infix.charAt(i);
            if(ch == '-' && firstTime)
                ch = '$';
            if(ch != ' ')
                firstTime = false;
            if(isADigit(ch)) {
                int m = i + 1;
                while(isADigit(infix.charAt(m))) m++;
                numbers[n++] = convert(infix.substring(i,m));
                postfix += '#';
                i = m - 1;
            }

            else if(ch == 'x')
                postfix += ch;
            else if("+-*/^$".indexOf(ch)>=0){
                while(leftFirst(operators.peek(),ch))
                    postfix += operators.pop();
                operators.push(ch);
            }
            else if("arelsc".indexOf(ch)>=0){
                operators.push('(');
                operators.push(ch);
                i++;
                firstTime = true;
            }
            else if(ch == '('){
                operators.push(ch);
                firstTime = true;
            }
            else if(ch == ')') {
                while(operators.peek()!='(')
                    postfix += operators.pop();
                operators.pop();
            }
        }
        while(!operators.empty())
            postfix += operators.pop();
        return postfix;
    }

    private boolean leftFirst(char a, char b) {
        if(a =='^' && b=='^')
            return false;
        int r = rank(a);
        int s = rank(b);
        return r>=s;
    }

    private int rank(char ch) {
        switch(ch) {
            case'+': case'-':
                return 1;
            case'*': case'/':
                return 2;
            case'$':
                return 3;
            case'^':
                return 4;
            default:
                return 0;
        }
    }

    private boolean isADigit(char ch) {
        return (ch >= '0' && ch <= '9') || ch == '.';
    }

    public String toString() {
        return "Calculator: infix=" + getInfix() +
                " postfix=" + getPostfix();
    }

    public void setInfix(String in) {
        infix = in;
        postfix = infixToPostfix(in);
    }

    private String getInfix() {
        return infix;
    }

    private String getPostfix() {
        String result = "";
        int num = 0;
        for (int n = 0; n < postfix.length(); n++)
            if (postfix.charAt(n) == '#')
                result += " " + numbers[num++];
            else
                result += " " + postfix.charAt(n);
        if (result.length() > 0)
            result = result.substring(1);
        return result;
    }

    private static double convert(String s) {
        try {
            return (new Double(s)).doubleValue();
        } catch (NumberFormatException nfe) {
            return -9876.54;
        }
    }
}