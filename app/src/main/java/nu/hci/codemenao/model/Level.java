package nu.hci.codemenao.model;
import android.util.Pair;


public class Level {
    private int levelNo;
    private Pair startingPos;
    private Pair finishPos;
    private String direction;

    public Level(int sv,int sh, int fv,int fh, String dir){
        startingPos = new Pair(sv,sh);
        finishPos = new Pair(fv,fh);
        direction = dir;
    }


    public Pair getStartingPos(){
        return startingPos;
    }
    public Pair getFinishPos(){
        return finishPos;
    }
    public String getDirection(){
        return direction;
    }



}
