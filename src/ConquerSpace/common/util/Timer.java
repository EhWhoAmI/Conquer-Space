/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.common.util;

/**
 * Timer that executes an action on an interval.
 * @author EhWhoAmI
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
                //Do nothing, because it is the default action
            }
        };

        runningThread = new Thread(() -> {
            while (running) {
                long timeStart = System.currentTimeMillis();
                action.run();
                long timeEnd = System.currentTimeMillis();
                
                long actualWait = wait - (timeEnd - timeStart);
                
                //Skip because the tick took longer than expected
                if(actualWait <= 0) {
                    continue;
                }
                
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException ex) {
                    //Ignore
                }
            }
        });
        runningThread.setName("Timer Thread");
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
