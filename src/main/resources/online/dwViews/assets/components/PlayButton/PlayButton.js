const PlayButtonFactory = (attributes) => {
  // CLASS NAMES
  // -----------

  const NEXT_ROUND_BUTTON = "tt-next-round-button";
  const PLAY_ROUND_BUTTON = "tt-play-round-button";
  const GAME_OVER_BUTTON = "tt-game-over-button";
  const DROPDOWN_BUTTON = "tt-dropdown-button";
  const ATTRIBUTE = "tt-attribute-selector";
  const ATTRIBUTE_LIST = "tt-attribute-list";

  // TEMPLATES
  // ---------

  // Wrap in a div so our Jquery object only references one thing!
  const buttonTemplate = () => `
      <div>
          <button type="button" class="${PLAY_ROUND_BUTTON} btn btn-lg btn-primary"> Play round </button>
          <button type="button" class="${NEXT_ROUND_BUTTON} btn btn-lg btn-warning"> Next round </button>
          <button type="button" class="${GAME_OVER_BUTTON} btn btn-lg btn-danger"> Get Scores </button>
          <button type="button" class="${DROPDOWN_BUTTON} btn btn-lg btn-primary dropdown-toggle"  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" aria-hidden="true">
              Play round
          </button>
          <div class="${ATTRIBUTE_LIST} dropdown-menu" aria-labelledby="dropdownMenuButton">
          <!-- Attributes inserted here -->
          </div>
      </div>
      `;

  const attributeTemplate = (attributeName) => {
    return `<a class="${ATTRIBUTE} dropdown-item">${attributeName}</a>`;
  };

  // PRIVATE VARIABLES
  // -------------------------------

  // Create jquery reference referencing the button template.
  const $this = $(buttonTemplate());

  // It's probably better to search locally for classes inside THIS's div, as IDs may create conflicts in the
  // long term on the page in general.
  const $nextRoundButton = $this.find("." + NEXT_ROUND_BUTTON);
  const $playRoundButton = $this.find("." + PLAY_ROUND_BUTTON);
  const $gameOverButton = $this.find("." + GAME_OVER_BUTTON);
  const $dropdownButton = $this.find("." + DROPDOWN_BUTTON);

  const buttonArray = [
    $nextRoundButton,
    $playRoundButton,
    $gameOverButton,
    $dropdownButton,
  ];

  const $attributeList = $this.find("." + ATTRIBUTE_LIST);

  // This will store the function the user wants to be calledback on a click of an attribute.
  let userCallback = (attributeName) => {
    return null;
  };

  // PRIVATE FUNCTIONS

  const hideButtons = () => {
    buttonArray.forEach((b) => b.hide());
  };

  // CONSTRUCTOR

  hideButtons();

  // Public methods
  const PlayButton = {
    // Show the next round button, hide the others

    setNextRoundButton: () => {
      hideButtons();
      $nextRoundButton.show();
    },

    // Set the nextround callback function
    onNextRoundClick: (callback) => {
      $nextRoundButton.click(callback);
    },

    // Show the play round button, hide the others

    setPlayRoundButton: () => {
      hideButtons();
      $playRoundButton.show();
    },

    // Set the playround callback function
    onPlayRoundClick: (callback) => {
      $playRoundButton.click(callback);
    },

    setGameOverButton: () => {
      hideButtons();
      $gameOverButton.show();
    },

    onGameOverClick: (callback) => {
      $gameOverButton.click(callback);
    },

    // Show the attribute button, hide the others
    setAttributeButton: () => {
      hideButtons();
      $dropdownButton.show();
    },

    // Sets the user callback function when an attribute is clicked.
    // The callback should take the attribute name as a parameter.
    onAttributeClick: (callback) => {
      userCallback = callback;
    },

    // Add attribute,
    addAttribute: (attributeName) => {
      // Create an attribute dom element
      const $attribute = $(attributeTemplate(attributeName));
      // Add a click handler that calls the 'userCallback' function when clicked, passing it the attributeName
      $attribute.click(() => {
        userCallback(attributeName);
      });
      // Add it to our attribute list
      $attributeList.append($attribute);
    },

    // Clear all attributes
    clearAttributes: () => {
      $attributeList.empty();
    },

    // Append to the target div id / class / ...
    attach: (target) => {
      $(target).append($this);
    },
  };

  return PlayButton;
};
