package me.samboycoding.gowguildtool.utils;

/**
 * Simple stopwatch
 * @author Sam
 */
public class Stopwatch
{

    private long start = 0;
    private long fin = 0;
    /**
     * Determines the state of the stopwatch:
     * <ul>
     * <li>0 = reset (default)</li>
     * <li>1 = started</li>
     * <li>2 = stopped, not reset</li>
     * </ul>
     */
    private int state = 0;

    public void start()
    {
        if (state != 0)
        {
            throw new IndexOutOfBoundsException("Stopwatch is not reset! It's in state " + state);
        }
        start = System.currentTimeMillis();
        state = 1;
    }

    public void stop()
    {
        if (state != 1)
        {
            throw new IndexOutOfBoundsException("Stopwatch is not started! It's in state " + state);
        }
        fin = System.currentTimeMillis();
        state = 2;
    }

    public void reset()
    {
        if (state == 0)
        {
            throw new IndexOutOfBoundsException("Stopwatch already reset!");
        }
        start = 0;
        fin = 0;
        state = 0;
    }

    public long getTime()
    {
        if (start == 0)
        {
            throw new IndexOutOfBoundsException("Stopwatch has no start time!");
        }
        if (fin != 0)
        {
            return fin - start;
        }
        return System.currentTimeMillis() - start;
    }
}
