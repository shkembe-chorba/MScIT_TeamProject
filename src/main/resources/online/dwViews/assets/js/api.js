/**
 * API.js
 *
 * A module that allows encapsulation of the API calls for consumption in
 * other javascript files. Allows any modifications to be isolated to this
 * file so backend / frontend devs can work from 'one source of truth'.
 */

// API PATHS
const STATS_API = 'api/stats';
const GAME_SETUP_API = 'api/setup';
const GAME_NEW_ROUND_API = 'api/startround';

// FUNCTIONS

// Get the statistics from the API
export const apiGetStats = async () => {
  const response = await fetch(STATS_API, {});

  const json = await response.json();
};

// Post the user defined configuration to the API
export const apiInitGame = async numAIPlayers => {
  // Send the request for the number of AI players in JSON
  const requestBody = { numAIPlayers };
  const response = await fetch(GAME_SETUP_API, {
    method: 'POST',
    body: JSON.stringify(requestBody),
  });

  const json = await response.json();

  // If we get a response, the game is registered and initialised on the server.
  return json ? true : false;
};

export const apiInitRound = async () => {
  const response = await fetch(GAME_NEW_ROUND_API);
  const json = await response.json();
  return json;
};

export const apiPlayRound = async attribute => {
  // Send the request for the number of AI players in JSON
  const requestBody = { attribute };
  const response = await fetch(GAME_SETUP_API, {
    method: 'POST',
    body: JSON.stringify(requestBody),
  });

  const json = await response.json();
  return json;
};
