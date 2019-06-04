package at.theOnlyHorst.tetrij.gameObjects.tetrij;

import at.theOnlyHorst.tetrij.engine.ResourceManager;
import at.theOnlyHorst.tetrij.gameObjects.GameSprite;
import at.theOnlyHorst.tetrij.renderer.Screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid extends GameSprite {

    private Map<Integer,Mino> gridMap;


    private static final float GRID_POS_X_0 = -0.3361f;
    private static final float GRID_POS_Y_0 = 0.861f;

    private static final float GRID_OFFSET = 0.075f;

    private static final int MAX_FIELD = 219;

    private Screen displayScreen;

    private TetriMino activeTetriMino;

    private int activeTetriMinoGridPos;

    public Grid(float x, float y,Screen screen)
    {
        super(x,y,1.5f,1.5f, ResourceManager.getTexture("bgGrid"));
        gridMap = new HashMap<>();
        displayScreen = screen;
        displayScreen.getSpriteList().add(this);
    }

    public Grid(Screen screen)
    {
        this(0,0,screen);
    }


    public float gridPosToLocX(int gridPos)
    {
        float tmp = GRID_POS_X_0;
        for (int i=0;i<gridPos%10;i++)
        {
            tmp+=GRID_OFFSET;
        }
        return tmp;
    }

    public float gridPosToLocY(int gridPos)
    {
        float tmp = GRID_POS_Y_0;
        for (int i=0;i<gridPos/10;i++)
        {
            tmp-=GRID_OFFSET;
        }
        return tmp;
    }

    public void addMino(int gridPos, Mino.MinoColor color)
    {
        Mino mino = new Mino(gridPos, color);
        gridMap.put(gridPos,mino);
        displayScreen.getSpriteList().add(mino);
    }

    public void moveMino(int gridPosOld,int gridPosNew)
    {
        Mino old =gridMap.get(gridPosOld);

        if(old ==null)
            return;
        Mino newM = gridMap.get(gridPosNew);
        if(newM!=null)
            return;
        gridMap.put(gridPosOld,null);
        gridMap.put(gridPosNew,old);

        old.transformPos(gridPosToLocX(gridPosNew),gridPosToLocY(gridPosNew));
    }




    public void moveTetriMino(int gridPos)
    {
        List<Mino> minoList = activeTetriMino.getAllMinos();
        List<Integer> relPos = activeTetriMino.getTemplate().getRelPosList();
        List<Integer> minoPoses = new ArrayList<>();
        for(int i =0; i<minoList.size();i++)
        {
            int minoPos = gridPos+relPos.get(i);
            minoPoses.add(minoPos);

        }
        boolean allowedMove = true;
        for(Integer x :minoPoses)
        {
            if(x<0||x>MAX_FIELD||(activeTetriMinoGridPos%10==0&&x%10==9)||(activeTetriMinoGridPos%10==9&&x%10==0))
            {
                allowedMove = false;
                break;
            }
        }

        if(allowedMove)
        {
            for(int i =0; i<minoList.size();i++)
            {
                minoList.get(i).transformPos(gridPosToLocX(minoPoses.get(i)),gridPosToLocY(minoPoses.get(i)));
            }
            activeTetriMinoGridPos = gridPos;
        }

    }

    public void moveTetriMinoRel(int relGridPos)
    {
        moveTetriMino(activeTetriMinoGridPos+relGridPos);
    }

    public boolean tetriMinoHasHitGround()
    {
        return tetriMinoHasHitGround(activeTetriMinoGridPos);
    }

    public boolean tetriMinoHasHitGround(int onPos)
    {
        boolean minoBelow;

        minoBelow = gridMap.get(onPos+10)!=null;
        minoBelow = minoBelow||gridMap.get(onPos+10+activeTetriMino.getRelPosMin2())!=null;
        minoBelow = minoBelow||gridMap.get(onPos+10+activeTetriMino.getRelPosMin3())!=null;
        minoBelow = minoBelow||gridMap.get(onPos+10+activeTetriMino.getRelPosMin4())!=null;

        return onPos+10>MAX_FIELD||minoBelow;
    }




    public void spawnTetriMino(TetriMinoTemplate tetriMino)
    {
        if(activeTetriMino==null) {
            activeTetriMino = TetriMino.createTetrimino(14, tetriMino);
            displayScreen.getSpriteList().addAll(activeTetriMino.getAllMinos());
            displayScreen.redraw();
            activeTetriMinoGridPos = 14;
        }
    }

    public void lockTetriMinoOnGrid()
    {
        gridMap.put(activeTetriMinoGridPos,activeTetriMino.getMino1());
        gridMap.put(activeTetriMinoGridPos+activeTetriMino.getRelPosMin2(),activeTetriMino.getMino2());
        gridMap.put(activeTetriMinoGridPos+activeTetriMino.getRelPosMin3(),activeTetriMino.getMino3());
        gridMap.put(activeTetriMinoGridPos+activeTetriMino.getRelPosMin4(),activeTetriMino.getMino4());
        activeTetriMino = null;
    }

}
