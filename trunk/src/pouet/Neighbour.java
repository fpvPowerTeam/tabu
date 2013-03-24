package pouet;

import util.Pair;


/**
 * A neighbour of a n-queens problem solution.
 */
public class Neighbour {

    private Solution neighbourSol_ = null;
    
    /**
     * The movement to perform to reach the neighbour.
     */
    private Pair<Integer, Integer> movement_ = null;

    /**
     * Constructs a new neighbour.
     * 
     * @param movement
     *            The movement to perform to reach the neighbour
     * @param cost
     *            The cost of the neighbour
     */
    public Neighbour(final Solution sol, final Pair<Integer, Integer> movement) {
	this.neighbourSol_ = sol;
	this.movement_ = movement;
    }
    
    public Solution getSolution() {
	return this.neighbourSol_;
    }
    
    public Pair<Integer, Integer> getMovement() {
	return this.movement_;
    }
}
