const MOCK_STATS = {
  ai_wins: 5,
  user_wins: 3,
  avg_draws: 4,
  tot_games_played: 7,
  max_rounds: 8,
};

function apiGetStatistics(callback) {
  callback(MOCK_STATS);
}
