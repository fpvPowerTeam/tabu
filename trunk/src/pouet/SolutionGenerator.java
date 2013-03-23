package pouet;


public interface SolutionGenerator {
    
    /**
     * Generates an initial solution of a n-queens problem.
     * 
     * @param size
     *            The number of queens
     * 
     * @return A solution
     */
    public Solution generate(Integer size);
}
