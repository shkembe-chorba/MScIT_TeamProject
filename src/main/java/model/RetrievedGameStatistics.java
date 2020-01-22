package model;

/**
 * This immutable class contains the overall game stats, passed in the constructor
 */
public class RetrievedGameStatistics {

    private final int totalGamesPlayed;
    private final int gamesWonByAi;
    private final int gamesWonByUser;
    private final double avgDraws;
    private final int maxRounds;

    /**
     * CONSTRUCTOR
     *
     * @param totalGamesPlayed - overall number of games played
     * @param gamesWonByAi - number of games won by the AIs
     * @param gamesWonByUser - number of games won by the user
     * @param avgDraws - number of draws on average
     * @param maxRounds - maximum number of rounds
     */
    public RetrievedGameStatistics(int totalGamesPlayed,
                                   int gamesWonByAi,
                                   int gamesWonByUser,
                                   double avgDraws,
                                   int maxRounds) {
        this.totalGamesPlayed = totalGamesPlayed;
        this.gamesWonByAi = gamesWonByAi;
        this.gamesWonByUser = gamesWonByUser;
        this.avgDraws = avgDraws;
        this.maxRounds = maxRounds;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public int getGamesWonByAi() {
        return gamesWonByAi;
    }

    public int getGamesWonByUser() {
        return gamesWonByUser;
    }

    public double getAvgDraws() {
        return avgDraws;
    }

    public int getMaxRounds() {
        return maxRounds;
    }
}
