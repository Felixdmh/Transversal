package Controlador;

import java.sql.Connection;
import java.util.Random;

public class HiloVotante extends Thread {

	private String comunidad;
	private String rangoEdad;
	private BaseDeDatos bd;
	private Connection con;

	public HiloVotante(String comunidad, String rangoEdad, BaseDeDatos bd, Connection con) {
		this.comunidad = comunidad;
		this.rangoEdad = rangoEdad;
		this.bd = bd;
		this.con = con;
	}

	@Override
	public void run() {

		try {

			int numero = (int) (Math.random() * 101); // 0 al 100

			String partido = calcularPartido(numero);

			bd.insertarVoto(con, comunidad, rangoEdad, partido);

			Thread.sleep(100);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String calcularPartido(int numero) { // public mejor que private para los test maven

		if (rangoEdad.equals("18_25")) {
			if (numero <= 30)
				return "PP";
			else if (numero <= 50)
				return "PSOE";
			else if (numero <= 70)
				return "VOX";
			else
				return "SALF";
		}

		if (rangoEdad.equals("26_40")) {
			if (numero <= 20)
				return "PP";
			else if (numero <= 55)
				return "PSOE";
			else if (numero <= 85)
				return "VOX";
			else
				return "SALF";
		}

		if (rangoEdad.equals("41_65")) {
			if (numero <= 10)
				return "PP";
			else if (numero <= 55)
				return "PSOE";
			else if (numero <= 90)
				return "VOX";
			else
				return "SALF";
		}

		if (rangoEdad.equals("MAS_66")) {
			if (numero <= 25)
				return "PP";
			else if (numero <= 60)
				return "PSOE";
			else if (numero <= 95)
				return "VOX";
			else
				return "SALF";
		}

		return "PP";
	}

}