/**
 * api-test.js
 *
 * A module that allows developing with test APIs and not relying on the server.
 */

// FUNCTIONS

// Get the statistics from the API
export const apiGetStats = async () => {
  return {};
};

// Post the user defined configuration to the API
export const apiInitGame = async numAIPlayers => {
  return true;
};

export const apiInitRound = async () => {
  return {
    players: [
      {
        name: 'USER',
        isAi: false,
        isActive: false,
        decksize: 20,
        card: {
          name: 'card',

          attributes: {
            strength: 14,
            stamina: 5,
          },
        },
      },
      {
        name: 'AI1',
        isAi: true,
        isActive: true,
        decksize: 10,
        card: {
          name: 'card2',
          attributes: {
            strength: 10,
            stamina: 5,
          },
        },
      },
    ],
  };
};

export const apiPlayRound = async attribute => {
  return {};
};
