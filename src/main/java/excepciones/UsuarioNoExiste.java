package excepciones;

public class UsuarioNoExiste extends Exception{
	public UsuarioNoExiste(String msg) {
		super("Usuario no existe.");
	}
}
