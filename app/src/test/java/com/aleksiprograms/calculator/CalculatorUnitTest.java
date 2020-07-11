package com.aleksiprograms.calculator;

import com.aleksiprograms.calculator.tools.Calculator;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorUnitTest {
    @Test
    public void testConstants() {
        assertEquals("3.1415926536", Calculator.calculate("pi"));
        assertEquals("2.7182818285", Calculator.calculate("e"));
    }
    @Test
    public void testAddition() {
        assertEquals("5", Calculator.calculate("3+2"));
        assertEquals("5", Calculator.calculate("2+3"));
        assertEquals("1", Calculator.calculate("3+(-2)"));
        assertEquals("-1", Calculator.calculate("(-3)+2"));
        assertEquals("-5", Calculator.calculate("(-3)+(-2)"));
    }
    @Test
    public void testSubtraction() {
        assertEquals("1", Calculator.calculate("3-2"));
        assertEquals("-1", Calculator.calculate("2-3"));
        assertEquals("5", Calculator.calculate("3-(-2)"));
        assertEquals("-5", Calculator.calculate("(-3)-2"));
        assertEquals("-1", Calculator.calculate("(-3)-(-2)"));
    }
    @Test
    public void testMultiplication() {
        assertEquals("6", Calculator.calculate("3*2"));
        assertEquals("6", Calculator.calculate("2*3"));
        assertEquals("-6", Calculator.calculate("3*(-2)"));
        assertEquals("-6", Calculator.calculate("(-3)*2"));
        assertEquals("6", Calculator.calculate("(-3)*(-2)"));
    }
    @Test
    public void testDivision() {
        assertEquals("1.5", Calculator.calculate("3/2"));
        assertEquals("0.6666666667", Calculator.calculate("2/3"));
        assertEquals("-1.5", Calculator.calculate("3/(-2)"));
        assertEquals("-1.5", Calculator.calculate("(-3)/2"));
        assertEquals("1.5", Calculator.calculate("(-3)/(-2)"));
        assertEquals("ERROR", Calculator.calculate("3/0"));
    }
    @Test
    public void testExponentiation() {
        assertEquals("9", Calculator.calculate("3^(2)"));
        assertEquals("3", Calculator.calculate("3^(1)"));
        assertEquals("1", Calculator.calculate("3^(0)"));
        assertEquals("0.1111111111", Calculator.calculate("3^(-2)"));
        assertEquals("5", Calculator.calculate("25^(1/2)"));
        assertEquals("ERROR", Calculator.calculate("(-25)^(1/2)"));
        assertEquals("2.9528826414", Calculator.calculate("(2.3)^(1.3)"));
    }
    @Test
    public void testSin() {
        Calculator.useRadians(false);
        assertEquals("0", Calculator.calculate("sin(0)"));
        assertEquals("1", Calculator.calculate("sin(90)"));
        assertEquals("0", Calculator.calculate("sin(180)"));
        assertEquals("-1", Calculator.calculate("sin(270)"));
        assertEquals("0", Calculator.calculate("sin(360)"));
        assertEquals("0", Calculator.calculate("sin(720)"));
        assertEquals("0.4226182617", Calculator.calculate("sin(25)"));
        Calculator.useRadians(true);
        assertEquals("0", Calculator.calculate("sin(0)"));
        assertEquals("1", Calculator.calculate("sin(pi/2)"));
        assertEquals("0", Calculator.calculate("sin(pi)"));
        assertEquals("-1", Calculator.calculate("sin(pi+pi/2)"));
        assertEquals("0", Calculator.calculate("sin(2*pi)"));
        assertEquals("0", Calculator.calculate("sin(4*pi)"));
    }
    @Test
    public void testCos() {
        Calculator.useRadians(false);
        assertEquals("1", Calculator.calculate("cos(0)"));
        assertEquals("0", Calculator.calculate("cos(90)"));
        assertEquals("-1", Calculator.calculate("cos(180)"));
        assertEquals("0", Calculator.calculate("cos(270)"));
        assertEquals("1", Calculator.calculate("cos(360)"));
        assertEquals("1", Calculator.calculate("cos(720)"));
        assertEquals("0.906307787", Calculator.calculate("cos(25)"));
        Calculator.useRadians(true);
        assertEquals("1", Calculator.calculate("cos(0)"));
        assertEquals("0", Calculator.calculate("cos(pi/2)"));
        assertEquals("-1", Calculator.calculate("cos(pi)"));
        assertEquals("0", Calculator.calculate("cos(pi+pi/2)"));
        assertEquals("1", Calculator.calculate("cos(2*pi)"));
        assertEquals("1", Calculator.calculate("cos(4*pi)"));
    }
    @Test
    public void testTan() {
        Calculator.useRadians(false);
        assertEquals("0", Calculator.calculate("tan(0)"));
        assertEquals("ERROR", Calculator.calculate("tan(90)"));
        assertEquals("0", Calculator.calculate("tan(180)"));
        assertEquals("ERROR", Calculator.calculate("tan(270)"));
        assertEquals("0", Calculator.calculate("tan(360)"));
        Calculator.useRadians(true);
        assertEquals("0", Calculator.calculate("tan(0)"));
        assertEquals("ERROR", Calculator.calculate("tan(pi/2)"));
        assertEquals("0", Calculator.calculate("tan(pi)"));
        assertEquals("ERROR", Calculator.calculate("tan(pi+pi/2)"));
        assertEquals("0", Calculator.calculate("tan(2*pi)"));
    }
    @Test
    public void testLog() {
        assertEquals("0", Calculator.calculate("log(1)"));
        assertEquals("1", Calculator.calculate("log(10)"));
        assertEquals("1.1760912591", Calculator.calculate("log(15)"));
        assertEquals("ERROR", Calculator.calculate("log(-1)"));
    }
    @Test
    public void testLn() {
        assertEquals("0", Calculator.calculate("ln(1)"));
        assertEquals("1", Calculator.calculate("ln(e)"));
        assertEquals("2.7080502011", Calculator.calculate("ln(15)"));
        assertEquals("ERROR", Calculator.calculate("ln(-1)"));
    }
    @Test
    public void testAbs() {
        assertEquals("3", Calculator.calculate("abs(3)"));
        assertEquals("3", Calculator.calculate("abs(-3)"));
        assertEquals("3.45", Calculator.calculate("abs(-3.45)"));
    }
    @Test
    public void testSqrt() {
        assertEquals("5", Calculator.calculate("sqrt(25)"));
        assertEquals("2.2360679775", Calculator.calculate("sqrt(5)"));
        assertEquals("ERROR", Calculator.calculate("sqrt(-25)"));
    }
    @Test
    public void testComplexExpressions() {
        Calculator.useRadians(false);
        assertEquals("11", Calculator.calculate("3*2+4-(4/2)*2+sqrt(25)"));
        assertEquals("1", Calculator.calculate("sin(90)*cos(0)*ln(e)"));
        assertEquals("3", Calculator.calculate("((1+2)*(3-1))/(ln(e)*2)"));
    }
    @Test
    public void testIncompleteExpressions() {
        assertEquals("ERROR", Calculator.calculate("-5-"));
        assertEquals("ERROR", Calculator.calculate("5*"));
        assertEquals("ERROR", Calculator.calculate("sqrt()"));
        assertEquals("ERROR", Calculator.calculate("5+a"));
        assertEquals("ERROR", Calculator.calculate("sin(a)"));
    }
}