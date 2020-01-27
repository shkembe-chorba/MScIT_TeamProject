package model;

public class AIPlayer extends Player {

    public AIPlayer(String name) {
        super(name);
    }


    // 
    public int chooseAttribute() {
        int [] topCardValues = peekCard().getCategoryValues();
        int max = 0;
        int index = 0;

        for (int i = 0; i < 5; i++) {
            if (max < topCardValues[i]) {
                max = topCardValues[i]; }
            index = i;
        }
        return index;
    }

}