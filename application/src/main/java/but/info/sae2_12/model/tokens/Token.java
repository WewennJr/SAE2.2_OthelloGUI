package but.info.sae2_12.model.tokens;

import but.info.sae2_12.model.Team;

public abstract class Token {
    protected Team team;

    public Token(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    public abstract String charRepr();
    public abstract Token clone();
}
