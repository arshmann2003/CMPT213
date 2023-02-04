public class Minion {
    private final String name;
    private final float height;
    private int numberOfEvilDeeds;

    public Minion(String name, float height){
        this.name = name;
        this.height = height;
        this.numberOfEvilDeeds = 0;
    }
    public String toString(){
        return getClass().getName() + " [Name:" + this.name + ", Evil Deeds:"
                + this.numberOfEvilDeeds + ", Height:" + this.height + "]";
    }
    public String getName(){
        return this.name;
    }
    public float getHeight(){
        return this.height;
    }
    public int getNumberOfEvilDeeds(){
        return this.numberOfEvilDeeds;
    }
    public void incrementEvilDeeds(){
        this.numberOfEvilDeeds+=1;
    }
}
