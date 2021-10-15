package information;

import java.util.*;

/** 
 *  
 * @author prou
 */	
public  class Information <T>  implements Iterable <T> {
   
    private LinkedList <T> content;
   
    /**
     * pour construire une information vide
     */
    public Information() {
	this.content = new LinkedList <T> (); 
    }
   
    /**
     * pour construire a partir d'un tableau de T une information
     * @param content le tableau d'elements pour initialiser l'information construite
     */
    public Information(T [] content) {
	this.content = new LinkedList <T> (); 
	for (int i = 0; i < content.length; i++) {
            this.content.addLast(content[i]);
	}
    }
   
    /**
     * pour connaitre le nombre d'elements d'une information
     * @return le nombre d'elements de l'information
     */
    public int nbElements() {
	return this.content.size();
    }
   
    /**
     * pour renvoyer un element d'une information
     * @return le ieme element de l'information
     */
    public T iemeElement(int i) {
	return this.content.get(i);
    }
   
    /**
     * pour modifier le ieme element d'une information
     */
    public void setIemeElement(int i, T v) {
	this.content.set(i, v);
    }
   
    /**
     * pour ajouter un element a la fin de l'information 
     * @param valeur  l'element a rajouter
     */
    public void add(T valeur) {
	this.content.add(valeur);
    }
   
   
    /**
     * pour comparer l'information courante avec une autre information
     * @param o  l'information  avec laquelle se comparer
     * @return "true" si les 2 informations contiennent les memes
     * elements aux memes places; "false" dans les autres cas
     */	 
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
	if (!(o instanceof Information))
            return false;
	Information <T> information =  (Information <T>) o;
	if (this.nbElements() != information.nbElements())
            return false;
	for (int i = 0; i < this.nbElements(); i++) {
            if (!this.iemeElement(i).equals(information.iemeElement(i)))
		return false;
	}
	return true;
    }
   
    /**
     * pour afficher une information
     */
    public String toString() {
	String s = "";
	for (int i = 0; i < this.nbElements(); i++) {
            s += " " + this.iemeElement(i);
	}
	return s;
    }
   
    /**
     * pour utilisation du "for each" 
     */
    public Iterator <T> iterator() {
	return content.iterator();
    }
}
