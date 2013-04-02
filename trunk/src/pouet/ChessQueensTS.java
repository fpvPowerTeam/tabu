package pouet;

import java.io.IOException;


public class ChessQueensTS {

    /**
     * The number of queens.
     */
    private int nQueens_;

    /**
     * The size of the tabu list (memory).
     */
    private int memorySize_;

    public ChessQueensTS(final int nQueens, final int memorySize) {
	this.nQueens_ = nQueens;
	this.memorySize_ = memorySize;
    }

    public Solution search(final Neighbourhood neighbourhood,
	    final SolutionGenerator generator, final Integer nRuns,
	    final Data data, final boolean verbose)
	    throws IOException {

	Solution candidateSol = null;
	Solution bestSol = null;
	Solution currentSol = null;
	Solution neighbourSol = null;

	Boolean stop = false;
	long cntIterations = 0;
	long cntRuns = 0;
	long startTime = 0;
	long endTime = 0;

	// Number of consecutive movements without any improvement
	Integer noImprovement = 0;

	if(verbose == true) {
	    System.out.println("\nNumber of queens : " + this.nQueens_);
	    System.out.println("Tabu list size : " + this.memorySize_);
	    System.out.println("Maximum number of runs : " + nRuns);
	}

	startTime = System.currentTimeMillis();

	for (cntRuns = 0; cntRuns < nRuns && stop == false; cntRuns++) {
	    if(verbose == true) {
		System.out.println("\nRun " + (cntRuns+1) + " :\n");
	    }

	    neighbourhood.clearTabuList();
	    noImprovement = 0;
	    cntIterations = 0;

	    currentSol = generator.generate(this.nQueens_, verbose);
	    bestSol = (Solution) currentSol.clone();

	    Boolean goOn = true;

	    if (verbose == true) {
		System.out.println("\nInitial cost : " + currentSol.cost());
	    }

	    while (goOn && bestSol.cost() > 0) {
		goOn = false;
		++cntIterations;

		boolean pouet = neighbourhood.findBestNeighbour(currentSol);

		if(pouet == true) {
		    neighbourSol = neighbourhood.getNeighbour();

		    if (neighbourSol.cost() <= bestSol.cost()) {

			if (neighbourSol.cost() < bestSol.cost()) {
			    noImprovement = 0;

			    if (verbose == true) {
				System.out.println("New lower cost : "
					+ neighbourSol.cost());
			    }
			}

			else {
			    ++noImprovement;
			}

			currentSol = neighbourSol;
			bestSol = (Solution) currentSol.clone();

			try {
			    neighbourhood.addToTabuList();
			}

			catch(Exception e) {
			    e.printStackTrace(System.err);
			}

			goOn = true;
		    }
		}

		if (noImprovement > this.memorySize_) {
		    currentSol.degrade();
		    noImprovement = 0;

		    if (verbose == true) {
			System.out.println("Local minimum detected."
				+ "Solution degraded.");
		    }
		}
	    }
	    
	    if(bestSol.cost() == 0) {
		stop = true;
	    }
	    else {
		if(verbose == true) {
		    System.out.println("No solution found.");
		}
	    }
	}
	
	endTime = System.currentTimeMillis();

	if (bestSol.cost() == 0) {
	    
	    if(verbose == true) {
		System.out.println("\nCandidate solution found in " + cntRuns
			+ " run(s) after " + (endTime - startTime) + " ms :");

		    bestSol.print();
	    }

	    if (data != null) {
		data.add((endTime - startTime), cntRuns, cntIterations);
	    }
	}

	return candidateSol;
    }
}
