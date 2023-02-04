import java.util.ArrayList;
import java.util.List;

public class StoreMinions {
    private List<Minion> allMinions;
    private int numberOfMinions;

    public StoreMinions(){
        this.allMinions = new ArrayList<Minion>();
        this.numberOfMinions = 0;
    }

    public void addMinion(List<Object> minionInfo) {
        Minion minion = new Minion((String) minionInfo.get(0), (float) minionInfo.get(1));
        allMinions.add(minion);
        this.numberOfMinions+=1;
    }

    public void removeMinionAtIndex(int index){
        if(index!=-1) {
            allMinions.remove(index);
            this.numberOfMinions -= 1;
        }
    }

    public int getNumberOfMinions(){
        return this.numberOfMinions;
    }

    public Minion getMinionAtIndex(int i){
        return this.allMinions.get(i);
    }

    public void displayMinions(){
        System.out.println("\nList of Minions: " + "\n****************");
        int i = 1;
        for(Minion obj: allMinions){
            System.out.println(i + ". " + obj.getName() + ", " + obj.getHeight() + "m, " + obj.getNumberOfEvilDeeds() + " evil deed(s)");
            i++;
        }
    }
}
