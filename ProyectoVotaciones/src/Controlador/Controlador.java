package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Color;
import java.util.Map;
import java.util.HashMap;

import Vista.Vista;
import persistencias.Comunidad;

public class Controlador implements MouseListener, ActionListener, ListSelectionListener {

	private final Vista vista;
	private final BaseDeDatos bd;

	// Guardamos la comunidad seleccionada para usarla en PanelDetalle
	private Comunidad comunidadSeleccionada;
	private List<Thread> listaHilos = new ArrayList<>();
	private List<Comunidad> listaOriginalComunidades = new ArrayList<>();
	private ChartPanel chartPanelActual;

	public Controlador(Vista vista) {
		this.vista = vista;
		this.bd = new BaseDeDatos();

		// ===== Panel inicial al arrancar =====
		this.vista.PanelInicio.setVisible(true);
		this.vista.PanelMapa.setVisible(false);
		this.vista.PanelDetalle.setVisible(false);

		// ===== Listeners =====
		this.vista.btnIniciarSimulacion.addActionListener(this);
		this.vista.btnVolverDetalle.addActionListener(this);
		this.vista.btnCerrar.addActionListener(this);

		this.vista.listComunidades.addListSelectionListener(this);

		// IMPORTANTE: para combos usamos ActionListener tambi�n
		this.vista.comboFiltroMapa.addActionListener(this);
		this.vista.comboFiltroPersonas.addActionListener(this);

		// ===== Paso A: rellenar combos =====
		cargarFiltros();

		// ===== Cargar comunidades desde BBDD =====
		cargarComunidadesAsync();
	}

	// =========================
	// PASO A: CARGAR FILTROS
	// =========================
	private void cargarFiltros() {

		// ---- comboFiltroMapa (global) ----
		DefaultComboBoxModel<String> modelMapa = new DefaultComboBoxModel<>();
		modelMapa.addElement("Mostrar todas");
		modelMapa.addElement("PP");
		modelMapa.addElement("PSOE");
		modelMapa.addElement("VOX");
		modelMapa.addElement("SALF");

		vista.comboFiltroMapa.setModel(modelMapa);
		vista.comboFiltroMapa.setSelectedIndex(0);

		// ---- comboFiltroPersonas (detalle) ----
		DefaultComboBoxModel<String> modelPersonas = new DefaultComboBoxModel<>();
		modelPersonas.addElement("Selecciona rango de edad");
		modelPersonas.addElement("18-25");
		modelPersonas.addElement("26-40");
		modelPersonas.addElement("41-65");
		modelPersonas.addElement("66+");
		vista.comboFiltroPersonas.setModel(modelPersonas);
		vista.comboFiltroPersonas.setSelectedIndex(0);
	}

