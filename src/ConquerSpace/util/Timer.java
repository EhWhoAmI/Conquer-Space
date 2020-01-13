package ConquerSpace.util;

/**
 *
 * @author zyunl
 */
public class Timer {

    private Runnable action;
    private Thread runningThread;

    private int wait;
    private boolean running = false;

    public Timer() {
        action = new Runnable() {
            @Override
            public void run() {
                //Do nothing
            }
        };

        runningThread = new Thread(() -> {
            while (running) {
                action.run();
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException ex) {
                }
            }
        });
    }

    public void start() {
        if (!runningThread.isAlive()) {
            runningThread.start();
        }

        running = true;
    }
    
    public void pause() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    public int getWait() {
        return wait;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }
}
