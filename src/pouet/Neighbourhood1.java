package pouet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Pair;


public class Neighbourhood1 extends Neighbourhood {
    
    List<Pair<Integer,Integer>> tabuList_ = new ArrayList<Pair<Integer,Integer>>();
    
    int tabuListSize_;
    
    Solution neighbour_ = null;
    
    Pair<Integer,Integer> movement_ = null;
    
    public Neighbourhood1(int tabuListSize) {
	this.tabuListSize_ = tabuListSize;
    }
    
    @Override
    public boolean findBestNeighbour(Solution sol) {
	List<Pair<Integer,Integer>> bestMoves = new ArrayList<Pair<Integer,Integer>>();
	int bestCost = sol.cost();
	
	Solution currentNeighbour = null;
	Pair<Integer,Integer> currentMove = null;
	
	boolean res = false;
	
	for (Integer row = 0; row < sol.size() ; ++row) {
	    for (Integer column = 0 ; column < sol.size(); ++column) {
		
		if((sol.get(row) != column) && (this.tabuList_.contains(row) == false)) {
		    currentMove = new Pair<Integer, Integer>(row, column);
		    currentNeighbour = new Solution(sol, currentMove);
		    
		    if(currentNeighbour.cost() <= bestCost) {
			if(currentNeighbour.cost() < bestCost) {
			    bestMoves.clear();
			}

			bestMoves.add(new Pair<Integer, Integer>(row, column));
			bestCost = currentNeighbour.cost();
			
			res = true;
		    }
		}
	    }
	}
	
	if (bestMoves.isEmpty() == false) {
	    Random rand = new Random();
	    Integer randIndex = rand.nextInt(bestMoves.size());
	    Pair<Integer, Integer> move = bestMoves.get(randIndex);

	    this.neighbour_ = new Solution(sol, move);
	    this.movement_ = move;
	}
	
	return res;
    }
    
    @Override
    public void addToTabuList() throws Exception {
	
	if(this.movement_ != null) {
	    if ((this.tabuList_.size() == this.tabuListSize_)
		    && (this.tabuListSize_ > 0)) {
		
//		System.out.println("DBG tabu list is full !");
		this.tabuList_.remove(0);
	    }

	    if (this.tabuListSize_ > 0) {
		
//		System.out.println("DBG movement added to tabu list.");
		
		this.tabuList_.add(this.movement_);
		this.movement_ = null;
		this.neighbour_ = null;
	    }
	}
	
	else {
	    throw new Exception("No neighbour to add to the tabu list.");
	}
    }
    
    public Solution getNeighbour() {
	return this.neighbour_;
    }
    
    public void clearTabuList() {
	this.tabuList_.clear();
    }
    
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
