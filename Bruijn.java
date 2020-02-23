/* Dina Benkirane, 20078896
 * Zheng Long Yang, Matricule
 */

import java.util.*;
import java.util.Arrays;

public class Bruijn{
	//Génère un cycle de de Bruijn pour des mots de longueur n
	//composés de k symboles différents
	public static String B(int k, int n){
		String result = "";

		//Taille de result
		int size = (int) Math.pow(k,n);

		//Emmagasine en mémoire quel index du "mot du haut"
		//a été visité
		BitSet visited = new BitSet(size);

		//Initialisation du tableau avec la séquence de chiffre répété
		int[] repeat_arr= new int[size];

		for (int i=0; i<size; i++){
			repeat_arr[i]=i%k;
		}

		//Initialisation du tableau sorted
		int[] sort_arr=repeat_arr.clone();
		Arrays.sort(sort_arr);

		//Tableau qui contient les index de correspondance du tableau Sorted à celui qui répéte les symbole.
		int[] correspondance= new int [size];
		for (int i=0; i<size;i++){
			//Formule qui détermine l'index dans repeat_arr associé
			correspondance[i] = ((i - (sort_arr[i] * (size / k))) * k) + sort_arr[i];
		}

		//variable qui fixe le début des cycles
		int to_visit=0;

		while(result.length() != size){
			//Lorsque la variable à visiter est déjà visiter on fini le cycle et on commence le prochain
			while (!visited.get(to_visit)){
				result+=repeat_arr[correspondance[to_visit]];
				//Keep track of what is visited
				visited.set(to_visit);
				//Poursuit le cycle
				to_visit=correspondance[to_visit];
			}

			//Lorsqu'un cycle est achevé on regarde le prochain index à partir du début qui est pas visité.
			to_visit= visited.nextClearBit(0);
		}

		return result;
	}

	public static void main(String args[]){

		System.out.println(B(10,4));


	}
}