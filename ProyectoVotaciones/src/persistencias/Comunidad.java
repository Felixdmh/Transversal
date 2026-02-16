package persistencias;

public class Comunidad {
	private String nombreComunidad;
	private int rango1_9;
	private int rango10_17;
	private int rango18_25;
	private int rango26_40;
	private int rango41_65;
	private int rangoMas66;
	private int totalHabitantes;

	public Comunidad() {
	}

	public String getNombreComunidad() {
		return nombreComunidad;
	}

	public void setNombreComunidad(String nombreComunidad) {
		this.nombreComunidad = nombreComunidad;
	}

	public int getRango1_9() {
		return rango1_9;
	}

	public void setRango1_9(int rango1_9) {
		this.rango1_9 = rango1_9;
	}

	public int getRango10_17() {
		return rango10_17;
	}

	public void setRango10_17(int rango10_17) {
		this.rango10_17 = rango10_17;
	}

	public int getRango18_25() {
		return rango18_25;
	}

	public void setRango18_25(int rango18_25) {
		this.rango18_25 = rango18_25;
	}

	public int getRango26_40() {
		return rango26_40;
	}

	public void setRango26_40(int rango26_40) {
		this.rango26_40 = rango26_40;
	}

	public int getRango41_65() {
		return rango41_65;
	}

	public void setRango41_65(int rango41_65) {
		this.rango41_65 = rango41_65;
	}

	public int getRangoMas66() {
		return rangoMas66;
	}

	public void setRangoMas66(int rangoMas66) {
		this.rangoMas66 = rangoMas66;
	}

	public int getTotalHabitantes() {
		return totalHabitantes;
	}

	public void setTotalHabitantes(int totalHabitantes) {
		this.totalHabitantes = totalHabitantes;
	}

	@Override
	public String toString() {
		return nombreComunidad;
	}
}