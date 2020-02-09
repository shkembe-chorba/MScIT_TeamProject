
<!-- Modal to start game -->
<div class="modal fade" id="newGameModal" tabindex="-1" role="dialog" aria-labelledby="newGameModal" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="">Choose the number of opponents you want to play with</h5>
                </button>
            </div>
            <div class="modal-body modal-lg">
                <div class="custom-control custom-radio">
                    <input type="radio" class="custom-control-input" id="select1" name="newGameModal-aiPlayers" value="1" checked>
                    <label class="custom-control-label" for="select1">1</label>
                </div>
                <div class="custom-control custom-radio">
                    <input type="radio" class="custom-control-input" id="select2" name="newGameModal-aiPlayers" value="2">
                    <label class="custom-control-label" for="select2">2</label>
                </div>
                <div class="custom-control custom-radio">
                    <input type="radio" class="custom-control-input" id="select3" name="newGameModal-aiPlayers" value="3">
                    <label class="custom-control-label" for="select3">3</label>
                </div>
                <div class="custom-control custom-radio">
                    <input type="radio" class="custom-control-input" id="select4" name="newGameModal-aiPlayers" value="4">
                    <label class="custom-control-label" for="select4">4</label>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="newGameModal-play">Start game</button>
                <button type="button" class="btn btn-secondary" id="newGameModal-abort">Close</button>
            </div>
        </div>
    </div>
</div>

<!-- END New Game Modal -->