/**
 * A psuedo class that uses closures to encapsulate information and returns an object
 * with all the public methods.
 *
 * @param {Object} playerObj - the player object passed by the api
 */

const PlayerFactory = (playerObj) => {
  // CLASS NAMES
  // -----------
  const NAME_BADGE = "tt-is-active";
  const ATTRIBUTE_NAME = "tt-attribute-name";
  const CARD_COVER = "card-hider";
  const CARD_HEADER = "tt-card-header";
  const DECK_SIZE = "tt-deck-size";

  // TEMPLATES
  // ---------
  const attributeTemplate = (attribute) => {
    return `<li class="list-group-item d-flex justify-content-between align-items-center">
          <span class="${ATTRIBUTE_NAME}">${attribute.name}</span>
          <span class="badge badge-primary badge-pill">${attribute.value}</span>
        </li>`;
  };

  const cardTemplate = (card) => {
    return `
      <div class="card-body">
          <div class="${CARD_COVER}">
              <div class="row align-items-center" style="height: 100%">
                  <div class="col text-center">
                      <h3>Top Trumps</h3>
                  </div>
              </div>
          </div>
          <h4 class="card-title">${card.name}</h4>
          <ul class="list-group list-group-flush">
              ${card.attributes.map((a) => attributeTemplate(a)).join("")}
          </ul>
      </div>
      `;
  };

  const playerTemplate = (player) => {
    const icon = player.isAI
      ? `<i class="fa fa-desktop" aria-hidden="true"></i>`
      : `<i class="fa fa-user" aria-hidden="true"></i>`;

    const iconActive = player.isActive
      ? `<i class="fa fa-star" aria-hidden="true"></i>`
      : ``;

    return `
      <div class="card card-player align-self-center mt-3">
        <div class="${CARD_HEADER} card-header">
          <div class="row">
              <div class="col-12 d-flex justify-content-center">
                  <h3>
                      <span class="${NAME_BADGE} badge">
                          ${icon}
                          ${iconActive}
                          ${player.name}
                      </span>
                      </h3>
                  </div>
                  <div class="col-12 d-flex justify-content-center">
                      <div class="col text-center">
                          <span class="${DECK_SIZE} badge badge-light">
                              Cards in deck:
                              <span class="badge badge-primary badge-pill">${
                                player.deckSize
                              }</span>
                          </span>
                      </div>
                  </div>
               </div>
            </div>
              ${cardTemplate(player.topCard)}
      </div>
      `;
  };

  // PRIVATE VARIABLES
  // -----------------
  // Create a jquery reference inside this 'class' as a private member (using closure)
  const $this = $(playerTemplate(playerObj));

  const $nameBadge = $this.find("." + NAME_BADGE);
  const $cardCover = $this.find("." + CARD_COVER);
  const $cardHeader = $this.find("." + CARD_HEADER);
  const $deckSize = $this.find("." + DECK_SIZE);

  // PRIVATE METHODS
  // ---------------

  const highlightAttribute = (attributeName, color) => {
    $this
      // Find the attribute field that has the attribute name containing the desired name
      .find(`.${ATTRIBUTE_NAME}:contains(${attributeName})`)
      .parent() // Get the parent list element and colorize it
      .css("background-color", color)
      .css("color", "white");
  };

  // CONSTRUCTOR
  // -----------

  // If the player is active, change namebadge colour to green, otherwise yellow
  if (playerObj.isActive) {
    $nameBadge.addClass("badge-success");
  } else {
    $nameBadge.addClass("badge-warning");
  }

  // PUBLIC METHODS
  // --------------

  return {
    // Get the owning player's name
    getName: () => {
      return playerObj.name;
    },
    // Return if the player is a user
    isUser: () => {
      return playerObj.isAI ? false : true;
    },
    // Add to a dom element (passed by ID or class as a parameter, e.g. #card-deck)
    attach: (container) => {
      $(container).append($this);
    },
    // Makes the cardCover visible (see .card-hider in css)
    // Effectively covers the contents of a card from view
    hideCard: () => {
      $cardCover.addClass("card-hider-hide");
    },
    // Makes the card attributes visible by removing the css class
    showCard: () => {
      $cardCover.removeClass("card-hider-hide");
    },
    // Highlights the namebadge green to show it is a winner.
    // Also highlights the winning attribute green.
    setWinner: (attributeName) => {
      highlightAttribute(attributeName, "green");
      $nameBadge
        .removeClass("badge-danger")
        .removeClass("badge-warning")
        .addClass("badge-success");
    },
    // Highlights the namebadge red to show it is a loser.
    // Also highlights the winning attribute red.
    setLoser: (attributeName) => {
      highlightAttribute(attributeName, "red");
      $nameBadge
        .removeClass("badge-warning")
        .removeClass("badge-success")
        .addClass("badge-danger");
    },
    // As above, but a lot more violent. Sets the whole header red
    // and displays an IS OUT message in the name badge
    eliminate: () => {
      // Set header a violent red
      $cardHeader.css("background-color", "red");

      // Set label to eliminated
      $nameBadge
        .removeClass("badge-warning")
        .removeClass("badge-success")
        .addClass("badge-danger") // Overrides primary and warning
        .empty()
        .text(`${playerObj.name} IS OUT`);

      // Set deck size to red for emphasis on that fat 0.
      $deckSize.removeClass("badge-light").addClass("badge-danger");
    },
  };
};
