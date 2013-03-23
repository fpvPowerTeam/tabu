package pouet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Generator1 implements SolutionGenerator {

    @Override
    public Solution generate(Integer size) {
	List<Integer> values = new ArrayList<Integer>();
	Random rand = new Random();
	
	for(Integer i = 0 ; i < size ; ++i) {
	    values.add(rand.nextInt(size));
	}
	
	return new Solution(values);
    }
    
}
