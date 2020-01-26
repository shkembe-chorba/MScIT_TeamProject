package model;

public class AIPlayer extends Player {

    public AIPlayer(String name) {
        super(name);
    }

    public void chooseAttribute() {
        Card topCardValues [] = cardValues(peekCard);
        int max = 0;
        int i = 0;

        for (int i = 0; i < 5; i++) {
            if (max < values[i]) {
                max = values[i]; }
            }
        return i;
        }

    }






}
