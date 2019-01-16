import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.text.ChangedCharSetException;

public class AnalizzatoreLessicale {

	//Dichiarazione delle variabili di istanza utili per la lettura del file
	private String testo_file= "";
	private int lunghezza;

	//Dichiarazione delle variabili di istanza che riguardano il metodo di getToken richiamato dall'analizzatore Sintattico
	private Token token;
	private int forward;
	private String variabile_esaminata = "";

	//Dichiarazione delle variabili di istanza per la definizione del metodo Digits
	private String stringa_intera = "";
	private String stringa_decimale = "";  
	private String stringa_esadecimale= "";
	boolean intero= false;
	boolean decimale= false;
	boolean esponenziale= false;

	//Dichiarazione delle variabili di istanza utili per il metodo che riconosce le variabili del programma
	private ArrayList<String> pattern_costrutti;
	private ArrayList<String> tabella_dei_Simboli;

	public AnalizzatoreLessicale() {
		pattern_costrutti= new ArrayList<>();
		pattern_costrutti.add("if");
		pattern_costrutti.add("for");
		pattern_costrutti.add("while");
		pattern_costrutti.add("do");
		pattern_costrutti.add("then");
		pattern_costrutti.add("else");
		pattern_costrutti.add("break");
		pattern_costrutti.add("continue");
		pattern_costrutti.add("switch");
		pattern_costrutti.add("case");

		tabella_dei_Simboli= new ArrayList<>();
		forward= 0;
	}
	
	public void leggiInput(String input) {
		// TODO Auto-generated method stub
		testo_file= input;
		
		lunghezza= testo_file.length();
		System.out.println(testo_file);
		System.out.println(testo_file.length());
	}

	public void leggiFile(String path) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file= new File("C:\\Users\\Antonio.Fasulo\\Desktop\\" + path + ".txt");
		Scanner in = new Scanner (file);

		while (in.hasNextLine()) {
			testo_file += "" + in.nextLine();
		}

