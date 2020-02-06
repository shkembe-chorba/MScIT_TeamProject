/**
 * This file contains the javascript used to
 */

// NOTE - Uncomment / Comment to change from TEST data to SERVER data

const setupGame = () => {
  // Get the number of AI players chosen
};

const setupGame = async numAIPlayers => {
  const json = await apiInitRound();
  const players = parsePlayers(json);
};

export { setupGame, other };
