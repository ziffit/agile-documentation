package de.fx.agiledocumentation;

public class Sample {

    Integer add(int a, int b) {
        return a + b;
    }

    double divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        return (double) a / b;
    }
}
