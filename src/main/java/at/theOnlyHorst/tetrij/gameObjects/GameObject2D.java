package at.theOnlyHorst.tetrij.gameObjects;

public class GameObject2D {


    private float locX;
    private float locY;

    private GameObject2D parent;

    public GameObject2D(float x,float y, GameObject2D parent)
    {
        this.locX = x;
        this.locY = y;

        this.parent = parent;
    }

    public GameObject2D(float x, float y)
    {
        this(x,y,null);
    }


    public float getLocX() {
        return locX;
    }

    public float getLocY() {
        return locY;
    }

    public float getAbsX()
    {
        if(parent==null)
        {
            return locX;
        }
        return parent.getAbsX()+locX;
    }

    public float getAbsY()
    {
        if(parent==null)
        {
            return locY;
        }
        return parent.getAbsY()+locY;
    }

    public void transformPos(float x,float y)
    {
        locX = x;
        locY = y;
        onRecalculate();
    }

    public void transformPosMove(float x,float y)
    {
        locX +=x;
        locY +=y;
        onRecalculate();
    }

    protected void onRecalculate()
    {
    }

    protected GameObject2D getParent()
    {
        return parent;
    }
}
