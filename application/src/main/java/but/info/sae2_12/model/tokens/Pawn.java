package but.info.sae2_12.model.tokens;

import but.info.sae2_12.model.Team;

public class Pawn extends Token {
    public Pawn(Team color) {
        super(color);
    }

    @Override
    public String charRepr() {
        return ".";
    }

    @Override
    public Token clone() {
        return new Pawn(team);
    }

    public void changeTeam() {
        this.team = (team == Team.BLACK) ? Team.WHITE : Team.BLACK;
    }
}
