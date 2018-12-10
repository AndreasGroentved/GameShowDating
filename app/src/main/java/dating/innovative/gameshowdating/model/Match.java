package dating.innovative.gameshowdating.model;

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

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
