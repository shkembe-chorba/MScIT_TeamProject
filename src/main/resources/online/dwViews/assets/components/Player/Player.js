const TEST_JSON = {
  players: [
    {
      name: 'USER',
      isAI: false,
      isActive: true,
      deckSize: 5,
      card: {
        name: 'Card',
        attributes: [
          {
            name: 'test attribute',
            value: 12,
          },
          {
            name: 'test attribute 2',
            value: 5,
          },
        ],
      },
    },
    {
      name: 'AI1',
      isAI: true,
      isActive: false,
      deckSize: 15,
      card: {
        name: 'AI Card',
        attributes: [
          {
            name: 'test attribute',
            value: 4,
          },
          {
            name: 'test attribute 2',
            value: 20,
          },
        ],
      },
    },
  ],
};

const attributeTemplate = attribute => {
  return `
      <li class="list-group-item d-flex justify-content-between align-items-center">
        ${attribute.name}
        <span class="badge badge-primary badge-pill">${attribute.value}</span>
      </li>
    `;
};

const cardTemplate = card => {
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
            ${card.attributes.map(a => attributeTemplate(a))}
        </ul>
    </div>
    `;
};

const playerTemplate = player => {
  return `
    <div class="card">
      <div class="card-header">
        <div class="row ">
            <div class="col-12 d-flex justify-content-center">
                <h3>
                    <span class="badge badge-success">
                        <i class="fa fa-user" aria-hidden="true"></i>
                        ${player.name}
                        <i class="fa fa-star" aria-hidden="true"></i>
                    </span>
                </h3>
            </div>
            <div class="col-12 d-flex justify-content-center">
                <span class="badge badge-light">
                    Cards in deck: <span class="badge badge-primary badge-pill">${
                      player.deckSize
                    }</span>
                </span>
            </div>
        </div>
    </div>
    ${cardTemplate(player.card)}
  </div>
    `;
};

const PlayerFactory = playerObj => {
  // Store an internal reference to the overall player object via passed in variable

  // PRIVATE VARIABLES / Constructor (Uses a closure):
  // -----------------------
  // Create a jquery reference inside this 'class' as a private member (using closure)
  const $player = $(playerTemplate(playerObj));

  // Returns an object of this 'Class' with the public methods below:
  const Player = {
    getObject: () => {
      return playerObj;
    },
    getJqueryObject: () => {
      return $player;
    },
    isUser: () => {
      return playerObj.isAI ? false : true;
    },
    // Add to a dom element (by ID or class)
    attach: container => {
      $(container).append($player);
    },
    hideCard: () => {
      $player.find('.card-hider').addClass('card-hider-hide');
    },
    showCard: () => {
      $player.find('.card-hider').removeClass('card-hider-hide');
    },
  };

  return Player;
};
