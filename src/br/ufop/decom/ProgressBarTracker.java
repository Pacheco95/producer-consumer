package br.ufop.decom;

import java.util.Stack;

import static br.ufop.decom.Constants.BUFFER_MAX_SIZE;

class ProgressBarTracker implements Runnable {
    private final Stack stack;
    private final long updateTime;

    ProgressBarTracker(Stack stack, long updateTime) {
        this.stack = stack;
        this.updateTime = updateTime;
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            StringBuffer draw = new StringBuffer("[");

            for (int i = 0; i < stack.size(); i++)
                draw.append("=");

            for (int i = stack.size(); i < BUFFER_MAX_SIZE; i++)
                draw.append(" ");


            draw.append(String.format("] %5.2f%%\r", stack.size() * 100.0 / BUFFER_MAX_SIZE));
            System.out.print(draw);
            System.out.flush();
            try {
                Thread.sleep(updateTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
