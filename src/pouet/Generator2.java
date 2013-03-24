package pouet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Generator2 implements SolutionGenerator {

    @Override
    public Solution generate(Integer size) {
	List<Integer> values = new ArrayList<Integer>();
	
	for(Integer i = 0 ; i < size ; ++i) {
	    values.add(i);
	}
	
	Random rand = new Random();
	Integer aux = 0, index1 = 0, index2 = 0;
	
	for(Integer i = 0 ; i < 5*size ; ++i) {
	    index1 = rand.nextInt(size);
	    index2 = rand.nextInt(size);
	    
	    aux = values.get(index1);
	    values.set(index1, values.get(index2));
	    values.set(index2, aux);
	}
	
	return new Solution(values);
    }

}
