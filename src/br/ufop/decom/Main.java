package br.ufop.decom;

import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Stack<Integer> buffer = new Stack<>();
        Lock bufferLock = new ReentrantLock(false);
        Condition notFull = bufferLock.newCondition();
        Condition notEmpty = bufferLock.newCondition();

        Producer producer = new Producer(buffer, bufferLock, notEmpty, notFull);
        Consumer consumer = new Consumer(buffer, bufferLock, notEmpty, notFull);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);
        Thread progressThread = new Thread(new ProgressBarTracker(buffer, 1000));

        producerThread.start();
        consumerThread.start();
        progressThread.start();

        producerThread.join();
        consumerThread.join();
        progressThread.join();
    }
}
