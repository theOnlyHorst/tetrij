package at.theOnlyHorst.tetrij.util;

import java.util.Random;

public class RNG {
    private Random rnd;
    public RNG()
    {
        rnd = new Random();
    }

    public Bag get7Bag()
    {
        int[] tmp = new int[7];

        for(int i=0;i<tmp.length;i++)
        {
            tmp[i] = i;
        }

        int shuffles = rnd.nextInt(15);

        for(int i=0;i<shuffles;i++)
        {
            int x = rnd.nextInt(7);
            int y;
            do {
                 y= rnd.nextInt(7);
            }while (x==y);
            int tri = tmp[x];
            tmp[x] = tmp[y];
            tmp[y] = tri;

        }


        return Bag.createNewBag(tmp);
    }
}
