package ru.fizteh.fivt.students.egiby.threads;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egiby on 18.12.15.
 */

public class BlockingQueueTest {
    private static final Object MONITOR = new Object();
    private static final Object WAIT_FIRST = new Object();
    private static volatile int current = 1;

    private static class ThreadTest extends Thread {
        protected static BlockingQueue<Integer> queue = new BlockingQueue<>(15);
    }

    private static class ThreadPush extends ThreadTest {
        private List<Integer> list;
        private int id;

        ThreadPush(List<Integer> to, int n) {
            list = to;
            id = n;
        }

        @Override
        public void run() {
            synchronized (WAIT_FIRST) {
                while (id != current) {
                    try {
                        WAIT_FIRST.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                queue.offer(list);
                current++;
                WAIT_FIRST.notifyAll();
            }
        }
    }

    private static List<Integer> answer = new ArrayList<>();

    private static class ThreadPop extends ThreadTest {
        private int n;

        ThreadPop(int size) {
            n = size;
        }

        @Override
        public void run() {
            //System.err.println((answer = queue.take(n)).toString());
            answer = queue.take(n);
            synchronized (MONITOR) {
                MONITOR.notify();
            }
        }
    }

    @Test
    public void test() throws Exception {
        List<Integer> to1 = new ArrayList<>();
        List<Integer> to2 = new ArrayList<>();

        for (int i = 0; i < 100000; ++i) {
            to1.add(i * 2);
        }

        for (int i = 1; i < 300000; ++i) {
            to2.add(i * 2 + 1);
        }

        ThreadPush threadPush1 = new ThreadPush(to1, 1);
        ThreadPush threadPush2 = new ThreadPush(to2, 2);

        ThreadPop threadPop= new ThreadPop(to1.size() + to2.size());

        synchronized (MONITOR) {
            threadPush2.start();
            threadPush1.start();
            threadPop.start();
            while (answer.isEmpty()) {
                MONITOR.wait();
            }

            to1.addAll(to2);
            assertThat(answer, is(to1));
        }
    }
}
