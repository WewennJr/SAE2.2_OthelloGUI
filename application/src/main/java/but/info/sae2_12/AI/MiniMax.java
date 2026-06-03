package but.info.sae2_12.AI;

import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.actions.Move;
import but.info.sae2_12.model.actions.RemoveLine;
import but.info.sae2_12.model.state.IState;
import but.info.sae2_12.model.tokens.Pawn;
import coordinates.Coordinate;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;
import java.util.Set;

/**
 * Implémentation de l'algorithme Minimax avec élagage Alpha-Bêta.
 *
 * <p>L'élagage Alpha-Bêta permet de réduire significativement le nombre de noeuds
 * explorés dans l'arbre de jeu tout en produisant le même résultat que Minimax classique.
 *
 * <h2>Principe</h2>
 * <ul>
 *   <li>Le joueur maximisant cherche à maximiser son score (l'IA).</li>
 *   <li>Le joueur minimisant cherche à minimiser ce score (l'adversaire).</li>
 *   <li>L'élagage coupe les branches dès qu'elles ne peuvent plus influencer la décision racine.</li>
 * </ul>
 *
 * <h2>Fonction d'évaluation</h2>
 * Le score d'un état est calculé ainsi :
 * <ul>
 *   <li>+1000 par anneau retiré par l'IA (victoire proche si 3 retirés).</li>
 *   <li>-1000 par anneau retiré par l'adversaire.</li>
 *   <li>+10  par pion de l'IA sur le plateau.</li>
 *   <li>-10  par pion de l'adversaire sur le plateau.</li>
 *   <li>+50  par ligne de 4 pions de l'IA</li>
 *   <li>-50  par ligne de 4 pions de l'adversaire.</li>
 *   <li>Victoire / Défaite terminale : ±100 000.</li>
 * </ul>
 */
public class MiniMax {

    /**
     * Nombre d'anneaux initiaux par équipe.
     */
    private static final SimpleIntegerProperty INITIAL_RINGS = new SimpleIntegerProperty(5);

    /**
     * Score absolu pour une fin de partie.
     */
    private static final SimpleDoubleProperty WIN_SCORE =  new SimpleDoubleProperty(100_000.0);

    /**
     * Poids d'un anneau retiré.
     */
    private static final SimpleDoubleProperty RING_REMOVED_WEIGHT = new SimpleDoubleProperty(1_000.0);

    /**
     * Poids d'un pion sur le plateau.
     */
    private static final SimpleDoubleProperty PAWN_WEIGHT = new SimpleDoubleProperty(10.0);

    /**
     * Poids d'une quasi-ligne (4 pions alignés).
     */
    private static final SimpleDoubleProperty NEAR_LINE_WEIGHT = new SimpleDoubleProperty(50.0);

    /**
     * Recherche le meilleur coup possible depuis l'état fourni pour l'équipe donnée.
     *
     * @param state  L'état de jeu actuel.
     * @param depth  La profondeur maximale d'exploration.
     * @param aiTeam L'équipe de l'IA (celle qui maximise).
     * @return Un tableau {@code [from, to]} représentant le meilleur déplacement,
     * ou {@code null} si aucun coup n'est disponible.
     */
    public Move bestMove(IState state, int depth, Team aiTeam) {
        double bestValue = Double.NEGATIVE_INFINITY;
        Move bestMove = null;

        for (Coordinate ring : state.rings().get(aiTeam)) {
            Set<Coordinate> destinations = state.availableMoves(ring);
            for (Coordinate dest : destinations) {
                IState next = applyMove(state, new Move(ring, dest));
                if (next == null) continue;

                double value = minimax(next, depth - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, aiTeam);
                if (value > bestValue) {
                    bestValue = value;
                    bestMove = new Move(ring, dest);
                }
            }
        }

        return bestMove;
    }

    /**
     * Algorithme Minimax récursif avec élagage Alpha-Bêta.
     *
     * @param state      L'état courant.
     * @param depth      Profondeur restante à explorer.
     * @param alpha      Meilleure valeur garantie pour le maximisant.
     * @param beta       Meilleure valeur garantie pour le minimisant.
     * @param maximizing {@code true} si c'est au tour du joueur maximisant.
     * @param aiTeam     L'équipe de l'IA.
     * @return Le score heuristique de l'état.
     */
    public double minimax(IState state, int depth, double alpha, double beta, boolean maximizing, Team aiTeam) {
        if (depth == 0 || isTerminal(state)) {
            return evaluate(state, aiTeam);
        }

        if (!state.getLines().isEmpty()) {
            return handleLineRemoval(state, depth, alpha, beta, maximizing, aiTeam);
        }

        Team currentTeam = state.turn();

        if (maximizing) {
            double value = Double.NEGATIVE_INFINITY;
            for (Coordinate ring : state.rings().get(currentTeam)) {
                for (Coordinate dest : state.availableMoves(ring)) {
                    IState next = applyMove(state, new Move(ring, dest));
                    if (next == null) continue;
                    value = Math.max(value, minimax(next, depth - 1, alpha, beta, false, aiTeam));
                    alpha = Math.max(alpha, value);
                    if (alpha >= beta) return value; // Coupure bêta
                }
            }
            return value;
        } else {
            double value = Double.POSITIVE_INFINITY;
            for (Coordinate ring : state.rings().get(currentTeam)) {
                for (Coordinate dest : state.availableMoves(ring)) {
                    IState next = applyMove(state, new Move(ring, dest));
                    if (next == null) continue;
                    value = Math.min(value, minimax(next, depth - 1, alpha, beta, true, aiTeam));
                    beta = Math.min(beta, value);
                    if (beta <= alpha) return value; // Coupure alpha
                }
            }
            return value;
        }
    }

