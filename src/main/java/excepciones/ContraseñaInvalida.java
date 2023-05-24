package excepciones;

public class ContraseñaInvalida extends Exception{
	public ContraseñaInvalida(String msg) {
		super("Contraseña incorrecta");
	}
}
