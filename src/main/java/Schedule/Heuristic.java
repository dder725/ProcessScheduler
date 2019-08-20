package Schedule;

public class Heuristic {
    private int _cost = 0;

    public Heuristic(State state){
        this._cost = state.getlastScheduledNode().getBottomLevel();
        
    }

    public int getCost(){
        return _cost;
    }
}
