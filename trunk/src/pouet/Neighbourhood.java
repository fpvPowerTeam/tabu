package pouet;


public abstract class Neighbourhood {
    
    /**
     * Searches an improving neighbour solution.
     * 
     * @param sol
     *            A n-queens problem solution
     * 
     * @return Whether it finds a neighbour with a cost greater or equal to the
     *         solution one
     */
    public abstract boolean findBestNeighbour(Solution sol);
    
    public abstract void addToTabuList() throws Exception;
    
    public abstract Solution getNeighbour();
    
    public abstract void clearTabuList();
    
    public abstract String TabuListToString();
}
