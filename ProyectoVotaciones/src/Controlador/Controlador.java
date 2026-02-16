package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Vista.Vista;
import persistencias.Comunidad;

public class Controlador implements MouseListener, ActionListener, ListSelectionListener {

    private final Vista vista;
    private final BaseDeDatos bd;

    // Guardamos la comunidad seleccionada para usarla en PanelDetalle
    private Comunidad comunidadSeleccionada;

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

        // IMPORTANTE: para combos usamos ActionListener también
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
        // Puedes cambiar nombres luego, pero esto te da una base clara
        DefaultComboBoxModel<String> modelMapa = new DefaultComboBoxModel<>();
        modelMapa.addElement("Selecciona filtro del mapa");
        modelMapa.addElement("Ganador por comunidad");
        modelMapa.addElement("Votos totales por comunidad");
        modelMapa.addElement("Participación (simulada)");
        modelMapa.addElement("Abstención (simulada)");
        vista.comboFiltroMapa.setModel(modelMapa);
        vista.comboFiltroMapa.setSelectedIndex(0);

        // ---- comboFiltroPersonas (detalle) ----
        DefaultComboBoxModel<String> modelPersonas = new DefaultComboBoxModel<>();
        modelPersonas.addElement("Selecciona rango de edad");
        modelPersonas.addElement("1-9");
        modelPersonas.addElement("10-17");
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
                    if (con != null) con.rollback();
                    e.printStackTrace();
                    throw e;

                } finally {
                    if (con != null) bd.disconnect(con);
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

        // ---- INICIAR SIMULACIÓN
        if (e.getSource() == vista.btnIniciarSimulacion) {
            vista.PanelInicio.setVisible(false);
            vista.PanelDetalle.setVisible(false);
            vista.PanelMapa.setVisible(true);
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
            if (opcion == null) return;

            System.out.println("Filtro mapa seleccionado: " + opcion);
            return;
        }

        // ---- CAMBIO EN comboFiltroPersonas ----
        if (e.getSource() == vista.comboFiltroPersonas) {
            String rango = (String) vista.comboFiltroPersonas.getSelectedItem();
            if (rango == null) return;


            if (comunidadSeleccionada == null) return;

            System.out.println("Rango seleccionado (" + comunidadSeleccionada.getNombreComunidad() + "): " + rango);
            return;
        }
    }

    // CLICK EN LISTA DE CCAA -> IR A DETALLE
    @Override
    public void valueChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting()) return;

        if (e.getSource() == vista.listComunidades) {
            Comunidad seleccionada = (Comunidad) vista.listComunidades.getSelectedValue();
            if (seleccionada == null) return;

            this.comunidadSeleccionada = seleccionada;

            // Cambiar panel
            vista.PanelMapa.setVisible(false);
            vista.PanelDetalle.setVisible(true);

            // Poner nombre
            vista.lblNombre.setText(seleccionada.getNombreComunidad());

            // Resetear el combo de personas al entrar
            vista.comboFiltroPersonas.setSelectedIndex(0);

            if (vista.listDetalle != null) {
                vista.listDetalle.setModel(new DefaultListModel<>());
            }
        }
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}