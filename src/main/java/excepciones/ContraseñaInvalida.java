package excepciones;

public class Contrase�aInvalida extends Exception{
	public Contrase�aInvalida(String msg) {
		super("Contrase�a incorrecta");
	}
}
