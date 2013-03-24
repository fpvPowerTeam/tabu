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

    private Boolean verbose_ = true;

    public ChessQueensTS(final Integer nQueens, final Integer memorySize) {
	this.nQueens_ = nQueens;
	this.memorySize_ = memorySize;
    }

    public ChessQueensTS(final Integer nQueens, final Integer memorySize,
	    final Boolean verbose) {
	this.nQueens_ = nQueens;
	this.memorySize_ = memorySize;
	this.verbose_ = verbose;
    }

    public Solution search(final Neighbourhood neighbourhood,
	    final SolutionGenerator generator, final Integer nRuns) {

	Solution candidateSol = null;
	Solution bestSol = null;
	Solution currentSol = null;
	Solution neighbourSol = null;

	Boolean stop = false;

	// Number of consecutive movements without any improvement
	Integer noImprovement = 0;
	
	long startTime = System.currentTimeMillis();
	
	for (int run = 0; run < nRuns && stop == false; run++) {
	    
	    this.tabuList_.clear();
	    noImprovement = 0;

	    currentSol = generator.generate(this.nQueens_);
	    bestSol = (Solution) currentSol.clone();

	    Boolean goOn = true;

	    if (this.verbose_ == true) {
		System.out.print("\nInitial solution : ");
		currentSol.print();
		
		System.out.println("Initial cost : "
			+ currentSol.cost() + "\n");
	    }

	    while (goOn && bestSol.cost() > 0) {
		goOn = false;

		Neighbour neighbour = neighbourhood.findBestNeighbour(
		        currentSol, this.tabuList_);

		neighbourSol = neighbour.getSolution();

		if (neighbourSol.cost() <= bestSol.cost()) {

		    if (neighbourSol.cost() < bestSol.cost()) {
			noImprovement = 0;

			if (this.verbose_ == true) {
			    System.out.println("New lower cost : "
				    + neighbourSol.cost());
			}
		    }

		    else {
			++noImprovement;

			System.out.println("DBG " + noImprovement
			        + " not improving mvt(s)");
		    }

		    currentSol = neighbourSol;
		    bestSol = (Solution) currentSol.clone();

		    if ((this.tabuList_.size() == this.memorySize_)
			    && (memorySize_ > 0)) {

			this.tabuList_.remove(0);
		    }

		    if (this.memorySize_ > 0) {
			this.tabuList_.add(neighbour.getMovement());
		    }

		    goOn = true;
		}

		if (noImprovement > this.memorySize_) {
		    currentSol.degrade();

		    if (this.verbose_ == true) {
			System.out.println("Local minimum detected."
			        + "Solution degraded.");
		    }
		}
	    }
	}

	long endTime = System.currentTimeMillis();

	if ((bestSol.cost() == 0) && (this.verbose_ == true)) {
	    System.out.println("DBG " + (endTime - startTime) + " ms");
	    
	    System.out.print("\nCandidate solution found : ");
	    bestSol.print();
	}

	return candidateSol;
    }
}
