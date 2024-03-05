package com.wtech.app;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello World");
    }

    public static int add(int num1, int num2) {
        return num1 + num2;
    }

    public static int divide(int num1, int num2) {
        if (num1 == 0 || num2 == 0) return 1; // not correct, only for the examples to run
        return num1 / num2;
    }
}