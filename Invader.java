import java.util.Random;

public class Invader extends SpaceEntity
{

    private double reloadTimer;
    private static Random rnd = new Random();
    private static boolean edgeReached = false;

    public Invader(double x, double y, int sprite)
    {
        super(x, y, Application.SPRITE_SIZE / 2, sprite);
        dx = 0;
        dy = 0;
        reloadTimer = rnd.nextInt(30) + 5;  
    }

    @Override
    public void update(double frameLength)
    {
        super.update(frameLength);
        if (x > Application.WINDOW_WIDTH - Application.SPRITE_SIZE / 2) edgeReached = true;
        if (x < Application.SPRITE_SIZE / 2) edgeReached = true;

        if (reloadTimer > 0) reloadTimer -= frameLength;
        if (reloadTimer < 0) reloadTimer = 0;

        friendly = false;

    }

    public boolean reloaded()
    {
        return reloadTimer == 0;
    }

    public void setReloadTimer(double time)
    {
        reloadTimer = time;
    }

    public void reverseDirection()
    {
        dx = -dx;
    }

    public void nudgeDown()
    {
        y += 20;
    }

    public static boolean checkIfEdgeReached()
    {
        if (edgeReached) {
            edgeReached = false;
            return true;
        }
        else return false;
    }        

}
