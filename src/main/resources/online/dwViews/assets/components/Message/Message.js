/**
 * A psuedo class that uses closures to encapsulate information and returns an object
 * with all the public methods.
 *
 * Javascript that controls the main message area.
 */

const MessageFactory = (attributes) => {
    // CLASS NAMES
    // -----------

    const PLAY_ROUND_ATTRIBUTE = "tt-play-round-attribute";
    const PLAY_ROUND_WINNER = "tt-play-round-winner";


    const messageTemplate = () => `
      <div>
          <h5 class="card-title">The chosen attribute was <strong>${PLAY_ROUND_ATTRIBUTE}</strong></h5> 
		  <h5 class="card-title">The winner of the round is <strong>${PLAY_ROUND_WINNER}</strong>.</h5>		 
      </div>
      `;

    // PRIVATE VARIABLES
    // -------------------------------

    const $this = $(messageTemplate());

    // Public methods
    return {
        setMessage: () => {
         PLAY_ROUND_ATTRIBUTE.text(`${roundWinnerName.topCard}`)
         PLAY_ROUND_WINNER.text(`${roundWinnerName}`)
        }
    }
}
