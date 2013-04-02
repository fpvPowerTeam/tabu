package pouet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Générateur de solution totalement aléatoire.
 */
public class Generator1 implements SolutionGenerator {
    
    private Preprocessor preprocessor_ = null;
    
    public Generator1() {}
    
    public Generator1(Preprocessor preproc) {
	this.preprocessor_ = preproc;
    }
    
    @Override
    public Solution generate(Integer size, boolean verbose) {
	List<Integer> values = new ArrayList<Integer>();
	Random rand = new Random();
	
	if(verbose == true) {
	    System.out.print("Generation of an initial solution ... ");
	}
	
	for(int i = 0 ; i < size ; ++i) {
	    values.add(rand.nextInt(size));
	}
	
	if(verbose == true) {
	    System.out.println("done");
	}
	
	return (this.preprocessor_ != null) ?
		this.preprocessor_.preprocess(new Solution(values), verbose)
		: new Solution(values);
    }
}
