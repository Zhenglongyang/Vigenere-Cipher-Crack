/* Nom, Matricule
 * Nom, Matricule
 */

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.*; 
import java.nio.file.Files; 
import java.nio.file.Paths;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Decrypt{
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static String decrypt(String text, String key) {
		String result = "";
		char alphabet[] = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		int keyCode[] = new int[key.length()];
		//Init keycode array
		for (int i = 0; i <key.length(); i++){
			for(int j=0;j<alphabet.length;j++)
				if(key.charAt(i)==alphabet[j])
					keyCode[i]= j;
		}


		System.out.println("Init Decryption");
		int counter =0;
		for(int i=0; i<text.length();i++) {
			for (int j = 0; j < alphabet.length; j++){
				if(alphabet[j] == text.charAt(i) ){
					if((j - keyCode[counter%key.length()])<0){
						result += alphabet[alphabet.length-Math.abs((j - keyCode[counter%key.length()]))];

					}
					else
						result += alphabet[(j - keyCode[counter%key.length()])%alphabet.length];
					//result += alphabet[((keyCode[counter%key.length()] - alphabet[j])%alphabet.length)];
					counter++;
					break;
				}

			}
		}
		System.out.println("end Decryption");
		return result;
	}

	public static int getKeySize(String text, double tolerance) {
		int keySize = 0;
		int maxKeywordLength = 210; // maximum keyword length

		float languageIndex = 0.065f; //theoretical value f or the language index
		char alphabet[]	= new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		String substring = "";
		String stringArray[] = new String[maxKeywordLength];

		int alphabetOccurences[];
		float frequencyArray[] = new float[maxKeywordLength];


		//Init alphabet occurence array
		alphabetOccurences = new int[maxKeywordLength];
		for (int i = 0; i < alphabet.length; i++) {
			alphabetOccurences[i] = 0;
		}

	//	System.out.println("Stage 1");

		//code to arrange the text into a single string
		String arrangedText="";
		for(int i=0; i<text.length();i++) {
			for (int j = 0; j < alphabet.length; j++)
			{
				if(text.charAt(i)==alphabet[j]){
					arrangedText += text.charAt(i);
					break;
				}
			}
		}

		text = arrangedText;
		// end
		//System.out.println("Stage 1 complete");

		//System.out.println("Stage 2");

		//Create a list of substrings for each period up until the maximum length defined earlier
		for(int period = 0; period< maxKeywordLength;period++){
			//add each current/period to an array
			for(int i=0; i<text.length();i++){
				substring += text.charAt(i);

				//Increment the count of a specific letter we found earlier
				for(int j=0; j<alphabet.length;j++) {
					if (text.charAt(i) == alphabet[j]) {
						alphabetOccurences[j]++;
					}
				}
				i+=period;
			}
			//calculate the frequency
			for(int k=0; k<alphabetOccurences.length; k++){
				frequencyArray[period]+= Math.pow((float)alphabetOccurences[k]/substring.length(),2);
			}

			//reset the alphabetOccurence table
			for (int l = 0; l < alphabet.length; l++) {
				alphabetOccurences[l] = 0;
			}

			stringArray[period] = substring;
			substring=""; //reset the substring

		}
		//System.out.println("Stage 2 complete");


		int keylength=-1;
		//find the closest to the theoritical value of the language index
		for(int i =0; i< frequencyArray.length; i++){
			if(frequencyArray[i]< languageIndex+ tolerance && frequencyArray[i]>languageIndex-tolerance)
				keylength=i;
		}

		//System.out.println("Keylength = "+keylength);

		keySize = keylength+1; //index starts a 0 so we have to add 1

		return keySize;
	}

	public static String getKey(String text, int keySize) {
		String result = "";
		//Fréquences théorique des lettres en anglais: f[0]=a, f[1]=b, etc.
		double[] f = new double[]{0.082, 0.015, 0.028, 0.043, 0.127, 0.022, 0.02,
				0.061, 0.07, 0.02, 0.08, 0.04, 0.024, 0.067, 0.015, 0.019, 0.001, 0.06,
				0.063, 0.091, 0.028, 0.02, 0.024, 0.002, 0.02, 0.001};


		char alphabet[] = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		String substring = "";
		int alphabetOccurences[];
		float frequencyList[] = new float[alphabet.length];
		int keyCodeArray[] = new int[keySize];

		//init keyCodeArray
		for (int i = 0; i < keyCodeArray.length; i++) {
			keyCodeArray[i] = 0;
		}

		//init String array
		String stringArray[] = new String[keySize];
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = "";
		}


		//Init alphabet occurence array
		alphabetOccurences = new int[alphabet.length];
		for (int i = 0; i < alphabet.length; i++) {
			alphabetOccurences[i] = 0;
		}


		//	System.out.println("Stage 1");

		//code to arrange the text into a single string
		String arrangedText = "";
		for (int i = 0; i < text.length(); i++) {
			for (int j = 0; j < alphabet.length; j++) {
				if (text.charAt(i) == alphabet[j]) {
					arrangedText += text.charAt(i);
					break;
				}
			}
		}

		text = arrangedText;


		//create substrings for each digit of the key
		for (int i = 0; i < text.length(); i++) {
			stringArray[i % keySize] += text.charAt(i);
		}

		//Now let us apply the frequency method in order to find the Key
		for (int i = 0; i < stringArray.length; i++) {
			//Count occurence of each letter of substring
			for (int j = 0; j < stringArray[i].length(); j++) {

				//if match alphabet letter increase the count
				for (int k = 0; k < alphabet.length; k++)
					if (stringArray[i].charAt(j) == alphabet[k])
						alphabetOccurences[k]++;


			}
			//calculate frequency
			for (int j = 0; j < alphabetOccurences.length; j++) {
				for (int k = 0; k < alphabetOccurences.length; k++)
					frequencyList[j] += (f[k] * alphabetOccurences[(k + j) % 26]) / stringArray[i].length();
			}

			//find max freq
			float max = 0;
			int maxIndex = -1; //if cannot find the keycode
			for (int maxI = 0; maxI < frequencyList.length; maxI++) {

				if (frequencyList[maxI] > max) {
					max = frequencyList[maxI];
					maxIndex = maxI;
				}
			}

			//setting the keycode
			keyCodeArray[i] = maxIndex;

			//resetting all the arrays

			for (int r = 0; r < alphabetOccurences.length; r++) {
				alphabetOccurences[r] = 0;
				frequencyList[r] = 0;
			}

		}

		//matching strings to keycode
		for (int i = 0; i < keyCodeArray.length; i++) {
			System.out.print(keyCodeArray[i] + ",");
			result += alphabet[keyCodeArray[i]];
		}
		System.out.println();
		System.out.println("Result string = "+result );

		return result;
	}

	public static void main(String args[]){
		String text = "";

		try{
			text += readFile("cipher.txt", StandardCharsets.UTF_8);
		}catch(IOException e) {
			System.out.println("Can't load file");
		}


		//TO DO: Vous devez trouver la tolérance nécessaire
		//à utiliser pour trouver la longueur de la clef
		float tolerance = 0.008f;
/*
		int keySize = getKeySize(text, tolerance);
		System.out.println("Keysize= "+keySize);
		String key = getKey(text, keySize);*/

		String key = getKey(text, 200);
		text = decrypt(text, key);


		try (PrintWriter out = new PrintWriter("result.txt")) {
		    out.println(text);

		}catch(IOException e) {
			System.out.println("Can't write file");
		}
	}


}