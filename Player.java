public class Player extends SpaceEntity
{

    private double reloadTimer;

    public Player(double x, double y)
    {
        super(x, y, Application.SPRITE_SIZE / 2, 0);
        reloadTimer = 0;
        friendly = true;
    }

    @Override
    public void update(double frameLength)
    {
        x += dx * frameLength;
        y += dy * frameLength;

        if (x > Application.WINDOW_WIDTH - Application.SPRITE_SIZE / 2) x = Application.SPRITE_SIZE / 2;
        if (x < Application.SPRITE_SIZE / 2) x = Application.SPRITE_SIZE / 2;        

        dx *= (1 - 10 * frameLength);
        dy *= (1 - 10 * frameLength);

        if (reloadTimer > 0) reloadTimer -= frameLength;
        if (reloadTimer < 0) reloadTimer = 0;

    }

    public boolean reloaded()
    {
        return reloadTimer == 0;
    }

    public void setReloadTimer(double time)
    {
        reloadTimer = time;
    }

    public boolean alive()
    {
        return !expired;
    }

}
