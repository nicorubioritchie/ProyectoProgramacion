package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

public class DAO {
	private static Connection connection;

	private static Statement conectar() {
		try {
			BufferedReader lector = new BufferedReader(new FileReader("./bdconfig.ini"));
			String ip = lector.readLine();
			int puerto = Integer.parseInt(lector.readLine());
			String nombreBD = lector.readLine();
			String usuario = lector.readLine();
			String pass = lector.readLine();
			lector.close();
			connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + puerto + "/" + nombreBD, usuario,
					pass);
			return connection.createStatement();
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private static void desconectar(Statement s) {
		try {
			s.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int insertar(String tabla, HashMap<String, Object> columnas) throws SQLException {
		Statement smt = conectar();
		// insert into cliente (email,nombre,contraseña) values ('a@a.a','paco','mer');
		// insert into cliente (email,telefono,contraseña,nombre) values (
		String consulta = "insert into " + tabla + " (";
		Iterator it = columnas.keySet().iterator();
		while (it.hasNext()) {
			consulta += (String) it.next() + ",";
		}
		consulta = consulta.substring(0, consulta.length() - 1);
		consulta += ") values (";
		it = columnas.values().iterator();
		while (it.hasNext()) {
			Object elemento = it.next();
			if (elemento.getClass() != String.class && elemento.getClass() != Character.class) {
				consulta += elemento + ",";
			} else {
				consulta += "'" + (String) elemento + "',";
			}
		}
		consulta = consulta.substring(0, consulta.length() - 1);
		consulta += ")";
		System.out.println(consulta);
		int ret = smt.executeUpdate(consulta);
		desconectar(smt);
		return ret;
	}

	public static int borrar(String tabla, HashMap<String, Object> columnas) throws SQLException {
		Statement smt = conectar();
		String consulta = "delete from " + tabla + " where ";
		Iterator it = columnas.entrySet().iterator();
		while (it.hasNext()) {
			Entry actual = (Entry) it.next();
			if (actual.getValue().getClass() != String.class && actual.getValue().getClass() != Character.class) {
				consulta += (String) actual.getKey() + "=" + (String) actual.getValue() + " and ";
			} else {
				consulta += (String) actual.getKey() + "='" + (String) actual.getValue() + "' and ";
			}
		}
		consulta = consulta.substring(0, consulta.length() - 5);
		System.out.println(consulta);
		int ret = smt.executeUpdate(consulta);
		desconectar(smt);
		return ret;
	}

	public static ArrayList<Object> consultar(String tabla, LinkedHashSet<String> columnasSelect,
		HashMap<String, Object> restricciones) throws SQLException {
		Statement smt = conectar();

		String query = "select ";
		Iterator ith = columnasSelect.iterator();
		while (ith.hasNext()) {
			query += (String) ith.next() + ",";
		}
		query = query.substring(0, query.length() - 1) + " from " + tabla + (restricciones.size() > 0 ? " where " : "");
		// select email,nombre,password,telefono from cliente where email='asdad' and
		Iterator itm = restricciones.entrySet().iterator();
		while (itm.hasNext()) {
			Entry actual = (Entry) itm.next();
			if (actual.getValue().getClass() != String.class && actual.getValue().getClass() != Character.class) {
				query += (String) actual.getKey() + "=" + (String) actual.getValue() + " and ";
			} else {
				query += (String) actual.getKey() + "='" + (String) actual.getValue() + "' and ";
			}
		}
		if (restricciones.size() > 0) {
			query = query.substring(0, query.length() - 5);
		}
		System.out.println(query);
		ResultSet cursor = smt.executeQuery(query);
		ArrayList<Object> fila = new ArrayList<Object>();
		while (cursor.next()) {
			Iterator hsCols = columnasSelect.iterator();
			while (hsCols.hasNext()) {
				String nombreCol = (String) hsCols.next();
				try {
					fila.add(cursor.getInt(cursor.findColumn(nombreCol)));
				} catch (NumberFormatException | SQLException e) {
					fila.add(cursor.getString(cursor.findColumn(nombreCol)));
				}
			}

		}
		desconectar(smt);
		return fila;
	}

	public static int actualizar(String tabla, HashMap<String, Object> datosAModificar,
			HashMap<String, Object> restricciones) throws SQLException {
		String query = "update " + tabla + " set ";
		Iterator itm = datosAModificar.entrySet().iterator();
		while (itm.hasNext()) {
			Entry actual = (Entry) itm.next();
			if (actual.getValue().getClass() != String.class && actual.getValue().getClass() != Character.class) {
				query += actual.getKey() + " = " + actual.getValue() + ",";
			} else {
				query += actual.getKey() + " = '" + actual.getValue() + "',";
			}
		}
		query = query.substring(0, query.length() - 1) + " where ";
		Iterator itr = restricciones.entrySet().iterator();
		while (itr.hasNext()) {
			Entry actual = (Entry) itr.next();
			if (actual.getValue().getClass() != String.class && actual.getValue().getClass() != Character.class) {
				query += actual.getKey() + " = " + actual.getValue() + " and ";
			} else {
				query += actual.getKey() + " = '" + actual.getValue() + "' and ";
			}
		}
		query = query.substring(0, query.length() - 5);

		Statement smt = conectar();
		System.out.println(query);
		int ret = smt.executeUpdate(query);
		desconectar(smt);

		return ret;
	}

}
