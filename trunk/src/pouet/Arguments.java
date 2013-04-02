package pouet;

import org.kohsuke.args4j.Option;


/**
 * Arguments de la ligne de commande.
 */
public class Arguments {
    
    /**
     * Number of queens.
     */
    @Option(name = "-n", aliases = {"--nQueens"}, usage = "Number of queens (required)", required = true)
    private int nQueens_;
    
    /**
     * Tabu list size.
     */
    @Option(name = "-t", aliases = {"--tabuSize"}, usage = "Tabu list size (required)", required = true)
    private int tabuListSize_;
    
    /**
     * Maximum number of runs.
     */
    @Option(name = "--nRuns", usage = "Maximum number of runs")
    private int nRuns_ = 1;
    
    /**
     * Number of executions.
     */
    @Option(name = "--nExec", usage = "Number of executions")
    private int nExec_ = 1;
    
    /**
     * Draw plots.
     */
    @Option(name = "--plots", usage = "Draw plots")
    private boolean plots_;
    
    /**
     * Verbose mode.
     */
    @Option(name = "-v", aliases = "{--verbose}", usage = "Verbose mode")
    private boolean verbose_;
    
    public int getNumberOfQueens() {
	return this.nQueens_;
    }
    
    public int getTabuListSize() {
	return this.tabuListSize_;
    }
    
    public int getNumberOfRuns() {
	return this.nRuns_;
    }
    
    public int getNumberOfExecutions() {
	return this.nExec_;
    }
    
    public boolean getPlots() {
	return this.plots_;
    }
    
    public boolean getVerbose() {
	return verbose_;
    }
}
