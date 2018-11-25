package dating.innovative.gameshowdating;

public class Match {

    public String nameOne;
    public String nameTwo;

    public Match(){}

    public Match(String nameOne, String nameTwo){
        this.nameOne = nameOne;
        this.nameTwo = nameTwo;
    }

    public String getNameOne() {
        return nameOne;
    }

    public void setNameOne(String nameOne) {
        this.nameOne = nameOne;
    }

    public String getNameTwo() {
        return nameTwo;
    }

    public void setNameTwo(String nameTwo) {
        this.nameTwo = nameTwo;
    }
}
