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

    private Lock queueLock = new ReentrantLock();
    private Lock offerLock = new ReentrantLock();
    private Lock takeLock = new ReentrantLock();
    private Object waitPush = new Object();
    private Object waitPop = new Object();

    BlockingQueue(int size) {
        maxQueueSize = size;
    }

    private void push(T element) {
        synchronized (waitPush) {
            while (queue.size() == maxQueueSize) {
                try {
                    waitPush.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            queueLock.lock();
            queue.offer(element);
        } finally {
            synchronized (waitPop) {
                waitPop.notify();
            }
            queueLock.unlock();
        }
    }

    private T pop() {
        synchronized (waitPop) {
            while (queue.isEmpty()) {
                try {
                    waitPop.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            queueLock.lock();
            return queue.poll();
        } finally {
            synchronized (waitPush) {
                waitPush.notify();
            }

            queueLock.unlock();
        }
    }

    public void offer(List<T> list) {
        try {
            offerLock.lock();
            Iterator<T> iterator = list.iterator();
            while (iterator.hasNext()) {
                push(iterator.next());
            }
        } finally {
            offerLock.unlock();
        }
    }

    public List<T> take(int n) {
        try {
            takeLock.lock();
            List<T> list = new ArrayList<>();

            for (int i = 0; i < n; ++i) {
                list.add(pop());
            }
            return list;
        } finally {
            takeLock.unlock();
        }
    }
}
