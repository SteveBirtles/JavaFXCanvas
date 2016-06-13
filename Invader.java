public class Invader extends SpaceEntity
{

    private static boolean edgeReached = false;

    public Invader(double x, double y, int sprite)
    {
        super(x, y, Application.SPRITE_SIZE / 2, sprite);
        dx = 0;
        dy = 0;
    }

    @Override
    public void update(double frameLength)
    {
        super.update(frameLength);
        if (x > Application.WINDOW_WIDTH - Application.SPRITE_SIZE / 2) edgeReached = true;
        if (x < Application.SPRITE_SIZE / 2) edgeReached = true;
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
