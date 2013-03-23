package pouet;

import java.util.ArrayList;
import java.util.List;

import util.Pair;


public class ChessQueensTS {

    /**
     * The number of queens.
     */
    private Integer nQueens_;

    /**
     * The size of the tabu list (memory).
     */
    private Integer memorySize_;

    /**
     * The tabu list.
     */
    private List<Pair<Integer, Integer>> tabuList_ =
	    new ArrayList<Pair<Integer, Integer>>();

    public ChessQueensTS(final Integer nQueens, final Integer memorySize) {
	this.nQueens_ = nQueens;
	this.memorySize_ = memorySize;
    }

    public Solution search(final Neighbourhood neighbourhood,
	    final SolutionGenerator generator, final Integer nRuns) {

	Solution candidateSol = null;
	Solution bestSol = null;
	Solution neighbourSol = null;

	Boolean stop = false;

	for (int run = 0; run < nRuns && stop == false; run++) {
	    long startTime = System.currentTimeMillis();
	    this.tabuList_.clear();

	    bestSol = generator.generate(this.nQueens_);
	    Boolean goOn = true;

	    System.out.print("Initial solution : ");
	    bestSol.print();

	    while(goOn && bestSol.cost() > 0) {
		goOn = false;

		Pair<Integer,Integer> mvt = neighbourhood.findBestNeighbour(
			bestSol, this.tabuList_);

		neighbourSol = new Solution(bestSol, mvt);

		if(neighbourSol.cost() <= bestSol.cost()) {
		    if(neighbourSol.cost() < bestSol.cost()) {
			System.out.println("New lower cost : "
				+ neighbourSol.cost());
		    }
		    
		    bestSol = neighbourSol.clone();
		    
		    if ((this.tabuList_.size() == this.memorySize_)
			    && (memorySize_ > 0)) {

			this.tabuList_.remove(0);
		    }

		    if(this.memorySize_ > 0) {
			this.tabuList_.add(mvt);
		    }

		    goOn = true;
		}
	    }
	}

	long endTime = System.currentTimeMillis();
	
	if(bestSol.cost() == 0) {
	    System.out.print("Candidate solution found : ");
	    bestSol.print();
	}
	
	return candidateSol;
    }
}
