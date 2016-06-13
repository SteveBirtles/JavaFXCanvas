
public class Beam extends SpaceEntity
{

    public Beam(double x, double y, int sprite, double dx, double dy, boolean friendly)
    {
        super(x, y, Application.BEAM_SIZE / 2, sprite);
        this.dx = dx;
        this.dy = dy;
        this.friendly = friendly;
    }

    @Override
    public void update(double frameLength)
    {
        super.update(frameLength);
        if (x > Application.WINDOW_WIDTH + Application.BEAM_SIZE / 2)  expired = true;
        if (x < -Application.BEAM_SIZE / 2) expired = true;
        if (y > Application.WINDOW_WIDTH + Application.BEAM_SIZE/ 2) expired = true; 
        if (y < -Application.BEAM_SIZE / 2) expired = true;
    }

}
