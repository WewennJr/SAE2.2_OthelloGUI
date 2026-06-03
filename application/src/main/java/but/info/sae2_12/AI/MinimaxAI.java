package but.info.sae2_12.AI;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.actions.Action;
import but.info.sae2_12.model.actions.RemoveLine;
import but.info.sae2_12.model.state.IState;
import coordinates.Coordinate;

import java.util.HashSet;
import java.util.Set;

public class MinimaxAI implements AI {

    /** Profondeur d'exploration dans l'arbre de jeu. */
    private final int depth;

    /** Équipe de l'IA. */
    private final Team aiTeam;

    /** Algorithme Minimax sous-jacent. */
    private final MiniMax miniMax;

    /**
     * Crée une IA Minimax.
     *
     * @param aiTeam L'équipe jouée par cette IA.
     * @param depth  La profondeur de recherche (plus la valeur est élevée, plus l'IA est forte mais lente).
     */
    public MinimaxAI(Team aiTeam, int depth) {
        this.aiTeam = aiTeam;
        this.depth = depth;
        this.miniMax = new MiniMax();
    }

    /**
     * Choisit le meilleur coup possible depuis l'état donné.
     *
     * <p>Si l'état contient des lignes à supprimer, cette méthode choisit automatiquement
     * le meilleur anneau à retirer.
     *
     * @param state L'état de jeu courant.
     * @return Un tableau {@code [from, to]} avec la coordonnée de départ et d'arrivée de l'anneau,
     *         ou {@code [ring, null]} si une ligne doit être supprimée.
     *         Retourne {@code null} si aucun coup n'est disponible.
     */
    @Override
    public Action chooseMove(IState state) {
        if (!state.getLines().isEmpty()) {
            return chooseBestLineRemoval(state);
        }

        return miniMax.bestMove(state, depth, aiTeam);
    }

    /**
     * Choisit le meilleur anneau à retirer lors d'une suppression de ligne.
     * Évalue chaque suppression possible et retourne celle qui maximise le score.
     *
     * @param state L'état avec des lignes à supprimer.
     * @return Un tableau {@code [ring, null]} indiquant l'anneau à retirer.
     */
    private RemoveLine chooseBestLineRemoval(IState state) {
        double bestScore = Double.NEGATIVE_INFINITY;
        Coordinate bestRing = null;
        Set<Coordinate> bestLines = new HashSet<>();
        
        for (Set<Coordinate> line : state.getLines()) {

            for (Coordinate ring : state.rings().get(aiTeam)) {
                try {
                    IState next = state.removeLine(new RemoveLine(line, ring));
                    double score = miniMax.evaluate(next, aiTeam);
                    if (score > bestScore) {
                        bestScore = score;
                        bestRing = ring;
                        bestLines = line;
                    }
                } catch (Exception ignored) {
                }
            }
        }

        return new RemoveLine(bestLines, bestRing);
    }

    /**
     * @return Le nom de cette IA avec sa profondeur.
     */
    @Override
    public String getName() {
        return "Minimax Alpha-Beta (depth=" + depth + ", team=" + aiTeam + ")";
    }

    /** @return La profondeur de recherche. */
    public int getDepth() { return depth; }

    /** @return L'équipe de l'IA. */
    public Team getAiTeam() { return aiTeam; }
}
