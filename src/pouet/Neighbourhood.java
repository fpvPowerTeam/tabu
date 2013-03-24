package pouet;

import java.util.List;

import util.Pair;


public interface Neighbourhood {
    
    /**
     * Returns the best neighbour of a n-queens problem solution.
     * 
     * @param sol
     * 		A n-queens problem solution
     * 
     * @param tabuList
     *            A list of forbidden movements
     * 
     * @return The best solution neighbour in accordance with the tabu
     *         list
     */
    public Neighbour findBestNeighbour(
	    Solution sol, List<Pair<Integer, Integer>> tabuList);
}
