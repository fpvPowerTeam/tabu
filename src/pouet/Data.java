package pouet;

import java.util.ArrayList;

import util.Pair;


/**
 * Données des exécutions de la recherche tabu.
 */
public class Data {

    /**
     * Les temps d'exécution en millisecondes.
     */
    private ArrayList<Pair<Long, Long>> times_ =
	    new ArrayList<Pair<Long, Long>>();
    
    /**
     * Les nombres de runs effectués.
     */
    private ArrayList<Pair<Long, Long>> runs_ =
	    new ArrayList<Pair<Long, Long>>();

    /**
     * Les nombres d'itérations (de mouvements) effectuées.
     */
    private ArrayList<Pair<Long, Long>> iterations_ =
	    new ArrayList<Pair<Long, Long>>();

    /**
     * Construit un nouveau conteneur de données.
     */
    public Data() {
	this.runs_.add(new Pair<Long, Long>(0L,0L));
	this.iterations_.add(new Pair<Long, Long>(0L,0L));
	this.times_.add(new Pair<Long, Long>(0L,0L));
    }

    /**
     * Retourne les nombres de runs cumulés.
     */
    public long[][] getRuns() {
	long[][] runs = new long[this.runs_.size()][2];
	int i = 0;

	for(Pair<Long, Long> p : this.runs_) {
	    runs[i][0] = p.getFirst();

	    if(i == 0) {
		runs[i][1] = p.getSecond();
	    }
	    else {
		runs[i][1] = p.getSecond() + runs[i-1][1];
	    }

	    ++i;
	}

	return runs;
    }

    /**
     * Retourne les nombres d'itérations cumulés.
     */
    public long[][] getIterations() {
	long[][] iter = new long[this.iterations_.size()][2];
	int i = 0;

	for(Pair<Long, Long> p : this.iterations_) {
	    iter[i][0] = p.getFirst();

	    if(i == 0) {
		iter[i][1] = p.getSecond();
	    }
	    else {
		iter[i][1] = p.getSecond() + iter[i-1][1];
	    }

	    ++i;
	}

	return iter;
    }

    /**
     * Retourne les temps d'exécution cumulés.
     */
    public long[][] getTimes() {
	long[][] times = new long[this.times_.size()][2];
	int i = 0;

	for(Pair<Long, Long> p : this.times_) {
	    times[i][0] = p.getFirst();

	    if(i == 0) {
		times[i][1] = p.getSecond();
	    }
	    else {
		times[i][1] = p.getSecond() + times[i-1][1];
	    }

	    ++i;
	}

	return times;
    }

    /**
     * Ajoute les données d'une nouvelle exécution de la recherche tabu.
     * 
     * <p>Implementation brutale mais ça marche...</p>
     * 
     * @param time Le temps d'exécution
     * @param runs Le nombre de runs effectués
     * @param iterations Le nombre d'itérations effectués
     */
    public void add(long time, long runs, long iterations) {
	boolean stop = false;

	for(int i = 0 ; i < this.times_.size() && stop == false ; ++i) {
	    Pair<Long, Long> p = this.times_.get(i);

	    if(p.getFirst() == time) {
		p.setSecond(p.getSecond()+1);
		this.times_.set(i, p);
		stop = true;
	    }

	    else {
		if(p.getFirst() > time) {
		    this.times_.add(i, new Pair<Long, Long>(time, 1L));
		    stop = true;
		}
	    }
	}

	if(stop == false) {
	    this.times_.add(new Pair<Long, Long>(time, 1L));
	}

	stop = false;

	for(int i = 0 ; i < this.runs_.size() && stop == false ; ++i) {
	    Pair<Long, Long> p = this.runs_.get(i);

	    if(p.getFirst() == runs) {
		p.setSecond(p.getSecond()+1);
		this.runs_.set(i, p);
		stop = true;
	    }

	    else {
		if(p.getFirst() > runs) {
		    this.runs_.add(i, new Pair<Long, Long>(runs, 1L));
		    stop = true;
		}
	    }
	}

	if(stop == false) {
	    this.runs_.add(new Pair<Long, Long>(runs, 1L));
	}

	stop = false;

	for(int i = 0 ; i < this.iterations_.size() && stop == false ; ++i) {
	    Pair<Long, Long> p = this.iterations_.get(i);

	    if(p.getFirst() == iterations) {
		p.setSecond(p.getSecond()+1);
		this.iterations_.set(i, p);
		stop = true;
	    }

	    else {
		if(p.getFirst() > iterations) {
		    this.iterations_.add(i, new Pair<Long, Long>(iterations, 1L));
		    stop = true;
		}
	    }
	}

	if(stop == false) {
	    this.iterations_.add(new Pair<Long, Long>(iterations, 1L));
	}
    }
}
