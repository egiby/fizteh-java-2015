package ru.fizteh.fivt.students.egiby.threads;

/**
 * Created by egiby on 17.12.15.
 */
public class Counter {
    private static volatile int current = 0;
    private static final Object MONITOR = new Object();
    private static int n;

    private static class ThreadCounter extends Thread {
        private int id;

        ThreadCounter(int id1) {
            id = id1;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (MONITOR) {
                    while (id != current) {
                        try {
                            MONITOR.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                    }

                    System.out.print("Thread-" + (id + 1));
                    current = (current + 1) % n;

                    MONITOR.notifyAll();
                }
            }
        }
    }

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

        for (int i = 0; i < n; ++i) {
            ThreadCounter thread = new ThreadCounter(i);
            thread.start();
        }
    }
}
