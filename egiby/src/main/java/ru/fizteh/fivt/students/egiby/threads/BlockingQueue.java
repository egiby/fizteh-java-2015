package ru.fizteh.fivt.students.egiby.threads;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by egiby on 17.12.15.
 */
public class BlockingQueue<T> {
    private int maxQueueSize;
    private Queue<T> queue = new ArrayDeque<>();

    BlockingQueue(int size) {
        maxQueueSize = size;
    }

    public synchronized void offer(List<T> list) {
        Iterator<T> iterator = list.iterator();

        while (list.size() + queue.size() > maxQueueSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (iterator.hasNext()) {
            queue.offer(iterator.next());
        }

        notifyAll();
    }

    public synchronized List<T> take(int n) {
        while (queue.size() < n) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<T> list = new ArrayList<>();

        for (int i = 0; i < n; ++i) {
            list.add(queue.poll());
        }

        notifyAll();
        return list;
    }
}
