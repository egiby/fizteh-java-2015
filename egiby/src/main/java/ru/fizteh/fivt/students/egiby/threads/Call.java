package ru.fizteh.fivt.students.egiby.threads;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by egiby on 17.12.15.
 */
public class Call {
    private static CyclicBarrier barrier;
    private static volatile boolean needAnswers = true;
    private static volatile int countReady = 0;
    private static final Object MONITOR = new Object();
    private static final Object NEED_QUESTION = new Object();
    private static final Object I_NEED_RESET_BARRIER = new Object();

    private static class Caller extends Thread {
        private static Random random = new Random();

        @Override
        public void run() {
            while (true) {
                synchronized (I_NEED_RESET_BARRIER) {
                    while (barrier.isBroken()) {
                        try {
                            I_NEED_RESET_BARRIER.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                int x = random.nextInt(10);

                if (x == 0) {
                    needAnswers = true;
                    System.out.println("No");
                } else {
                    System.out.println("Yes");
                }

                try {
                    synchronized (MONITOR) {
                        countReady++;
                        synchronized (NEED_QUESTION) {
                            if (countReady == n) {
                                NEED_QUESTION.notify();
                            }
                        }
                    }
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int n;

    private static void help() {
        System.err.println("It is not a good number");
        System.exit(1);
    }

    public static void main(String[] args) {
        try {
            n = Integer.valueOf(args[0]);
        } catch (Exception e) {
            help();
        }

        if (n < 0) {
            help();
        }

        barrier = new CyclicBarrier(n + 1);

        System.out.println("Are you ready?");

        for (int i = 0; i < n; ++i) {
            Caller caller = new Caller();
            caller.start();
        }

        while (true) {
            synchronized (NEED_QUESTION) {
                while (countReady < n) {
                    try {
                        NEED_QUESTION.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.exit(123);
                    }
                }

                countReady = 0;
            }
            if (!needAnswers) {
                System.exit(0);
            }

            synchronized (I_NEED_RESET_BARRIER) {
                System.out.println("Are you ready?");

                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

                barrier.reset();
                I_NEED_RESET_BARRIER.notifyAll();
            }

            needAnswers = false;
        }
    }
}
