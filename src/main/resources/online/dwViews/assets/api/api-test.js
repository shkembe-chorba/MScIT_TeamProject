const MOCK_STATS = {
  aiWins: 5,
  userWins: 3,
  avgDraws: 4,
  totGamesPlayed: 7,
  maxRounds: 8,
};

function apiGetStatistics(callback) {
  callback(MOCK_STATS);
}
