package br.ufop.decom;

import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer implements Runnable {

    private final Stack<Integer> buffer;
    private final Lock bufferLock;
    private final Condition notEmpty;
    private final Condition notFull;

    Consumer(Stack<Integer> buffer, Lock bufferLock, Condition notEmpty, Condition notFull) {
        this.buffer = buffer;
        this.bufferLock = bufferLock;
        this.notEmpty = notEmpty;
        this.notFull = notFull;
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            bufferLock.lock();

            try {
                while (buffer.isEmpty())
                    notEmpty.await();

                buffer.pop();
                notFull.signal();
                bufferLock.unlock();
                Thread.sleep(Constants.CONS_WAIT_TIME + (long) (Math.random() * Constants.OFFSET_MAX_RAND_TIME));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}