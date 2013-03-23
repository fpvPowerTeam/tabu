package pouet;

import util.Pair;


/**
 * A neighbour of a n-queens problem solution.
 */
public class Neighbour {

    /**
     * The movement to perform to reach the neighbour.
     */
    private Pair<Integer, Integer> movement_ = null;

    /**
     * The cost of the neighbour.
     */
    private Integer cost_ = null;

    /**
     * Constructs a new neighbour.
     * 
     * @param movement
     *            The movement to perform to reach the neighbour
     * @param cost
     *            The cost of the neighbour
     */
    public Neighbour(final Pair<Integer, Integer> movement, final Integer cost) {
	this.movement_ = movement;
	this.cost_ = cost;
    }
    
    public Pair<Integer, Integer> getMovement() {
	return this.movement_;
    }
    
    public Integer getCost() {
	return this.cost_;
    }
}
