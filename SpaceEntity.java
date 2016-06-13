import java.util.Set;
import java.util.Iterator;

public abstract class SpaceEntity
{

    protected int sprite;
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    protected boolean expired;
    protected double r;
    protected boolean friendly;

    public SpaceEntity(double x, double y, double r, int sprite)
    {
        this.x = x;
        this.y = y;        
        this.sprite = sprite;        
        this.r = r;        
    }

    public void setVelocity(double dx, double dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }    

    public int getSprite()
    {
        return sprite;
    }

    public void update(double frameLength)
    {
        x += dx * frameLength;
        y += dy * frameLength;
    }

    public static void clearUpExired(Set<SpaceEntity> entities)
    {
        Iterator<SpaceEntity> expiredEntityRemover = entities.iterator();
        while (expiredEntityRemover.hasNext()) {
            SpaceEntity s = expiredEntityRemover.next(); 
            if (s.expired) expiredEntityRemover.remove();
        }
    }

    public boolean collidesWith(SpaceEntity other)
    {
        if (expired || other.expired || friendly == other.friendly) return false;
        if (Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) < Math.pow(r + other.r, 2))
        {
            other.expired = true;
            return true;
        }
        return false;
    }

}