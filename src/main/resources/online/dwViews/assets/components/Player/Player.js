// "CLASS" TEMPLATES

const attributeTemplate = (attribute) => {
  return `<li class="tt-attribute list-group-item d-flex justify-content-between align-items-center">
        <span class="tt-attribute-name">${attribute.name}</span>
        <span class="badge badge-primary badge-pill">${attribute.value}</span>
      </li>`;
};

const cardTemplate = (card) => {
  return `
    <div class="card-body">

        <div class="card-hider">
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

    <div class="card card-player mt-3">
      <div class="card-header">
        <div class="row ">
            <div class="col-12 d-flex justify-content-center">
                <h3>
                    <span class="tt-is-active badge">
                        ${icon}
                        ${iconActive}
                        ${player.name}
                    </span>
                    </h3>
                </div>
                <div class="col-12 d-flex justify-content-center">
                    <div class="col text-center">
                        <span class="tt-deck-size badge badge-light">
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

const PlayerFactory = (playerObj) => {
  // Store an internal reference to the overall player object via passed in variable

  // PRIVATE VARIABLES / CONSTRUCTOR (Uses a closure):
  // -----------------------
  // Create a jquery reference inside this 'class' as a private member (using closure)
  const $this = $(playerTemplate(playerObj));

  if (playerObj.isActive) {
    $this.find(".tt-is-active").addClass("badge-success");
  } else {
    $this.find(".tt-is-active").addClass("badge-warning");
  }

  const highlightAttribute = (attributeName, color) => {
    $this
      .find(`.tt-attribute:has(>.tt-attribute-name:contains(${attributeName}))`)
      .css("background-color", color)
      .css("color", "white");
  };

  // PUBLIC METHODS:
  const Player = {
    getName: () => {
      return playerObj.name;
    },
    isUser: () => {
      return playerObj.isAI ? false : true;
    },
    // Add to a dom element (by ID or class)
    attach: (container) => {
      $(container).append($this);
    },
    hideCard: () => {
      $this.find(".card-hider").addClass("card-hider-hide");
    },
    showCard: () => {
      $this.find(".card-hider").removeClass("card-hider-hide");
    },
    setWinner: (attributeName) => {
      highlightAttribute(attributeName, "green");
      $this
        .find(".tt-is-active")
        .removeClass("badge-danger")
        .removeClass("badge-warning")
        .addClass("badge-success");
    },
    setLoser: (attributeName) => {
      highlightAttribute(attributeName, "red");
      $this
        .find(".tt-is-active")
        .removeClass("badge-warning")
        .removeClass("badge-success")
        .addClass("badge-danger");
    },
    eliminate: () => {
      // Set header a violent red
      $this.find(".card-header").css("background-color", "red");

      // Set label to eliminated
      $this
        .find(".tt-is-active")
        .removeClass("badge-warning")
        .removeClass("badge-success")
        .addClass("badge-danger") // Overrides primary and warning
        .empty()
        .text(`${playerObj.name} IS OUT`);

      // Set deck size to red for emphasis on that fat 0.
      $this
        .find(".tt-deck-size")
        .removeClass("badge-light")
        .addClass("badge-danger");
    },
  };

  return Player;
};
