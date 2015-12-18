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

    private static class ThreadTest extends Thread {
        protected static BlockingQueue<Integer> queue = new BlockingQueue<>(150);
    }

    private static class ThreadPush extends ThreadTest {
        private List<Integer> list;

        ThreadPush(List<Integer> to) {
            list = to;
        }

        @Override
        public void run() {
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            queue.offer(list);
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

        ThreadPush threadPush1 = new ThreadPush(to1);
        ThreadPush threadPush2 = new ThreadPush(to2);

        ThreadPop threadPop= new ThreadPop(to1.size() + to2.size());

        synchronized (MONITOR) {
            threadPush1.start();
            threadPush2.start();
            threadPop.start();
            while (answer.isEmpty()) {
                MONITOR.wait();
            }

            to1.addAll(to2);
            assertThat(answer, is(to1));
        }
    }
}
