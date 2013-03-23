package pouet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Pair;


public class Neighbourhood1 implements Neighbourhood {

    @Override
    public Pair<Integer, Integer> findBestNeighbour(Solution sol,
            List<Pair<Integer, Integer>> tabuList) {
	
	int bestCost = sol.cost();
	
	Pair<Integer, Integer> currentMove = null;
	List<Pair<Integer, Integer>> bestMoves =
		new ArrayList<Pair<Integer, Integer>>();
	
	Solution currentNeighbour = null;
	
	for (Integer row = 0; row < sol.size() ; ++row) {
	    for (Integer column = 0 ; column < sol.size(); ++column) {
		currentMove = new Pair<Integer, Integer>(row, column);
		
		if((sol.get(row) != column) && (tabuList.contains(currentMove) == false)) {
		    currentNeighbour = new Solution(sol, currentMove);

		    if(currentNeighbour.cost() <= bestCost) {
			if(currentNeighbour.cost() < bestCost) {
			    bestMoves.clear();
			}

			bestMoves.add(new Pair<Integer, Integer>(currentMove));
			bestCost = currentNeighbour.cost();
		    }
		}
	    }
	}
	
	Random rand = new Random();
	Integer randIndex = rand.nextInt(bestMoves.size());
	
	return bestMoves.get(randIndex);
    }

}
