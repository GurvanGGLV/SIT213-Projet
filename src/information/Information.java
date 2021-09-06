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
     * pour construire Ã  partir d'un tableau de T une information
     * @param content le tableau d'éléments pour initialiser l'information construite
     */
    public Information(T [] content) {
	this.content = new LinkedList <T> (); 
	for (int i = 0; i < content.length; i++) {
            this.content.addLast(content[i]);
	}
    }
   
    /**
     * pour connaÃ®tre le nombre d'éléments d'une information
     * @return le nombre d'éléments de l'information
     */
    public int nbElements() {
	return this.content.size();
    }
   
    /**
     * pour renvoyer un élément d'une information
     * @return le ieme élément de l'information
     */
    public T iemeElement(int i) {
	return this.content.get(i);
    }
   
    /**
     * pour modifier le iÃ¨me élément d'une information
     */
    public void setIemeElement(int i, T v) {
	this.content.set(i, v);
    }
   
    /**
     * pour ajouter un élément Ã  la fin de l'information 
     * @param valeur  l'élément Ã  rajouter
     */
    public void add(T valeur) {
	this.content.add(valeur);
    }
   
   
    /**
     * pour comparer l'information courante avec une autre information
     * @param o  l'information  avec laquelle se comparer
     * @return "true" si les 2 informations contiennent les mÃªmes
     * éléments aux mÃªmes places; "false" dans les autres cas
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
