const PlayerButtonFactory = (attributes) => {
  // TEMPLATES
  // Stored internally in this function to prevent conflicts with other templates, e.g. attributeTemplate.

  // Attribute template
  const attributeTemplate = (attributeName) => {
    return `<a id="tt-attribute-selector" class="dropdown-item">${attributeName}</a>`;
  };

  // Wrap in a div so our Jquery object only references one thing!
  const buttonTemplate = (attributes) => `
      <div>
          <button type="button" class="tt-round-button btn btn-primary"> Play round </button>
          <button type="button" class="tt-dropdown-button btn btn-primary dropdown-toggle"  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" aria-hidden="true">
              Play round
          </button>
          <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
              ${attributes.map((a) => attributeTemplate(a.name)).join("")}
          </div>
      </div>
      `;

  // PRIVATE VARIABLES / CONSTRUCTOR FUNCTIONS

  // Create jquery reference referencing the button template.
  const $this = $(playerTemplate(attributes));

  // It's probably better to search locally for classes inside THIS's div, as IDs may create conflicts in the
  // long term on the page in general.
  const $roundButton = this.find(".tt-round-button");
  const $dropdownButton = this.find(".tt-round-button");
  const $attributes = this.find(".tt-attribute-selector");

  // Setup an on-click for each of our attributes

  // This will store the function the user wants to be calledback on a click of an attribute.
  let userCallback;

  // For each Jquery attribute
  $attributes.each(function() {
    // Get the jquery attribute
    const $attribute = this;
    // When the user clicks this attribute, it calls the 'userCallback' set in the public method below.
    // It is passed the contents of the attribute anchor (the attribute name).
    this.onClick(function() {
      userCallback($attribute.text().trim());
    });
  });

  // Public methods
  return {
    // Todo
    setRoundButton: () => {},
    // Todo
    setAttributeButton: () => {},
    // Append to the target div id / class / ...
    attach: (target) => {
      $(target).append($this);
    },
    // Sets the user callback function when an attribute is clicked.
    // The callback should take the attribute name as a parameter.
    onAttributeClick: (callback) => {
      userCallback = callback;
    },
  };
};
