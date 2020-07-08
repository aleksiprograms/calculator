package com.aleksiprograms.calculator.tools;

import java.text.DecimalFormat;

public class Calculator {

    public static final String[][] FUNCTIONS = {
            {"sin", "s"},
            {"cos", "c"},
            {"tan", "t"},
            {"sqrt", "r"},
            {"abs", "a"},
            {"log", "l"},
            {"ln", "n"}
    };
    public static final String[][] CONSTANTS = {
            {"pi", String.valueOf(Math.PI)},
            {"e", String.valueOf(Math.E)}
    };
    public static final String[] OPERATORS = {
            "+", "-", "*", "/", "^", "$"
    };

    private static boolean radians;

    private Calculator() {
    }

    public static void setRadians(boolean radians) {
        Calculator.radians = radians;
    }

    public static String calculate(String expression) {
        double[] numbers = new double[30];
        expression = functionsToShorter(expression);
        expression = constantsToValues(expression);
        String infix = expression;
        String result;
        try {
            String postfix = infixToPostfix(infix, numbers);
            result = getResult(postfix, numbers);
            DecimalFormat decimalFormat = new DecimalFormat("#.##########");
            result = String.valueOf(decimalFormat.format(Double.parseDouble(result)));
        } catch (Exception ex) {
            result = "ERROR";
        }
        return result;
    }

    private static String getResult(String postfix, double[] numbers) {
        DoubleStack memory = new DoubleStack();
        int n = 0;
        for (int i = 0; i < postfix.length(); i++) {
            char character = postfix.charAt(i);
            switch (character) {
                case '#':
                    memory.push(numbers[n++]);
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
                case 'a':
                    memory.push(Math.abs(memory.pop()));
                    break;
                case 'r':
                    memory.push(Math.sqrt(memory.pop()));
                    break;
                case 'n':
                    memory.push(Math.log(memory.pop()));
                    break;
                case 'l':
                    memory.push(Math.log10(memory.pop()));
                    break;
                case 's':
                    if (radians) {
                        memory.push(Math.sin(memory.pop()));
                    } else {
                        memory.push(Math.sin(Math.toRadians(memory.pop())));
                    }
                    break;
                case 'c':
                    if (radians) {
                        memory.push(Math.cos(memory.pop()));
                    } else {
                        memory.push(Math.cos(Math.toRadians(memory.pop())));
                    }
                    break;
                case 't':
                    if (radians) {
                        memory.push(Math.tan(memory.pop()));
                    } else {
                        memory.push(Math.tan(Math.toRadians(memory.pop())));
                    }
                    break;
            }
        }
        return String.valueOf(memory.pop());
    }

    private static String infixToPostfix(String infix, double[] numbers) {
        CharStack operators = new CharStack();
        int n = 0;
        String postfix = "";
        infix += " ";
        boolean firstTime = true;
        for (int i = 0; i < infix.length(); i++) {
            char character = infix.charAt(i);
            if (character == '-' && firstTime) {
                character = '$';
            }
            if (character != ' ') {
                firstTime = false;
            }
            if (isDigit(character)) {
                int m = i + 1;
                while (isDigit(infix.charAt(m))) {
                    m++;
                }
                numbers[n++] = Double.parseDouble(infix.substring(i, m));
                postfix += '#';
                i = m - 1;
            } else if (isOperator(character)) {
                while (leftFirst(operators.peek(), character)) {
                    postfix += operators.pop();
                }
                operators.push(character);
            } else if (isFunction(character)) {
                operators.push('(');
                operators.push(character);
                i++;
                firstTime = true;
            } else if (character == '(') {
                operators.push(character);
                firstTime = true;
            } else if (character == ')') {
                while (operators.peek() != '(') {
                    postfix += operators.pop();
                }
                operators.pop();
            }
        }
        while (!operators.empty()) {
            postfix += operators.pop();
        }
        return postfix;
    }

    private static boolean leftFirst(char characterA, char characterB) {
        if (characterA == '^' && characterB == '^') {
            return false;
        }
        int rankA = rank(characterA);
        int rankB = rank(characterB);
        return rankA >= rankB;
    }

    private static int rank(char character) {
        switch (character) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '$':
                return 3;
            case '^':
                return 4;
            default:
                return 0;
        }
    }

    private static boolean isDigit(char character) {
        return (character >= '0' && character <= '9') || character == '.';
    }

    private static boolean isFunction(char character) {
        for (String[] function : FUNCTIONS) {
            if (Character.toString(character).equals(function[1])) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOperator(char character) {
        for (String operator : OPERATORS) {
            if (Character.toString(character).equals(operator)) {
                return true;
            }
        }
        return false;
    }

    private static String constantsToValues(String expression) {
        for (String[] constant : CONSTANTS) {
            expression = expression.replace(constant[0], constant[1]);
        }
        return expression;
    }

    private static String functionsToShorter(String expression) {
        for (String[] function : FUNCTIONS) {
            expression = expression.replace(function[0], function[1]);
        }
        return expression;
    }
}