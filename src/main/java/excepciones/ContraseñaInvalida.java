package excepciones;

public class ContraseņaInvalida extends Exception{
	public ContraseņaInvalida(String msg) {
		super("Contraseņa incorrecta");
	}
}
