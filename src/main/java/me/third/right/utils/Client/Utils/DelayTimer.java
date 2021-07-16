package me.third.right.utils.Client.Utils;

public class DelayTimer {
    private long time;

    public DelayTimer() {
        this.time = -1L;
    }

    public boolean passedMs(final long ms) {
        return this.getMs(System.nanoTime() - this.time) >= ms;
    }


    public void reset() {
        this.time = System.nanoTime();
    }

    public long getMs(final long time) {
        return time / 1000000L;
    }
}