		lunghezza= testo_file.length();
		System.out.println(testo_file);
		System.out.println(testo_file.length());
	}

	public Token getNextToken() {
		// TODO Auto-generated method studio

		while (true) {

			token= new Token();

			//Verifico finchè non trovo la fine del file di riferimento
			if (forward<lunghezza) {

				char c= testo_file.charAt(forward);

				//Questo switch permette di definire in quale automa andare ad effettuare il controllo di riferimento
				if (c == '>' || c == '<') {

					//Richiamo il metodo che effettua il controllo sull'automa RELOP
					token= checkAutomaRelop();
					if (token.getPattern() == "NULL") {
						token.setPattern("ERROR");
					}
					forward++;
					return token;
				}else if (Character.isLetter(c)){
					token= checkAutomaLetterOrDigits();
					forward++;
					return token;
				}else if (c == '('){
					token.setPattern("TondAperta");
					forward++;
					return token;
				}else if (c == ')') {
					token.setPattern("TondaChiusa");
					forward++;
					return token;
				}else if (c == '[') {
					token.setPattern("QuadraAperta");
					forward++;
					return token;
				}else if (c == ']') {
					token.setPattern("QuadraChiusa");
					forward++;
					return token;
				}else if (c == '{') {
					token.setPattern("GraffAperta");
					forward++;
					return token;
				}else if (c == '}') {
					token.setPattern("GraffaChiusa");
					forward++;
					return token;
				}else if (Character.isDigit(c)){

					int begin= forward;
					intero= checkIntero();
					forward= begin;
					decimale = checkDecimale();
					forward= begin;
					esponenziale= checkEsponenziale();
					
					//System.out.println("Valori ritornati: " + intero + decimale + esponenziale);
					
					if (intero == true && decimale == false && esponenziale == false) {
						forward= stringa_intera.length() + begin;
						token.setPattern("NCONST");
						token.setValore(stringa_intera);
						intero= false;
						decimale= false;
						esponenziale= false;
						stringa_intera= "";
						stringa_decimale= "";
						stringa_esadecimale= "";
						return token;
					}else if (decimale == true && esponenziale == false) {
						forward = stringa_decimale.length() + begin;
						token.setPattern("DECIMALE");
						token.setValore(stringa_decimale);
						intero= false;
						decimale= false;
						esponenziale= false;
						stringa_intera= "";
						stringa_decimale= "";
						stringa_esadecimale= "";
						return token;
					}else if (esponenziale == true) {
						System.out.println("Ecco la stringa esadecimale di ritorno: " + stringa_esadecimale);
						forward= stringa_esadecimale.length() + begin;
						token.setPattern("ESPONENZIALE");
						token.setValore(stringa_esadecimale);
						intero= false;
						decimale= false;
						esponenziale= false;
						stringa_intera= "";
						stringa_decimale= "";
						stringa_esadecimale= "";
						return token;
					}
				}else if (c == ','){
					token.setPattern("Virgola");
					forward++;
					return token;
				}else if (c == ';') {
					token.setPattern("SEMI");
					forward++;
					return token;
				}else if (c == '='){
					token.setPattern("ASSIGN");
					forward++;
					return token;
				}else {
					forward++;
				}
			}else {
				token.setPattern("EOF");
				return token;
			}

			//forward++;
		}
	}

	/*
	private void checkDigits() {
		// TODO Auto-generated method stub

		boolean checkpoint= false;
		boolean checkesp= false;

		for (; forward<lunghezza; forward++) {

			char c= testo_file.charAt(forward);

			if ((Character.isDigit(c) || c == ' ') && checkpoint == false && checkesp == false) {
				interbool= true;
				variabile_esaminata += "" + c;
			}else if (c == '.' && checkpoint == false && checkesp == false) {
				interbool= false;
				decibool= true;
				forward++;
				char c1= testo_file.charAt(forward);
				variabile_esaminata += ".";
				while (Character.isDigit(c1) && forward<lunghezza) {
					c1= testo_file.charAt(forward);
					variabile_esaminata += c1;
					forward++;
				}
				System.out.println("Sono fuori");
			}else if (c == 'E' && checkesp == false) {
				checkesp= true;
				forward++;
				char c_dec= testo_file.charAt(forward);
				if (Character.isDigit(c_dec)) {
					//interbool= false;
					esponetialbool= true;
					variabile_esaminata += "E" + c_dec;
				}else {
					break;
				}
			}else {
				//forward++;
				break;
			}
		}
	}
	 */

	private boolean checkEsponenziale() {
		// TODO Auto-generated method stub
		boolean check_esp= false;
		boolean check_dec= false;

		while (forward<lunghezza) {

			char c = testo_file.charAt(forward);

			if (Character.isDigit(c)) {
				stringa_esadecimale += c;
				forward++;
			}else if (c == '.' && check_dec == false) {
				check_dec= true;
				forward++;
				char c1= testo_file.charAt(forward);
				if (Character.isDigit(c1)) {
					stringa_esadecimale += "." + c1;
					forward++;
					//decimale= true;
				}else {
					forward--;
					//decimale= false;
					break;
				}
			}else if (c == 'E' && check_esp == false){
				check_esp= true;
				forward++;
				char c1 = testo_file.charAt(forward);
				if (Character.isDigit(c1)) {
					stringa_esadecimale += "E" + c1;
					forward++;
					esponenziale= true;
				}else {
					forward--;
					esponenziale= false;
					break;
				}
			}else {
				break;
			}
		}
		
		return esponenziale;

	}

	private boolean checkDecimale() {
		// TODO Auto-generated method stub

		boolean check_dec= false;

		while (forward<lunghezza) {

			char c = testo_file.charAt(forward);

			if (Character.isDigit(c)) {
				stringa_decimale += c;
				forward++;
			}else if (c == '.' && check_dec == false) {
				check_dec= true;
				forward++;
				char c1= testo_file.charAt(forward);
				if (Character.isDigit(c1)) {
					stringa_decimale += "." + c1;
					forward++;
					decimale= true;
				}else {
					forward--;
					decimale= false;
					break;
				}
			}else {
				break;
			}
		}
		
		return decimale;
	}

	private boolean checkIntero() {
		// TODO Auto-generated method stub

		while (forward<lunghezza) {

			char c= testo_file.charAt(forward);

			if (Character.isDigit(c)) {
				stringa_intera += c;
				forward++;
				intero= true;
			}else {
				break;
			}
		}
		
		return intero;
	}

	private Token checkAutomaLetterOrDigits() {
		// TODO Auto-generated method stub
		//Variabili di controllo booleane per verificare se appartiene ad un pattern
		boolean check_pattern= false;
		int check_simboli= -1;
		token = new Token();


		for (; forward<lunghezza; forward++) {

			char c = testo_file.charAt(forward);

			if (Character.isLetterOrDigit(c) && c != ' ') {
				variabile_esaminata += c;
			}else{
				forward--;
				break;
			}
		}

		//Da Questa parte del codice controllo se la variabile ritornata rappresenta un Identificativo o un pattern

		
		check_simboli= checkSimboli();
		check_pattern= checkPattern();

		//System.out.println("Ritorno di riferimento: " + check_simboli + check_pattern);

		if (check_simboli == -1 && check_pattern == false) {
			tabella_dei_Simboli.add(variabile_esaminata);
			token.setPattern("IDENTIFICATIVO");
			int valore= tabella_dei_Simboli.indexOf(variabile_esaminata);
			token.setValore("" + valore);
		}else if (checkPattern() == true) {
			System.out.println("Mi trovo qui");
			token.setPattern(variabile_esaminata);
		}else if (check_simboli<=tabella_dei_Simboli.size()){
			token.setPattern("IDENTIFICATIVO");
			token.setValore("" + check_simboli);
		}else if (tabella_dei_Simboli.size()==0){
			//System.out.println("Mi trovo qui");
			tabella_dei_Simboli.add(variabile_esaminata);
			token.setPattern("IDENTIFICATIVO");
			int valore= tabella_dei_Simboli.indexOf(variabile_esaminata);
			token.setValore("" + valore);
		}else {
			token.setPattern("ERRORE");
		}
		
		/*
		if (tabella_dei_Simboli.size()==0) {
			//System.out.println("Mi trovo qui");
			tabella_dei_Simboli.add(variabile_esaminata);
			token.setPattern("IDENTIFICATIVO");
			int valore= tabella_dei_Simboli.indexOf(variabile_esaminata);
			token.setValore("" + valore);
		}else {
			check_simboli= checkSimboli();
			check_pattern= checkPattern();

			//System.out.println("Ritorno di riferimento: " + check_simboli + check_pattern);

			if (check_simboli == -1 && check_pattern == false) {
				tabella_dei_Simboli.add(variabile_esaminata);
				token.setPattern("IDENTIFICATIVO");
				int valore= tabella_dei_Simboli.indexOf(variabile_esaminata);
				token.setValore("" + valore);
			}else if (checkPattern() == true) {
				System.out.println("Mi trovo qui");
				token.setPattern(variabile_esaminata);
			}else if (check_simboli<=tabella_dei_Simboli.size()){
				token.setPattern("IDENTIFICATIVO");
				token.setValore("" + check_simboli);
			}else {
				token.setPattern("ERRORE");
			}
		}
		*/

		//System.out.println("Valore della variabile in considerazione: " + variabile_esaminata);
		//token.setPattern(variabile_esaminata);
		variabile_esaminata = "";
		return token;
	}

	private int checkSimboli() {
		// TODO Auto-generated method stub

		int ritorno= -1;

		for (String s: tabella_dei_Simboli) {
			if (s.equals(variabile_esaminata)) {
				ritorno=  tabella_dei_Simboli.indexOf(s);
			}
		}

		return ritorno;
	}

	private boolean checkPattern() {
		// TODO Auto-generated method stub

		for (String s: pattern_costrutti) {
			if (s.equals(variabile_esaminata)) {
				return true;
			}
		}

		return false;
	}

	private Token checkAutomaRelop() {
		// TODO Auto-generated method stub
		token= new Token();

		char c= testo_file.charAt(forward);

		switch (c) {

		case '=' :
			token.setPattern("relop");
			token.setValore("EQ");
			return token;

		case '<' :
			forward++;
			c= testo_file.charAt(forward);
			if (c == '=') {
				token.setPattern("relop");
				token.setValore("LE");
				return token;
			}else if (c == '>') {
				token.setPattern("relop");
				token.setValore("NE");
				return token;
			}else if (c == '-'){
				//Bisogna verificare se appartiene alla classe Assign
				forward++;
				char c1= testo_file.charAt(forward);
				if (c1 == '-') {
					token.setPattern("ASSIGN");
					return token;
				}else {
					forward -= 2;
					token.setPattern("relop");
					token.setValore("LT");
					return token;
				}
			}else {
				forward--;
				token.setPattern("relop");
				token.setValore("LT");
				return token;
			}

		case '>' :
			forward++;
			c= testo_file.charAt(forward);
			if (c == '=') {
				token.setPattern("relop");
				token.setValore("GE");
				return token;
			}else {
				forward--;
				token.setPattern("relop");
				token.setValore("GT");
				return token;
			}

		default :
			token.setPattern("NULL");
			return token;
		}

		//return null;
	}

	public ArrayList<String> get_tabellaSimboli() {
		// TODO Auto-generated method stub
		return tabella_dei_Simboli;
	}
}