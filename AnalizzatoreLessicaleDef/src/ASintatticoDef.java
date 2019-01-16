import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ASintatticoDef {

	//Variabile nella quale sono contenuti l'insieme dei token ritornati nell'analisi lessicale
	private static ArrayList<Token> token_ritornati= new ArrayList<>();
	private static ArrayList<String> tabella_simboli= new ArrayList<>();
	private static ArrayList<String> azioni= new ArrayList<>();

	//Dichiarazione di tutte le produzioni della Grammatica
	private static String produzione1= "Program -> Stmt Program 1";
	private static String produzione2= "Program1 -> ; Stmt Program 1";
	private static String produzione3= "Program1 -> epsilon";
	private static String produzione4= "Stmt -> IF Expr THEN Stmt";
	private static String produzione5= "Stmt -> ID ASSIGN Expr";
	private static String produzione6= "Expr -> Term Expr1";
	private static String produzione7= "Expr1 -> RELOP TERM";
	private static String produzione8= "Expr1 -> epsilon";
	private static String produzione9= "Term -> ID";
	private static String produzione10= "Term -> NUMBER";

	//Variabile che permette di tenere traccia dei token esaminati nell'analisi sintattica
	private static int contatore_token;

	//Traccia delle produzioni per accettazione dell'input
	private static ArrayList<String> traccia_produzioni= new ArrayList<>();

	public static void main (String []args) throws FileNotFoundException {

		System.out.println("Digita il nome del file di lettura che hai sul Desktop: ");
		Scanner in= new Scanner (System.in);
		String path= in.nextLine();

		//Creo un istanza dell'oggetto Analizzatore Lessicale
		AnalizzatoreLessicale lessicale= new AnalizzatoreLessicale();
		lessicale.leggiFile(path);

		//Dopo aver letto il File bisogna chiamare ripetutamente i vari Token finchè non arriva un Token che segni la fine del file denotato con EOF
		/*
		Token token= new Token();
		token= lessicale.getNextToken2();
		System.out.println("Stampo il valore di ritorno: " + token.getPattern());
		 */

		int i= 0;
		while (true) {

			i++;
			Token token= new Token();
			token= lessicale.getNextToken();
			token_ritornati.add(token);

			if (token.getPattern().equals("EOF")) {
				System.out.println("Stampo il valore del token finale: " + token.getPattern());
				break;
			}else {
				//System.out.println("Effettuo la chiamata: " + i);
				System.out.println("< " + token.getPattern() + ", " + token.getValore() + " >");
			}
		}

		System.out.println("Inizio della fase di ANALISI SINTATTICA....");

		//Prendo riferimento alla tabella dei simboli creata nel passo precedente da utilizzare anche in questa fase
		tabella_simboli= lessicale.get_tabellaSimboli();
		
		/*
		 * Definizione della Grammatica che deve essere soddisfatta
		 */
		System.out.println("\n\nEcco la Grammatica che bisogna considerare come input: " + "\n\n" +
				"PROGRAM -> STMT PROGRAM1" + "\n" +
				"PROGRAM1 -> ;STMT PROGRAM1 | EPSILON" + "\n" +
				"STMT -> if EXPR then STMT | id assign EXPR" + "\n" +
				"EXPR -> TERM EXPR1" + "\n" + 
				"EXPR1 -> relop TERM | EPSILON" + "\n" + 
				"TERM -> id|number" + "\n\n\n");
		
		contatore_token = 0;
		
		azioni.add(produzione1);
		
		boolean controllo_stmt= false;
		boolean controlloProgram1= false;
		controllo_stmt = checkStmt();
		
		//Da qui iniziano i problemi per l'attività di Program1
		if (controllo_stmt == true) {
			//System.out.println("Tutto Ok mi trovo quiF");
			controlloProgram1 = checkProgram1();
			
			int prova= contatore_token+1;
			while (controlloProgram1 == true) {
				System.out.println("VALORE" + controlloProgram1);
				controlloProgram1 = checkProgram1();
				prova= contatore_token+1;
			}
			
			if (token_ritornati.get(contatore_token).getPattern().equals("EOF")) {
				azioni.add(produzione3);
				System.out.println("LA STRINGA INSERITA IN INPUT RISPETTA LA GRAMMATICA.");
			}else {
				System.out.println("Mi dispiace la stringa inserita non appartiene alla Grammatica");
			}
			
		}else {
			System.out.println("Mgi dispiace l'input non appartiene al Linguaggio... ");
		}
		
		for (String s : azioni) {
			System.out.println("Produzione: " + s);
		}
	}

	private static boolean checkProgram1() {
		// TODO Auto-generated method stub
		boolean ritorno = false;
		boolean checkStmt = false;
		//boolean checkSelf= true;
		
		/*
		if (token_ritornati.get(contatore_token).getPattern().equals("SEMI")) {
			azioni.add(produzione2);
			contatore_token++;
			checkStmt = checkStmt();
			while (checkStmt == true) {
				if (token_ritornati.get(contatore_token).getPattern().equals("EOF")) {
					azioni.add(produzione3);
					ritorno = true;
					System.out.println("PELEEE");
				}else {
					checkSelf = checkProgram1();
				}
			}
		}else {
			ritorno = false;
		}
		*/
		
		if (token_ritornati.get(contatore_token).getPattern().equals("SEMI")) {
			azioni.add(produzione2);
			contatore_token++;
			checkStmt = checkStmt();
			if (checkStmt == true) {
				ritorno = true;
			}else {
				ritorno = false;
			}
		}else {
			ritorno = false;
		}
		
		return ritorno;
	}

	private static boolean checkStmt() {
		// TODO Auto-generated method stub
		boolean ritorno = true;
		boolean controllo_expr = false;
		boolean controllo_stmt = false;
		System.out.println("Mi trovo nella procedura");
		
		if (token_ritornati.get(contatore_token).getPattern().equals("if")) {
			azioni.add(produzione4);
			contatore_token++;
			//Controllo dell'Expr
			controllo_expr = checkExpr();
			
			if (controllo_expr == true) {
				if (token_ritornati.get(contatore_token).getPattern().equals("then")) {
					contatore_token++;
					controllo_stmt = checkStmt();
					
					if (controllo_stmt == false) {
						ritorno = false;
					}
				}
			}
			
		}else if (token_ritornati.get(contatore_token).getPattern().equals("IDENTIFICATIVO")) {
			contatore_token++;
			if (token_ritornati.get(contatore_token).getPattern().equals("ASSIGN")) {
				azioni.add(produzione5);
				contatore_token++;
				//controllo dell'Expr
				controllo_expr = checkExpr();
			}else {
				ritorno = false;
			}
		}else {
			ritorno = false;
		}
		
		if (ritorno == true && controllo_expr == true) {
			return ritorno = true;
		}else {
			return ritorno = false;
		}
	}

	private static boolean checkExpr() {
		// TODO Auto-generated method stub
		boolean ritorno = true;
		boolean controllo_expr1 = false;
		//Variabile boolean in più che permette di controllare il ritorno di term
		boolean controllo_term = false;
		
		azioni.add(produzione6);
		
		controllo_term = checkTerm();
		
		if (controllo_term == true) {
			
			controllo_expr1 = check_Expr1();
			
		}else {
			ritorno = false;
		}
		
		if (ritorno == true && controllo_expr1 == true) {
			return ritorno = true;
		}else {
			return ritorno = false;
		}
	}

	private static boolean check_Expr1() {
		// TODO Auto-generated method stub
		boolean ritorno = true;
		
		if (token_ritornati.get(contatore_token).getPattern().equals("relop")) {
			azioni.add(produzione7);
			contatore_token++;
			ritorno = checkTerm();
		}else {
			azioni.add(produzione8);
		}
		
		return ritorno;
	}

	private static boolean checkTerm() {
		// TODO Auto-generated method stub
		//boolean ritorno = true;
		
		if (token_ritornati.get(contatore_token).getPattern().equals("IDENTIFICATIVO")) {
			azioni.add(produzione9);
			contatore_token++;
			return true;
		}else if (token_ritornati.get(contatore_token).getPattern().equals("NCONST")) {
			azioni.add(produzione10);
			contatore_token++;
			return true;
		}else {
			return false;
		}
	}
}