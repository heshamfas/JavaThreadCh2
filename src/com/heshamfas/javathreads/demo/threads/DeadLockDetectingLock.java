package com.heshamfas.javathreads.demo.threads;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by root on 6/26/15.
 */
public class DeadLockDetectingLock extends ReentrantLock{
    private static List deadLocksRegistry = new ArrayList();
    private List hardWaitingThreads = new ArrayList<Thread>();
    private boolean debugging;

    private static synchronized void registerLock(DeadLockDetectingLock deadLockDetectingLock){

        if(!deadLocksRegistry.contains(deadLockDetectingLock))
            deadLocksRegistry.add(deadLockDetectingLock);
    }

    private static synchronized void unregisterLock(DeadLockDetectingLock deadLockDetectingLock){
        if(deadLocksRegistry.contains(deadLockDetectingLock)){
            deadLocksRegistry.remove(deadLockDetectingLock);
        }
    }
private static synchronized void freeIfHardwait(List l, Thread t){
    if(l.contains(t)){
        l.remove(t);
    }
}
    private static synchronized void markAsHardwait(List hardWaitingThreads, Thread t){
        if(!hardWaitingThreads.contains(t)){
            hardWaitingThreads.add(t);
        }
    }
    private static Iterator getAllLocksOwned(Thread t){
        DeadLockDetectingLock current;
        ArrayList results = new ArrayList();
        Iterator iterator = deadLocksRegistry.iterator();
        while(iterator.hasNext()){
            current = (DeadLockDetectingLock) iterator.next();
            if(current.getOwner() == t) results.add(current);
        }
        return results.iterator();
    }

    private static Iterator getAllThreadsHardWaiting(DeadLockDetectingLock deadLockDetectingLock){

        return deadLockDetectingLock.hardWaitingThreads.iterator();
    }

private static synchronized boolean canThreadWaitOnLock(Thread thread, DeadLockDetectingLock deadLockDetectingLock){
    Iterator locksOwned = getAllLocksOwned(thread);
    while (locksOwned.hasNext()){
        DeadLockDetectingLock current = (DeadLockDetectingLock) locksOwned.next();
        if(current == deadLockDetectingLock){
            return false;
        }
        Iterator waitingThreads = getAllThreadsHardWaiting(current);
        while (waitingThreads.hasNext()){
            Thread otherThread = (Thread) waitingThreads.next();
            if (!canThreadWaitOnLock(otherThread, deadLockDetectingLock)){
                return false;
            }
        }
    }
    return true;
}

  public DeadLockDetectingLock(){
      this(false,false);
  }
    public DeadLockDetectingLock(boolean fair){
        this(fair, false);
    }
    public DeadLockDetectingLock(boolean fair, boolean debug){
        super(fair);
        debugging= debug;
        registerLock(this);
    }

    public void lock(){
        if(isHeldByCurrentThread()){
            if(debugging) System.out.println("Aready Own Lock");
            super.lock();
            freeIfHardwait(hardWaitingThreads,Thread.currentThread());
            return;
        }
        markAsHardwait(hardWaitingThreads, Thread.currentThread());
        if(canThreadWaitOnLock(Thread.currentThread(), this)){
            if(debugging){
                System.out.println(("Waiting for Lock"));
                }
            super.lock();
            freeIfHardwait(hardWaitingThreads,Thread.currentThread());
            if(debugging){
                System.out.println("Got new lock");
            }else {
                throw new DeadLockDetectedException("DeadLock");
            }
        }
    }

    public void lockInterruptibly() throws InterruptedException{
        lock();
    }





    /* DeadLockDetectingCondition*/

    public class DeadLockDetectingCondition implements Condition{
        Condition embeddedCondition;

        public DeadLockDetectingCondition(ReentrantLock reentrantLock){
            super();

        }
        protected DeadLockDetectingCondition(ReentrantLock lock, Condition condition){
            embeddedCondition = condition;
        }

        @Override
        public void await() throws InterruptedException {
            try{
                markAsHardwait(hardWaitingThreads, Thread.currentThread());
                embeddedCondition.await();
            }finally {
                freeIfHardwait(hardWaitingThreads, Thread.currentThread());
            }
        }

        @Override
        public void awaitUninterruptibly() {

            markAsHardwait(hardWaitingThreads, Thread.currentThread());
            embeddedCondition.awaitUninterruptibly();
            freeIfHardwait(hardWaitingThreads, Thread.currentThread());
        }

        @Override
        public long awaitNanos(long nanosTimeout) throws InterruptedException {
            try {
                markAsHardwait(hardWaitingThreads, Thread.currentThread());
                return embeddedCondition.awaitNanos(nanosTimeout);

            }finally {
                freeIfHardwait(hardWaitingThreads,Thread.currentThread());
            }
        }

        @Override
        public boolean await(long time, TimeUnit unit) throws InterruptedException {
            try{
                markAsHardwait(hardWaitingThreads, Thread.currentThread());
                return embeddedCondition.await(time, unit);
            }finally {
                freeIfHardwait(hardWaitingThreads,Thread.currentThread());
            }
        }

        @Override
        public boolean awaitUntil(Date deadline) throws InterruptedException {
            try {
                markAsHardwait(hardWaitingThreads,Thread.currentThread());
                return embeddedCondition.awaitUntil(deadline);
            }finally {
                freeIfHardwait(hardWaitingThreads, Thread.currentThread());
            }
        }

        @Override
        public void signal() {
            embeddedCondition.signal();
        }

        @Override
        public void signalAll() {
            embeddedCondition.signalAll();
        }

    }
    public Condition newCondition(){
        return new DeadLockDetectingCondition(this);
    }
}