	private void cargarComunidadesAsync() {
		new SwingWorker<DefaultListModel<Comunidad>, Void>() {

			@Override
			protected DefaultListModel<Comunidad> doInBackground() throws Exception {
				Connection con = null;

				try {
					con = bd.createConnection();
					List<Comunidad> comunidades = bd.selectComunidades(con);

					DefaultListModel<Comunidad> model = new DefaultListModel<>();
					for (Comunidad c : comunidades) {
						model.addElement(c);
					}

					con.commit();
					return model;

				} catch (Exception e) {
					if (con != null)
						con.rollback();
					e.printStackTrace();
					throw e;

				} finally {
					if (con != null)
						bd.disconnect(con);
				}
			}

			@Override
			protected void done() {
				try {
					vista.listComunidades.setModel(get());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.execute();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// ---- SALIR ----
		if (e.getSource() == vista.btnCerrar) {
			System.exit(0);
		}

		// ---- INICIAR SIMULACI�N
		if (e.getSource() == vista.btnIniciarSimulacion) {

			vista.PanelInicio.setVisible(false);
			vista.PanelDetalle.setVisible(false);
			vista.PanelMapa.setVisible(true);

			listaHilos.clear();

			try {

				Connection conexionGlobal = bd.createConnection();

				for (int i = 0; i < vista.listComunidades.getModel().getSize(); i++) {
					Comunidad c = (Comunidad) vista.listComunidades.getModel().getElementAt(i);
					generarHilosComunidad(c, conexionGlobal);
				}

				for (Thread t : listaHilos) {
					t.start();
				}

				for (Thread t : listaHilos) {
					t.join();
				}

				conexionGlobal.commit();
				bd.disconnect(conexionGlobal);

				actualizarListaGanadores();

				Connection con = bd.createConnection();
				String ganadorEspana = bd.obtenerGanadorEspana(con);
				pintarGanadorEspana(ganadorEspana);
				bd.disconnect(con);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return;
		}

		if (e.getSource() == vista.btnVolverDetalle) {
			vista.PanelDetalle.setVisible(false);
			vista.PanelMapa.setVisible(true);
			return;
		}

		// ---- CAMBIO EN comboFiltroMapa ----
		if (e.getSource() == vista.comboFiltroMapa) {

			String opcion = (String) vista.comboFiltroMapa.getSelectedItem();

			if (opcion == null)
				return;

			filtrarPorPartido(opcion);
			return;
		}

		// ---- CAMBIO EN comboFiltroPersonas ----
		if (e.getSource() == vista.comboFiltroPersonas) {
			if (e.getSource() == vista.comboFiltroPersonas) {

				if (comunidadSeleccionada == null)
					return;

				String seleccion = (String) vista.comboFiltroPersonas.getSelectedItem();

				String rangoBD = null;

				switch (seleccion) {
				case "18-25":
					rangoBD = "18_25";
					break;
				case "26-40":
					rangoBD = "26_40";
					break;
				case "41-65":
					rangoBD = "41_65";
					break;
				case "66+":
					rangoBD = "MAS_66";
					break;
				default:
					rangoBD = null;
				}

				mostrarGraficoComunidad(comunidadSeleccionada.getNombreComunidad(), rangoBD);

				return;
			}

		}
	}

	// CLICK EN LISTA DE CCAA -> IR A DETALLE
	@Override
	public void valueChanged(ListSelectionEvent e) {

		if (e.getValueIsAdjusting())
			return;

		if (e.getSource() == vista.listComunidades) {
			Comunidad seleccionada = (Comunidad) vista.listComunidades.getSelectedValue();
			if (seleccionada == null)
				return;

			this.comunidadSeleccionada = seleccionada;

			// Cambiar panel
			vista.PanelMapa.setVisible(false);
			vista.PanelDetalle.setVisible(true);

			// Poner nombre
			vista.lblNombre.setText(seleccionada.getNombreComunidad());
			mostrarGraficoComunidad(seleccionada.getNombreComunidad(), null);

			// Resetear el combo de personas al entrar
			vista.comboFiltroPersonas.setSelectedIndex(0);

			if (vista.listDetalle != null) {
				vista.listDetalle.setModel(new DefaultListModel<>());
			}
		}
	}

	private void calcularHilosComunidad(Comunidad comunidad) {

		System.out.println("---- " + comunidad.getNombreComunidad() + " ----");

		int hilos18_25 = comunidad.calcularHilosRango(comunidad.getRango18_25());
		hilos18_25 = comunidad.aplicarMinimoHilos(hilos18_25);

		int hilos26_40 = comunidad.calcularHilosRango(comunidad.getRango26_40());
		hilos26_40 = comunidad.aplicarMinimoHilos(hilos26_40);

		int hilos41_65 = comunidad.calcularHilosRango(comunidad.getRango41_65());
		hilos41_65 = comunidad.aplicarMinimoHilos(hilos41_65);

		int hilosMas66 = comunidad.calcularHilosRango(comunidad.getRangoMas66());
		hilosMas66 = comunidad.aplicarMinimoHilos(hilosMas66);

		System.out.println("18-25: " + hilos18_25 + " hilos");
		System.out.println("26-40: " + hilos26_40 + " hilos");
		System.out.println("41-65: " + hilos41_65 + " hilos");
		System.out.println("+66: " + hilosMas66 + " hilos");
	}

	private void generarHilosComunidad(Comunidad comunidad, Connection con) {

		String nombre = comunidad.getNombreComunidad();

		crearHilosPorRango(comunidad, nombre, "18_25", comunidad.getRango18_25(), con);
		crearHilosPorRango(comunidad, nombre, "26_40", comunidad.getRango26_40(), con);
		crearHilosPorRango(comunidad, nombre, "41_65", comunidad.getRango41_65(), con);
		crearHilosPorRango(comunidad, nombre, "MAS_66", comunidad.getRangoMas66(), con);
	}

	private void crearHilosPorRango(Comunidad comunidad, String nombreComunidad, String rango, int porcentaje,
			Connection con) {

		int hilos = comunidad.calcularHilosRango(porcentaje);
		hilos = comunidad.aplicarMinimoHilos(hilos);

		for (int i = 0; i < hilos; i++) {
			HiloVotante hilo = new HiloVotante(nombreComunidad, rango, bd, con);
			listaHilos.add(hilo);
		}
	}

	private void actualizarListaGanadores() {

		try {

			Connection con = bd.createConnection();

			DefaultListModel<Comunidad> modelo = new DefaultListModel<>();

			listaOriginalComunidades.clear();

			for (int i = 0; i < vista.listComunidades.getModel().getSize(); i++) {

				Comunidad c = (Comunidad) vista.listComunidades.getModel().getElementAt(i);

				String ganador = bd.obtenerGanadorPorComunidad(con, c.getNombreComunidad());

				pintarComunidad(c.getNombreComunidad(), ganador);

				c.setPartidoGanador(ganador);

				listaOriginalComunidades.add(c);

				modelo.addElement(c);
			}

			vista.listComunidades.setModel(modelo);

			bd.disconnect(con);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pintarComunidad(String nombreComunidad, String partido) {

		JLabel label = obtenerLabelPorNombre(nombreComunidad);

		if (label == null)
			return;

		switch (partido) {
		case "PP":
			label.setIcon(new ImageIcon("src/Imagenes/BanderaPP.png"));
			break;
		case "PSOE":
			label.setIcon(new ImageIcon("src/Imagenes/BanderaPsoe.png"));
			break;
		case "VOX":
			label.setIcon(new ImageIcon("src/Imagenes/BanderaVox.png"));
			break;
		case "SALF":
			label.setIcon(new ImageIcon("src/Imagenes/BanderaSALF.png"));
			break;
		}
	}

	private JLabel obtenerLabelPorNombre(String nombre) {

		switch (nombre) {
		case "Andalucia":
			return vista.lblAndalucia;
		case "Aragon":
			return vista.lblAragon;
		case "Asturias":
			return vista.lblAsturias;
		case "Baleares":
			return vista.lblBaleares;
		case "Canarias":
			return vista.lblCanarias;
		case "Cantabria":
			return vista.lblCantabria;
		case "Castilla La Mancha":
			return vista.lblCastillaLaMancha;
		case "Castilla y Leon":
			return vista.lblCastillaLeon;
		case "Catalunia":
			return vista.lblCataluna;
		case "Comunidad Valenciana":
			return vista.lblComunidadValenciana;
		case "Extremadura":
			return vista.lblExtremadura;
		case "Galicia":
			return vista.lblGalicia;
		case "Madrid":
			return vista.lblMadrid;
		case "Murcia":
			return vista.lblMurcia;
		case "Navarra":
			return vista.lblNavarra;
		case "Pais Vasco":
			return vista.lblPaisVasco;
		case "La Rioja":
			return vista.lblLaRioja;
		case "Ceuta":
			return vista.lblCeuta;
		case "Melilla":
			return vista.lblMelilla;
		}

		return null;
	}

	private void pintarGanadorEspana(String partido) {

		switch (partido) {
		case "PP":
			vista.lblBanderaEspana.setIcon(new ImageIcon("src/Imagenes/BanderaPP.png"));
			break;
		case "PSOE":
			vista.lblBanderaEspana.setIcon(new ImageIcon("src/Imagenes/BanderaPsoe.png"));
			break;
		case "VOX":
			vista.lblBanderaEspana.setIcon(new ImageIcon("src/Imagenes/BanderaVox.png"));
			break;
		case "SALF":
			vista.lblBanderaEspana.setIcon(new ImageIcon("src/Imagenes/BanderaSALF.png"));
			break;
		}
	}

	private void filtrarPorPartido(String partido) {

		DefaultListModel<Comunidad> modelo = new DefaultListModel<>();

		if (partido.equals("Mostrar todas")) {

			for (Comunidad c : listaOriginalComunidades) {
				modelo.addElement(c);
			}

		} else {

			for (Comunidad c : listaOriginalComunidades) {
				if (c.getPartidoGanador() != null && c.getPartidoGanador().equals(partido)) {

					modelo.addElement(c);
				}
			}
		}

		vista.listComunidades.setModel(modelo);
	}

	private void mostrarGraficoComunidad(String nombreComunidad, String rangoEdad) {

		try {

			Connection con = bd.createConnection();

			Map<String, Integer> datos = bd.obtenerVotosPorComunidadYRango(con, nombreComunidad, rangoEdad);

			actualizarListaDetalle(datos);

			bd.disconnect(con);

			DefaultPieDataset dataset = new DefaultPieDataset();

			for (Map.Entry<String, Integer> entry : datos.entrySet()) {
				dataset.setValue(entry.getKey(), entry.getValue());
			}

			JFreeChart chart = ChartFactory.createPieChart("Resultados en " + nombreComunidad, dataset, true, true,
					false);

			PiePlot plot = (PiePlot) chart.getPlot();

			plot.setSectionPaint("PP", Color.BLUE);
			plot.setSectionPaint("PSOE", Color.RED);
			plot.setSectionPaint("VOX", Color.GREEN);
			plot.setSectionPaint("SALF", Color.ORANGE);

			if (chartPanelActual != null) {
				vista.PanelDetalle.remove(chartPanelActual);
			}

			chartPanelActual = new ChartPanel(chart);
			chartPanelActual.setBounds(100, 150, 400, 300);

			vista.PanelDetalle.add(chartPanelActual);
			vista.PanelDetalle.revalidate();
			vista.PanelDetalle.repaint();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actualizarListaDetalle(Map<String, Integer> datos) {

		DefaultListModel<String> modelo = new DefaultListModel<>();

		int totalVotos = 0;

		for (int votos : datos.values()) {
			totalVotos += votos;
		}

		for (Map.Entry<String, Integer> entry : datos.entrySet()) {

			String partido = entry.getKey();
			int votos = entry.getValue();

			double porcentaje = 0;

			if (totalVotos > 0) {
				porcentaje = (votos * 100.0) / totalVotos;
			}

			modelo.addElement(partido + " - " + votos + " votos (" + String.format("%.2f", porcentaje) + "%)");
		}

		vista.listDetalle.setModel(modelo);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}