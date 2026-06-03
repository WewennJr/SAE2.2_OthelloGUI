package but.info.sae2_12.AI;

import but.info.sae2_12.model.actions.Action;
import but.info.sae2_12.model.state.IState;
import coordinates.Coordinate;

/**
 * Interface commune à toutes les IA du jeu YINSH.
 *
 * <p>Une IA reçoit un état de jeu et retourne le meilleur coup qu'elle a calculé.
 *
 * <p>Le coup est représenté par un tableau de deux coordonnées :
 * <ul>
 *   <li>{@code result[0]} : la coordonnée de l'anneau à déplacer (ou à retirer si suppression de ligne).</li>
 *   <li>{@code result[1]} : la coordonnée de destination du déplacement, ou {@code null} si c'est une suppression de ligne.</li>
 * </ul>
 */
public interface AI {

    /**
     * Choisit le meilleur coup depuis l'état de jeu fourni.
     *
     * @param state L'état de jeu courant.
     * @return Un tableau {@code [from, to]} :
     *         <ul>
     *           <li>Pour un déplacement d'anneau : {@code [coordDépart, coordArrivée]}</li>
     *           <li>Pour une suppression de ligne : {@code [coordAnneau, null]}</li>
     *         </ul>
     *         Retourne {@code null} si aucun coup n'est possible.
     */
    Action chooseMove(IState state);

    /**
     * @return Le nom de l'IA (pour l'affichage).
     */
    String getName();
}
