
public class Token {
	
	//Dichiarazione delle variabili d'istanza della classe
	private String pattern;
	private String valore;
	private int valore_int;
	
	
	public Token (String identificativo, String valore) {
		this.pattern= identificativo;
		this.valore= valore;
	}
	
	
	public Token (String identificativo, int valore) {
		this.pattern= identificativo;
		this.valore_int= valore;
	}
	
	public Token () {
		//this.pattern= identificativo;
		//this.valore= valore;
	}
	
	public Token (String identificativo) {
		this.pattern= identificativo;
	}

	//Inserimento dei metodi Get & Set
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getValore_int() {
		return valore_int;
	}

	public void setValore_int(int valore_int) {
		this.valore_int = valore_int;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String i) {
		this.valore = i;
	}
	
	public void setValoreIntero(int i) {
		this.valore_int = i;
	}
}