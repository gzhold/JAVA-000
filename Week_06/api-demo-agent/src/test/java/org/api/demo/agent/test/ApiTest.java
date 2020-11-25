package org.api.demo.agent.test;

import org.junit.Test;

public class ApiTest {

    @Test
    public void test() throws InterruptedException {
        ApiTest apiTest = new ApiTest();
        apiTest.echo1();
    }

    public static void main(String[] args) throws InterruptedException {
        ApiTest apiTest = new ApiTest();
        apiTest.echo1();
        apiTest.echo2();
    }


    private void echo1() throws InterruptedException {
        System.out.println("echo 1...");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void echo2() throws InterruptedException {
        System.out.println("echo 2...");
        Thread.sleep((long) (Math.random() * 1000));
    }

}
