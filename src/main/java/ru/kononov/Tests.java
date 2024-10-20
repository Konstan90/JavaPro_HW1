package ru.kononov;

import ru.kononov.annotations.*;

public class Tests {
    public static void support() {

    }

    @AfterTest
    public void afterTest(){
        System.out.println("after each test");
    }


    @BeforeTest
    public void beforeTest(){
        System.out.println("before each test");
    }

    @Test(priority = 2)
    public void test1() {
        System.out.println("make test priority 2");
    }

    @Test(priority = 1)
    public void test2() {
        System.out.println("make test priority 1");
        throw new RuntimeException("Error");
    }

    @Test(priority = 5)
    public void test3() {
        System.out.println("make test priority 5");
    }

    @BeforeSuite
    public void testBefore2() {
        System.out.println("make test before");
    }

    @AfterSuite
    public void testAfter() {
        System.out.println("make test after");

    }

    @CsvSource("5 true hello")
    @Test
    public void methodWithParams(int a, boolean b, String c) {

        System.out.println(a + ((b==true) ? " true " : " else ") + c);
    }
}
