package pouet;

import java.util.ArrayList;
import java.util.List;

import util.Pair;


/**
 * A solution (not necessarily feasible) to a n-queens problem.
 */
public class Solution implements Cloneable {
    
    /**
     * The list of the values of the variables.
     */
    private List<Integer> variables_ = new ArrayList<Integer>();
    
    /**
     * The solution cost.
     */
    private Integer cost_;
    
    /**
     * Constructs a new solution.
     * 
     * @param values
     *            List of the variables values
     */
    public Solution(List<Integer> values) {
	for(Integer v : values) {
	    this.variables_.add(v);
	}
    }
    
    /**
     * Constructs a new solution.
     * 
     * @param values
     *            Array of the variables values
     */
    public Solution(int[] values) {
	for(int i = 0 ; i < values.length ; ++i) {
	    this.variables_.add(values[i]);
	}
    }
    
    public Solution(final Solution sol, final Pair<Integer, Integer> movement) {
	for(Integer i = 0 ; i < sol.size() ; ++i) {
	    if(i == movement.getFirst()) {
		this.variables_.add(movement.getSecond());
	    }
	    
	    else {
		this.variables_.add(sol.get(i));
	    }
	}
    }
    
    public Integer get(final Integer index) {
	return this.variables_.get(index);
    }
    
    public Integer size() {
	return this.variables_.size();
    }
    
    public Integer cost() {
	return (this.cost_ != null) ? this.cost_ : this.fitness();
    }
    
    public void set(final Integer index, final Integer value) {
	this.variables_.set(index, value);
    }
    
    private Integer fitness() {
	Integer result = 0;

	// allDifferent on Q
	result += this.fitnessAllDiff(this);

	// allDifferent on y
	
	int[] aux = new int[this.size()];
	
	for (int i = 0 ; i < this.size() ; ++i) {
	    aux[i] = this.get(i) + i;
	}
	
	result += this.fitnessAllDiff(new Solution(aux));

	// allDifferent on z
	
	for (int i = 0 ; i < this.size() ; ++i) {
	    aux[i] = this.get(i) - i;
	}
	
	result += this.fitnessAllDiff(new Solution(aux));

	return result;
    }
    
    private Integer fitnessAllDiff(Solution sol) {
	Integer result = 0;
	
	for (Integer i = 0 ; i < sol.size() ; ++i) {
	    for (Integer j = i+1 ; j < sol.size() ; ++j) {
		if (sol.get(i) == sol.get(j)) {
		    ++result;
		}
	    }
	}
	
	return result;
    }
    
    @Override
    public Solution clone() {
	Solution clone = null;
	
	try {
	    clone = (Solution) super.clone();
	}
	
	catch(CloneNotSupportedException e) {
	    e.printStackTrace(System.err);
	}
	
	return clone;
    }
    
    public void print() {
	System.out.print("{");
	
	if(this.size() > 0) {
	    System.out.print(this.get(0));
	    
	    for(Integer i = 1 ; i < this.size() ; ++i) {
		System.out.print(", " + this.get(i));
	    }
	}
	
	System.out.println("}");
    }

    public boolean equals(Solution other) {
	boolean eq = true;
	
	if(this.size() == other.size()) {
	    for(Integer i = 0 ; i < this.size() ; ++i) {
		if(this.get(i) != other.get(i)) {
		    eq = false;
		}
	    }
	}
	
	else {
	    eq = false;
	}
	
	return eq;
    }
}
