package pouet;

import java.util.ArrayList;
import java.util.List;

import util.Pair;

public class Preprocessor1 implements Preprocessor {
    
    @Override
    public Solution preprocess(Solution sol, boolean verbose){
	List<Integer> pouet = new ArrayList<Integer>(sol.size());
	List<Pair<Integer,Integer>> buffer = new ArrayList<Pair<Integer,Integer>>();
	List<Integer> costs = new ArrayList<Integer>();
	
	if(verbose == true) {
	    System.out.print("Preprocessing ... ");
	}
	
	pouet.add(sol.get(0));
	
	for(int i = 1 ; i < sol.size() ; ++i) {
	    pouet.add(sol.get(i));
	    
	    Solution s = new Solution(pouet);
	    
	    if(s.cost() != 0) {
		pouet.remove(pouet.size()-1);
		buffer.add(new Pair<>(i, sol.get(i)));
	    }
	}
	
	boolean addition;
	
	while(buffer.isEmpty() == false) {
	    addition = false;
	    costs.clear();
	    
	    int i = 0;
	    
	    while(i < buffer.size()) {
		pouet.add(buffer.get(i).getSecond());
		
		Solution s = new Solution(pouet);
		
		if(s.cost() == 0) {
		    buffer.remove(i);
		    addition = true;
		}
		
		else {
		    costs.add(s.cost());
		    pouet.remove(pouet.size()-1);
		    ++i;
		}
	    }
	    
	    if(addition == false) {
		int minCost = Integer.MAX_VALUE;
		int idxMinCost = -1;
		
		for(i = 0 ; i < costs.size() ; ++i) {
		    if(costs.get(i) < minCost) {
			minCost = costs.get(i);
			idxMinCost = i;
		    }
		}
		
		pouet.add(buffer.get(idxMinCost).getSecond());
		buffer.remove(idxMinCost);
	    }
	}
	
	if(verbose == true) {
	    System.out.println("done");
	}
	
	return new Solution(pouet);
    }
}
