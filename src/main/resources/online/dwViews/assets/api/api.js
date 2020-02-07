/**
 * API.js
 *
 * A module that allows encapsulation of the API calls for consumption in
 * other javascript files. Allows any modifications to be isolated to this
 * file so backend / frontend devs can work from 'one source of truth'.
 */

// API PATHS
const URL_GET_STATISTICS = "http://localhost:7777/toptrumps/retrieveStats";

// CORS REQUEST HELPER FUNCTIONS
// -----------------------------

function createCORSRequest(method, url) {
  var xhr = new XMLHttpRequest();
  if ("withCredentials" in xhr) {
    // Check if the XMLHttpRequest object has a "withCredentials" property.
    // "withCredentials" only exists on XMLHTTPRequest2 objects.
    xhr.open(method, url, true);
  } else if (typeof XDomainRequest != "undefined") {
    // Otherwise, check if XDomainRequest.
    // XDomainRequest only exists in IE, and is IE's way of making CORS requests.
    xhr = new XDomainRequest();
    xhr.open(method, url);
  } else {
    // Otherwise, CORS is not supported by the browser.
    xhr = null;
  }
  return xhr;
}

// Don't need to touch this, it's DRY :D
function apiGet(url, callback) {
  // First create a CORS request, this is the message we are going to send (a get request in this case)
  let xhr = createCORSRequest("GET", url); // Request type and URL

  // Message is not sent yet, but we can check that the browser supports CORS
  if (!xhr) {
    alert("CORS not supported");
  }
  // CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
  // to do when the response arrives
  xhr.onload = function() {
    const obj = JSON.parse(xhr.response);
    callback(obj);
  };
  // We have done everything we need to prepare the CORS request, so send it
  xhr.send();
}

// API FUNCTIONS
// -------------

/**
 * This function returns the game statistics as a javascript object/dictionary.
 * Format :
 * {
 * "ai_wins": 5,
 * "user_wins": 3,
 * "avg_draws": 4,
 * "tot_games_played": 7,
 * "max_rounds": 8
 * }
 */
function apiGetStatistics(callback) {
  apiGet(URL_GET_STATISTICS, callback);
}
