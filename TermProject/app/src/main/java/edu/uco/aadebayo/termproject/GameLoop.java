package edu.uco.aadebayo.termproject;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by Ayodeji on 10/14/2015.
 */
public class GameLoop extends Thread {

    private static final String TAG = GameLoop.class.getSimpleName();
    //flag to hold game state
    private boolean running;
    private GamePanel gamePanel;
    Canvas canvas;
    private final static int MAX_FPS = 60;
    //max frames to skip
    private final static int FRAME_SKIP = 6;
    //frame period
    private final static int PERIOD = 1000/MAX_FPS;


    // Stuff for stats */
    private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
    // we'll be reading the stats every second
    private final static int 	STAT_INTERVAL = 1000; //ms
    // the average will be calculated by storing
    // the last n FPSs
    private final static int	FPS_HISTORY_NR = 10;
    // last time the status was stored
    private long lastStatusStore = 0;
    // the status time counter
    private long statusIntervalTimer	= 0l;
    // number of frames skipped since the game started
    private long totalFramesSkipped			= 0l;
    // number of frames skipped in a store cycle (1 sec)
    private long framesSkippedPerStatCycle 	= 0l;

    // number of rendered frames in an interval
    private int frameCountPerStatCycle = 0;
    private long totalFrameCount = 0l;
    // the last FPS values
    private double 	fpsStore[];
    // the number of times the stat has been read
    private long 	statsCount = 0;
    // the average FPS since the game started
    private double 	averageFps = 0.0;

    // Surface holder that can access the physical surface
    private SurfaceHolder surfaceHolder;
    // The actual view that handles inputs
    // and draws to the surface


    public void setRunning(boolean running){
        this.running = running;
    }

    public GameLoop(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run(){
        Canvas canvas;
        //long tickCount = 0L;
        Log.d(TAG, "Starting game loop");
        //initialise timing element to start gathering
        initTimingElements();

        long startTime;
        long timeDifference;
        long sleepTime = 0;
        int frameSkipped;

        while (running){
            canvas = null;
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    startTime = System.currentTimeMillis();
                    frameSkipped = 0;//resetting the frames skipped
                    //update game state
                    this.gamePanel.update();
                    //darw canvas to panel
                    this.gamePanel.render(canvas);
                    //calculate how long the cycle took
                    timeDifference = System.currentTimeMillis() - startTime;
                    //calculate the sleep time
                    sleepTime = (PERIOD - timeDifference);

                    if(sleepTime > 0){
                        //sleep OK
                        try{
                            //sleep thread also saves a bit of power
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    while (sleepTime < 0 && frameSkipped < FRAME_SKIP){
                        //game lagging update without rendering
                        this.gamePanel.update();
                        //add frame period to check if in next frame
                        sleepTime += PERIOD;
                        frameSkipped++;
                    }
                    if(frameSkipped > 0){
                        Log.d(TAG, "Skipped:" + frameSkipped);
                    }
                    //for the stats part
                    framesSkippedPerStatCycle += frameSkipped;
                    //store the stats
                    storeStats();
                }
            } finally {
                if(canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    /**
     * With the aid of several articles online I think it works right
     * The statistics - it is called every cycle, it checks if time since last
     * store is greater than the statistics gathering period (1 sec) and if so
     * it calculates the FPS for the last period and stores it.
     *
     *  It tracks the number of frames per period. The number of frames since
     *  the start of the period are summed up and the calculation takes part
     *  only if the next period and the frame count is reset to 0.
     */
    private void storeStats() {
        frameCountPerStatCycle++;
        totalFrameCount++;

        // check the actual time
        statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

        if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
            // calculate the actual frames pers status check interval
            double actualFps = (double)(frameCountPerStatCycle / (STAT_INTERVAL / 1000));

            //stores the latest fps in the array
            fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;

            // increase the number of times statistics was calculated
            statsCount++;

            double totalFps = 0.0;
            // sum up the stored fps values
            for (int i = 0; i < FPS_HISTORY_NR; i++) {
                totalFps += fpsStore[i];
            }

            // obtain the average
            if (statsCount < FPS_HISTORY_NR) {
                // in case of the first 10 triggers
                averageFps = totalFps / statsCount;
            } else {
                averageFps = totalFps / FPS_HISTORY_NR;
            }
            // saving the number of total frames skipped
            totalFramesSkipped += framesSkippedPerStatCycle;
            // resetting the counters after a status record (1 sec)
            framesSkippedPerStatCycle = 0;
            statusIntervalTimer = 0;
            frameCountPerStatCycle = 0;

            statusIntervalTimer = System.currentTimeMillis();
            lastStatusStore = statusIntervalTimer;
            gamePanel.setAvgFps("FPS: " + df.format(averageFps));
        }
    }

    private void initTimingElements() {
        // initialise timing elements
        fpsStore = new double[FPS_HISTORY_NR];
        for (int i = 0; i < FPS_HISTORY_NR; i++) {
            fpsStore[i] = 0.0;
        }
        Log.d(TAG + ".initTimingElements()", "Timing elements for stats initialised");
    }

}
