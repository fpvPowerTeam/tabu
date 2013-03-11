import org.kohsuke.args4j.Option;

/**
 * Parseur de la ligne de commande.
 */
public class Arguments {
    
    /**
     * Nombre de reines.
     * 
     * <p>Doit etre superieur a 3 pour que le probleme ait une solution.</p>
     */
    @Option(name="-n", aliases = {"--nQueens"}, usage="Nombre de reines")
    private int nQueens_;
    
    /**
     * Taille de la liste tabu.
     * 
     * <p>Doit etre positive non nulle.</p>
     */
    @Option(name="-t", aliases = {"--tabuSize"}, usage="Taille de la liste taboue")
    private int tabuListSize_;
    
    /**
     * Nombre de runs.
     * 
     * <p>Doit etre positif non nul.</p>
     */
    @Option(name="--nRuns", usage="Nombre de runs")
    private int nRuns_;
}
