package model;

/**
 * AIPlayer.java
 *
 * Class inheriting from Player, used to initiate AIPlayers
 * Allows with method to choose index of the attribute that
 * AI should play with (highest value) and authomatically
 * chooses for them
 */
    public class AIPlayer extends Player {
        public AIPlayer(String name) {
        super(name);
    }

    /**
     * Method that chooses the highest value for AI to play with
     * @return index of the Attribute - this is because also player
     * will be returning the index of the attribute
     * This will be passed to the GameModel
     */

    public int chooseAttribute() {
        int [] attributeValues = peekCard().getCategoryValues();
        int max = 0;
        int index = 0;

        for (int i = 0; i < 5; i++) {
            if (max < attributeValues[i]) {
                max = attributeValues[i]; }
            index = i;
        }
        return index;
    }

}