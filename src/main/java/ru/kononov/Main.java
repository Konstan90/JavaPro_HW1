package ru.kononov;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws Exception {
        TestRunner testRunner = new TestRunner(Tests.class);
        testRunner.runTests();
    }
}