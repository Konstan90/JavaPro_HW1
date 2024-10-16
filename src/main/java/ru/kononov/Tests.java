package ru.kononov;

public class Tests {
    public static void support() {

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
}
