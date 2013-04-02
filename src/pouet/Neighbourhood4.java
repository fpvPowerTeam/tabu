package pouet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import util.Pair;

/**
 * 
 * Exploration partielle du voisinnage.
 * Un mouvement de voisinnage est le swap entre 2 reines (échange des colonnes).
 */
public class Neighbourhood4 extends Neighbourhood {
    
    Set<Pair<Integer,Integer>> tabuList_ = new HashSet<Pair<Integer,Integer>>();
    
    int tabuListSize_;
    
    Solution neighbour_ = null;
    
    Pair<Integer,Integer> movement_ = null;
    
    public Neighbourhood4(int tabuListSize) {
	this.tabuListSize_ = tabuListSize;
    }
    
    @Override
    public boolean findBestNeighbour(Solution sol) {
	List<Pair<Integer, Integer>> bestMoves = new ArrayList<Pair<Integer, Integer>>();
	int bestCost = sol.cost();
	
	Pair<Integer, Integer> currentMove = null;
	
	boolean stop = false;
	
	for(int row1 = 0 ; (row1 < sol.size()) && (stop == false) ; ++row1) {
	    Set<Integer> meuh = pouet(sol, row1);

	    if(meuh.isEmpty() == false) {

		for(int row2 = 0 ; row2 < sol.size() ; ++row2) {
		    if(sol.get(row1) != row2) {
			currentMove = new Pair<Integer, Integer>(row1, row2);

			if(this.tabuList_.contains(currentMove) == false) {
			    sol.swap(row1, row2);
			    
			    if(sol.cost() <= bestCost) {
				if(sol.cost() < bestCost) {
				    bestMoves.clear();
				}
	
				bestMoves.add(new Pair<Integer, Integer>(currentMove));
				bestCost = sol.cost();
				
				stop = true;
			    }
			    
			    sol.swap(row1, row2);
			}
		    }
		}
	    }
	}
	
	if(bestMoves.isEmpty() == false) {
	    Random rand = new Random();
	    Integer randIndex = rand.nextInt(bestMoves.size());
	    Pair<Integer, Integer> move = bestMoves.get(randIndex);
	    
	    this.neighbour_ = (Solution) sol.clone();
	    this.neighbour_.swap(move.getFirst(), move.getSecond());
	    
	    this.movement_ = move;
	}
	
	return (bestMoves.isEmpty() == false);
    }
    
    public void addToTabuList() throws Exception {
	
	if(this.movement_ != null) {
	    if ((this.tabuList_.size() == this.tabuListSize_)
		    && (this.tabuListSize_ > 0)) {
		
		this.tabuList_.remove(0);
	    }

	    if (this.tabuListSize_ > 0) {
		this.tabuList_.add(this.movement_);
		this.movement_ = null;
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
	
	for(Pair<Integer,Integer> p : this.tabuList_) {
	    output += p + " ";
	}
	
	output += "}";
	
	return output;
    }
}
