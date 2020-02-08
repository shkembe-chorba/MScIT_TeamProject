const PlayButtonFactory = (attributes) => {
  // CLASS NAMES
  // -----------

  const NEXT_ROUND_BUTTON = "tt-next-round-button";
  const PLAY_ROUND_BUTTON = "tt-play-round-button";
  const DROPDOWN_BUTTON = "tt-dropdown-button";
  const ATTRIBUTE = "tt-attribute-selector";
  const ATTRIBUTE_LIST = "tt-attribute-list";

  // TEMPLATES
  // ---------

  // Wrap in a div so our Jquery object only references one thing!
  const buttonTemplate = () => `
      <div>
          <button type="button" class="${PLAY_ROUND_BUTTON} btn btn-primary"> Play round </button>
          <button type="button" class="${NEXT_ROUND_BUTTON} btn btn-warning"> Next round </button>
          <button type="button" class="${DROPDOWN_BUTTON} btn btn-primary dropdown-toggle"  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" aria-hidden="true">
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

  // CONSTRUCTOR / PRIVATE VARIABLES
  // -------------------------------

  // Create jquery reference referencing the button template.
  const $this = $(buttonTemplate());

  // It's probably better to search locally for classes inside THIS's div, as IDs may create conflicts in the
  // long term on the page in general.
  const $nextRoundButton = $this.find("." + NEXT_ROUND_BUTTON);
  const $playRoundButton = $this.find("." + PLAY_ROUND_BUTTON);
  const $dropdownButton = $this.find("." + DROPDOWN_BUTTON);
  const $attributeList = $this.find("." + ATTRIBUTE_LIST);

  // This will store the function the user wants to be calledback on a click of an attribute.
  let userCallback = (attributeName) => {
    return null;
  };

  // Public methods
  const PlayButton = {
    // Show the next round button, hide the others

    setNextRoundButton: () => {
      $playRoundButton.hide();
      $nextRoundButton.show();
      $dropdownButton.hide();
    },

    // Set the nextround callback function
    onNextRoundClick: (callback) => {
      $nextRoundButton.click(callback);
    },

    // Show the play round button, hide the others

    setPlayRoundButton: () => {
      $playRoundButton.show();
      $nextRoundButton.hide();
      $dropdownButton.hide();
    },


    // Set the playround callback function
    onPlayRoundClick: (callback) => {
      $playRoundButton.click(callback);
    },


    // Show the attribute button, hide the others

    setAttributeButton: () => {
      $playRoundButton.hide();
      $nextRoundButton.hide();
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
