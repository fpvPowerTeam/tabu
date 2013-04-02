package pouet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Pair;


/**
 * A solution (not necessarily feasible) to a n-queens problem.
 */
public class Solution2 implements Cloneable {
    
    /**
     * The list of the values of the variables.
     */
    private List<Integer> variables_ = new ArrayList<Integer>();
    
    /**
     * The solution cost.
     */
    private Integer cost_ = null;
    
    /**
     * Constructs a new solution with all variables fixed to zero.
     * 
     * @param size
     *            The size of the solution
     */
    public Solution2() {}
    
    /**
     * Constructs a new solution.
     * 
     * @param values
     *            List of the variables values
     */
    public Solution2(List<Integer> values) {
	this.variables_.addAll(values);
    }
    
    /**
     * Constructs a new solution.
     * 
     * @param values
     *            Array of the variables values
     */
    public Solution2(int[] values) {
	for(int i = 0 ; i < values.length ; ++i) {
	    variables_.add(values[i]);
	}
    }
    
    public Solution2(final Solution2 sol, final Pair<Integer, Integer> movement) {
	this.variables_.addAll(sol.variables_);
	this.variables_.set(movement.getFirst(),movement.getSecond());
    }
    
    public int get(final int index) {
	return this.variables_.get(index);
    }
    
    public int size() {
	return this.variables_.size();
    }
    
    public int cost() {
	return (this.cost_ != null) ? this.cost_ : this.fitness();
    }
    
    public void add(int value) {
	this.variables_.add(value);
    }
    
    public void remove(int index) {
	this.variables_.remove(index);
    }
    
    public void set(final int index, final int value) {
	this.variables_.set(index, value);
	this.cost_ = null;
    }
    
    public void degrade() {
	Random rand = new Random();
	
	int index1 = rand.nextInt(this.size());
	int index2 = rand.nextInt(this.size());
	
	this.swap(index1, index2);
	
	this.cost_ = null;
    }
    
    public void swap(int index1, int index2) {
	int aux = this.variables_.get(index1);
	this.variables_.set(index1, this.variables_.get(index2));
	this.variables_.set(index2, aux);
    }
    
    private int fitness() {
	int result = 0;

	// allDifferent on Q
	result += this.fitnessAllDiff(this);

	// allDifferent on y
	
	int[] aux = new int[this.size()];
	
	for (int i = 0 ; i < this.size() ; ++i) {
	    aux[i] = this.variables_.get(i) + i;
	}
	
	result += this.fitnessAllDiff(new Solution2(aux));

	// allDifferent on z
	
	for (int i = 0 ; i < this.size() ; ++i) {
	    aux[i] = this.variables_.get(i) - i;
	}
	
	result += this.fitnessAllDiff(new Solution2(aux));

	return result;
    }
    
    private int fitnessAllDiff(Solution2 sol) {
	int result = 0;
	
	for (int i = 0 ; i < sol.size() ; ++i) {
	    for (int j = i+1 ; j < sol.size() ; ++j) {
		if (sol.get(i) == sol.get(j)) {
		    ++result;
		}
	    }
	}
	
	return result;
    }
    
    @Override
    public Object clone() {
	return new Solution(this.variables_);
    }
    
    public void print() {
	System.out.print("{");
	
	if(this.size() > 0) {
	    System.out.print(this.get(0));
	    
	    for(int i = 1 ; i < this.size() ; ++i) {
		System.out.print(", " + this.get(i));
	    }
	}
	
	System.out.println("}");
    }

    public boolean equals(Solution other) {
	boolean eq = true;
	
	if(this.size() == other.size()) {
	    for(int i = 0 ; i < this.size() ; ++i) {
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
