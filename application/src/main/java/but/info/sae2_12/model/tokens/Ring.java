package but.info.sae2_12.model.tokens;

import but.info.sae2_12.model.Team;
public class Ring extends Token {
    public Ring(Team color) {
        super(color);
    }

    @Override
    public String charRepr() {
        return (team == Team.BLACK) ? "o" : "O";
    }

    @Override
    public Token clone() {
        return new Ring(team);
    }
}
