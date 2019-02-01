package br.ufop.decom;

import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static br.ufop.decom.Constants.BUFFER_MAX_SIZE;

public class Producer implements Runnable {

    private final Stack<Integer> buffer;
    private final Lock bufferLock;
    private final Condition notEmpty;
    private final Condition notFull;

    Producer(Stack<Integer> buffer, Lock bufferLock, Condition notEmpty, Condition notFull) {
        this.buffer = buffer;
        this.bufferLock = bufferLock;
        this.notEmpty = notEmpty;
        this.notFull = notFull;
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        for (int i = 0; ; i++) {
            bufferLock.lock();

            try {
                while (buffer.size() == BUFFER_MAX_SIZE) {
                    notFull.await();
                }

                buffer.push(i);

                notEmpty.signal();
                bufferLock.unlock();
                Thread.sleep(Constants.PROD_WAIT_TIME + (long) (Math.random() * Constants.OFFSET_MAX_RAND_TIME));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