    /**
     * Gère la phase de suppression de ligne dans l'arbre Minimax.
     * Pour chaque ligne à retirer, on essaie chaque anneau valide et on continue l'exploration.
     */
    private double handleLineRemoval(IState state, int depth, double alpha, double beta,
                                     boolean maximizing, Team aiTeam) {
        List<Set<Coordinate>> lines = state.getLines();
        Set<Coordinate> line = lines.get(0); // On traite la première ligne disponible
        Team currentTeam = state.turn();

        if (maximizing) {
            double value = Double.NEGATIVE_INFINITY;
            for (Coordinate ring : state.rings().get(currentTeam)) {
                try {
                    IState next = state.removeLine(new RemoveLine(line, ring));
                    value = Math.max(value, minimax(next, depth - 1, alpha, beta, false, aiTeam));
                    alpha = Math.max(alpha, value);
                    if (alpha >= beta) return value;
                } catch (Exception ignored) {
                }
            }
            return value;
        } else {
            double value = Double.POSITIVE_INFINITY;
            for (Coordinate ring : state.rings().get(currentTeam)) {
                try {
                    IState next = state.removeLine(new RemoveLine(line, ring));
                    value = Math.min(value, minimax(next, depth - 1, alpha, beta, true, aiTeam));
                    beta = Math.min(beta, value);
                    if (beta <= alpha) return value;
                } catch (Exception ignored) {
                }
            }
            return value;
        }
    }

    /**
     * Évalue un état de jeu du point de vue de l'IA.
     *
     * @param state  L'état à évaluer.
     * @param aiTeam L'équipe de l'IA.
     * @return Un score positif si l'état est favorable à l'IA, négatif sinon.
     */
    public double evaluate(IState state, Team aiTeam) {
        Team opponent = aiTeam.other();

        int aiRings = state.rings().get(aiTeam).size();
        int opponentRings = state.rings().get(opponent).size();

        if (aiRings <= 2) return WIN_SCORE.get();      // L'IA a retiré 3 anneaux 
        if (opponentRings <= 2) return -WIN_SCORE.get();     // L'adversaire a retiré 3 anneaux 

        double score = 0.0;
        score += (INITIAL_RINGS.get() - aiRings) * RING_REMOVED_WEIGHT.get();
        score -= (INITIAL_RINGS.get() - opponentRings) * RING_REMOVED_WEIGHT.get();

        // Pions
        for (var entry : state.board().entrySet()) {
            if (entry.getValue() instanceof Pawn pawn) {
                score += (pawn.getTeam() == aiTeam) ? PAWN_WEIGHT.get() : -PAWN_WEIGHT.get();
            }
        }

        // Lignes en cours
        score += state.getLines().stream()
                .filter(line -> line.stream().allMatch(c ->
                        state.board().get(c) instanceof Pawn p && p.getTeam() == aiTeam))
                .count() * NEAR_LINE_WEIGHT.get();

        score -= state.getLines().stream()
                .filter(line -> line.stream().allMatch(c ->
                        state.board().get(c) instanceof Pawn p && p.getTeam() == opponent))
                .count() * NEAR_LINE_WEIGHT.get();

        // Nb de coups
        long aiMobility = state.rings().get(aiTeam).stream()
                .mapToLong(r -> state.availableMoves(r).size())
                .sum();
        long oppMobility = state.rings().get(opponent).stream()
                .mapToLong(r -> state.availableMoves(r).size())
                .sum();

        score += (aiMobility - oppMobility) * 0.5;

        return score;
    }
    /**
     * Vérifie si l'état est terminal (fin de partie).
     *
     * @param state L'état à vérifier.
     * @return {@code true} si la partie est terminée.
     */
    public boolean isTerminal(IState state) {
        // Un joueur a retiré 3 anneaux
        if (state.rings().get(Team.WHITE).size() <= 2) return true;
        if (state.rings().get(Team.BLACK).size() <= 2) return true;

        // Match nul : aucun anneau du joueur courant ne peut bouger
        Team current = state.turn();
        return state.rings().get(current).stream()
                .allMatch(r -> state.availableMoves(r).isEmpty());
    }

    /**
     * Applique un déplacement d'anneau de {@code from} vers {@code to} et retourne
     * le nouvel état. Renvoie {@code null} en cas d'exception.
     *
     * @param state L'état courant.
     * @param move Déplacement à faire.
     * @return Le nouvel état, ou {@code null} si le coup est invalide.
     */
    private IState applyMove(IState state, Move move) {
        try {
            return state.move(move);
        } catch (Exception e) {
            return null;
        }
    }
}
