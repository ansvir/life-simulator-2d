package com.itique.ls2d.model.timeline;

public class TimeLineTask implements Runnable {

    private volatile long currentMinutes;
    private volatile long currentHours;
    private final long waitTime;
    private volatile long prevTime;
    private volatile long time;

    public TimeLineTask(long waitTime) {
        this.currentMinutes = 0L;
        this.currentHours = 0L;
        this.prevTime = System.currentTimeMillis();
        this.time = this.prevTime;
        this.waitTime = waitTime;
    }

    @Override
    public void run() {
        boolean stop = false;
        while (!stop) {
            stop = iterateAndGet();
        }
        Thread.currentThread().interrupt();
    }

    private boolean iterateAndGet() {
        if (this.prevTime - this.time >= this.waitTime) {
            this.time = this.prevTime;
            return next();
        }
        this.prevTime = System.currentTimeMillis();
        return false;
    }

    private synchronized boolean next() {
        if (this.currentMinutes >= 59L) {
            this.currentMinutes = 0L;
            if (this.currentHours >= 23L) {
                return true;
            } else {
                this.currentHours++;
                return false;
            }
        } else {
            this.currentMinutes++;
            return false;
        }
    }

    public String get() {
        StringBuilder result = new StringBuilder();
        if (this.currentHours < 10L) {
            result.append("0");
        }
        result.append(this.currentHours);
        result.append(":");
        if (this.currentMinutes < 10L) {
            result.append("0");
        }
        result.append(this.currentMinutes);
        return result.toString();
    }

}
