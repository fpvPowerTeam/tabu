package util;
/**
 * Une paire d'elements.
 * 
 * <p>
 * Code recupere <a href="http://stackoverflow.com/a/677248">ici</a>.
 * </p>
 * 
 * @param <A> Type du premier element de la paire
 * @param <B> Type du second element de la paire
 */
public class Pair<A extends Comparable<A>, B extends Comparable<B>>
implements Comparable<Pair<A, B>>, Cloneable {

    private A first;
    private B second;

    public Pair(A first, B second) {
	super();
	this.first = first;
	this.second = second;
    }

    public Pair(Pair<A,B> other) {
	this.first = other.first;
	this.second = other.second;
    }

    public int hashCode() {
	int hashFirst = first != null ? first.hashCode() : 0;
	int hashSecond = second != null ? second.hashCode() : 0;

	return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
	if (other instanceof Pair) {
	    @SuppressWarnings({ "unchecked", "rawtypes" }) // XXX
            Pair<A,B> otherPair = (Pair) other;

	    return (
	        (this.first == otherPair.first || (this.first != null && otherPair.first != null && this.first.equals(otherPair.first)))
	        && (this.second == otherPair.second || (this.second != null && otherPair.second != null && this.second.equals(otherPair.second)))
	    );
	    
	}

	return false;
    }

    public String toString() {
	return "(" + first + ", " + second + ")";
    }

    public A getFirst() {
	return first;
    }

    public void setFirst(A first) {
	this.first = first;
    }

    public B getSecond() {
	return second;
    }

    public void setSecond(B second) {
	this.second = second;
    }

    @Override
    public int compareTo(Pair<A, B> other) {
	int cmp = this.first.compareTo(other.first);
	
	return (cmp == 0) ? this.second.compareTo(other.second) : cmp;
    }
    
    public Object clone() {
	Object o = null;
	try {
		// On récupère l'instance à renvoyer par l'appel de la 
		// méthode super.clone()
		o = super.clone();
	} catch(CloneNotSupportedException cnse) {
		// Ne devrait jamais arriver car nous implémentons 
		// l'interface Cloneable
		cnse.printStackTrace(System.err);
	}
	// on renvoie le clone
	return o;
    }
}