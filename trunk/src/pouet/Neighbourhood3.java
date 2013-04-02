package pouet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import util.Pair;

/**
 * Exploration partielle du voisinnage.
 * Un mouvement de voisinnage est le déplacement d'une reine dans sa ligne.
 */
public class Neighbourhood3 extends Neighbourhood {
    
    Set<Integer> tabuList_ = new HashSet<Integer>();
    
    int tabuListSize_;
    
    Solution neighbour_ = null;
    
    int movedQueen_ = -1;
    
    public Neighbourhood3(int tabuListSize) {
	this.tabuListSize_ = tabuListSize;
    }
    
    @Override
    public boolean findBestNeighbour(Solution sol) {
	List<Pair<Integer, Integer>> bestMoves = new ArrayList<Pair<Integer, Integer>>();
	int bestCost = sol.cost();
	
	Solution currentNeighbour = null;
	Pair<Integer, Integer> currentMove = null;
	
	boolean stop = false;
	
	for(int row = 0 ; (row < sol.size()) && (stop == false) ; ++row) {
	    if(this.tabuList_.contains(row) == false) {
		Set<Integer> meuh = pouet(sol, row);
		
		if(meuh.isEmpty() == false) {
		    for(int column = 0 ; column < sol.size() ; ++column) {
			if(sol.get(row) != column) {
			    currentMove = new Pair<Integer, Integer>(row, column);
			    currentNeighbour = new Solution(sol, currentMove);

			    if(currentNeighbour.cost() <= bestCost) {
				if(currentNeighbour.cost() < bestCost) {
				    bestMoves.clear();
				    stop = true;
				}

				bestMoves.add(new Pair<Integer, Integer>(currentMove));
				bestCost = currentNeighbour.cost();
			    }
			}
		    }
		}
	    }
	}
	
	if(bestMoves.isEmpty() == false) {
	    Random rand = new Random();
	    Integer randIndex = rand.nextInt(bestMoves.size());
	    Pair<Integer, Integer> move = bestMoves.get(randIndex);
	    
	    this.neighbour_ = new Solution(sol, move);
	    this.movedQueen_ = move.getFirst();
	}
	
	return (bestMoves.isEmpty() == false);
    }
    
    public void addToTabuList() throws Exception {
	
	if(this.movedQueen_ > -1) {
	    if ((this.tabuList_.size() == this.tabuListSize_)
		    && (this.tabuListSize_ > 0)) {
		this.tabuList_.remove(0);
	    }

	    if (this.tabuListSize_ > 0) {
		this.tabuList_.add(this.movedQueen_);
		this.movedQueen_ = -1;
		this.neighbour_ = null;
	    }
	}
	
	else {
	    throw new Exception("No neighbour to add to the tabu list.");
	}
    }
    
    /**
     * Tests whether a queen is conflictual.
     *  
     * @param sol A solution
     * @param row The row index of the queen
     * 
     * @return
     */
    private Set<Integer> pouet(Solution sol, int row) {
	Set<Integer> attackingQueens = new HashSet<Integer>();
	
	for (int j = 0 ; j < sol.size() ; ++j) {
	    if ( (j != row) && (sol.get(row) == sol.get(j)) ) {
		attackingQueens.add(j);
	    }
	}
	
	int[] aux = new int[sol.size()];

	for (int i = 0 ; i < sol.size() ; ++i) {
	    aux[i] = sol.get(i) + (row - i);
	}

	Solution s = new Solution(aux);

	for (int j = 0 ; j < s.size() ; ++j) {
	    if ( (j != row) && (s.get(row) == s.get(j)) ) {
		attackingQueens.add(j);
	    }
	}
	
	aux = new int[sol.size()];

	for (int i = 0 ; i < sol.size() ; ++i) {
	    aux[i] = sol.get(i) - (row - i);
	}

	s = new Solution(aux);

	for (int j = 0 ; j < s.size() ; ++j) {
	    if ( (j != row) && (s.get(row) == s.get(j)) ) {
		attackingQueens.add(j);
	    }
	}
	
	return attackingQueens;
    }
    
    public Solution getNeighbour() {
	return this.neighbour_;
    }
    
    public void clearTabuList() {
	this.tabuList_.clear();
    }

    @Override
    public String TabuListToString() {
	String output = new String();
	
	output += "{";
	
	for(int i : this.tabuList_) {
	    output += i + " ";
	}
	
	output += "}";
	
	return output;
    }
}
