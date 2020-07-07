package com.aleksiprograms.calculator.tools;

public class Calculator {

    private static DoubleStack memory;
    private static CharStack operators;
    private static String postfix;
    private static double[] numbers;
    private static String infix;
    private static boolean radians;

    private Calculator() {
    }

    public static void setRadians(boolean radians) {
        Calculator.radians = radians;
    }

    public static String calculate(String expression) {
        memory = new DoubleStack();
        operators = new CharStack();
        numbers = new double[30];
        infix = expression;
        postfix = infixToPostfix(infix);
        int n = 0;
        memory.clear();
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
                case 'z':
                    if (radians) {
                        memory.push(Math.asin(memory.pop()));
                    } else {
                        memory.push(Math.asin(Math.toRadians(memory.pop())));
                    }
                    break;
                case 'k':
                    if (radians) {
                        memory.push(Math.acos(memory.pop()));
                    } else {
                        memory.push(Math.acos(Math.toRadians(memory.pop())));
                    }
                    break;
                case 'd':
                    if (radians) {
                        memory.push(Math.atan(memory.pop()));
                    } else {
                        memory.push(Math.atan(Math.toRadians(memory.pop())));
                    }
                    break;
            }
        }
        return String.valueOf(memory.pop());
    }

    private static String infixToPostfix(String infix) {
        int n = 0;
        postfix = "";
        infix += " ";
        operators.clear();
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
                numbers[n++] = convert(infix.substring(i, m));
                postfix += '#';
                i = m - 1;
            } else if (character == 'x') {
                postfix += character;
            } else if ("+-*/^$".indexOf(character) >= 0) {
                while (leftFirst(operators.peek(), character)) {
                    postfix += operators.pop();
                }
                operators.push(character);
            } else if ("arelsc".indexOf(character) >= 0) {
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

    private static double convert(String string) {
        try {
            return (new Double(string)).doubleValue();
        } catch (NumberFormatException nfe) {
            return -9876.54;
        }
    }
}