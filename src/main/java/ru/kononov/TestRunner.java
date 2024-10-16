package ru.kononov;

import ru.kononov.annotations.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class TestRunner {
    private Class clz;
    private Method[] methods;
    private HashMap<String, Integer> resultTests = new HashMap<>();

    public TestRunner(Class clz) {
        this.clz = clz;
        this.methods = clz.getDeclaredMethods();
    }

    public void runTests() throws Exception {
        checkers(); // Выполняем необходимые проверки аннотаций
        Object tests = clz.getDeclaredConstructor().newInstance();
        Method methodBefore = getMethodByAnnotation(BeforeSuite.class);
        Method methodBeforeEach = getMethodByAnnotation(BeforeTest.class);
        Method methodAfter = getMethodByAnnotation(AfterSuite.class);
        Method methodAfterEach = getMethodByAnnotation(AfterTest.class);
        if(methodBefore != null)    //Выполняем метод @BeforeSuite, если такой есть
            runTest(methodBefore, tests);
        sortedTests().forEach(method -> {   //Выполняем основные тесты @Test
            if(methodAfterEach!= null)
                runTest(methodBeforeEach, tests);   //Выполняем @BeforeTest
            runTest(method, tests);
            if(methodAfterEach!= null)
                runTest(methodAfterEach, tests);   //Выполняем @AfterTest
        });
        if(methodAfter != null)    //Выполняем метод @AfterSuite, если такой есть
            runTest(methodAfter, tests);
        System.out.println("----------------------");
        System.out.println("Результаты тестов: ");
        int positiveTests = 0;
        int negativeTests = 0;
        for(Map.Entry<String, Integer> entry : resultTests.entrySet()) {
            String key = entry.getKey();
            if(entry.getValue() == 0)
            {
                System.out.println(entry.getKey() + ":   OK");
                positiveTests++;
            }
            else {
                System.out.println(entry.getKey() + ":   ERROR");
                negativeTests++;
            }


        }
        System.out.println("Успешных тестов:     " + positiveTests);
        System.out.println("Тестов с ошибками:   " + negativeTests);
    }

    private int countMethodsWithAnnotations(Class clz, Class annotation) {
        int count = 0;
        for (Method method : methods) {
            if(method.isAnnotationPresent(annotation))
                count++;
        }
        return count;
    }

    private Method getMethodByAnnotation(Class annotation) {
        for (Method method : methods) {
            if(method.isAnnotationPresent(annotation))
                return method;
        }
        return null;
    }

    private void checkers() throws Exception {
        if(countMethodsWithAnnotations(clz, BeforeSuite.class) > 1)
            throw new Exception("Methods with BeforeSuite annotation more than one");
        if(countMethodsWithAnnotations(clz, AfterSuite.class) > 1)
            throw new Exception("Methods with AfterSuite annotation more than one");
    }

    private List<Method> sortedTests() {
        List<Method> sorted = Arrays.stream(this.methods)
                .filter(m -> m.getAnnotation(Test.class) != null)
                .sorted(Comparator.comparingInt(m -> -m.getAnnotation(Test.class).priority()))
                .collect(Collectors.toList());
        return sorted;
    }

    private void runTest(Method m, Object cls) {
        try {
            m.invoke(cls);
            resultTests.put(m.getName(),0);
        } catch (Exception e) {
            resultTests.put(m.getName(),1);
        }
    }
}
