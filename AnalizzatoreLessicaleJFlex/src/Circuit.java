// Circuit.java
//
// Code to join lexer and parser for circuit description language.
//
// Ian Stark


public class Circuit {
	
	public static void main(String[] args) throws Exception {
		
	   	System.out.println("Type in input, hit Return");
		System.out.println("To finish hit Return then Ctrl-Z (or Ctrl-D on a MacOSX)");
		System.out.println("(if it does not finish, before Ctrl-Z/D give focus to another window first)");
	   	Yylex yy = new Yylex(System.in);
	   	
	   	for(int tokenId= yy.next_token().sym; tokenId != CircuitSym.EOF; tokenId  = yy.next_token().sym) {
	   		System.out.println("token returned is "+ CircuitSym.terminalNames[tokenId] +" "+ yy.yytext().toString() + "\n");
	   	}
	   	
		System.out.println("The end, thank you! ");
		
	}
}

