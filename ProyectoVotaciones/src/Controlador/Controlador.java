package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Vista.Vista;
import persistencias.Comunidad;

public class Controlador implements MouseListener, ActionListener, ListSelectionListener {

    private final Vista vista;
    private final BaseDeDatos bd;

    private Comunidad comunidadSeleccionada;

    public Controlador(Vista vista) {
        this.vista = vista;
        this.bd = new BaseDeDatos();

        // === Panel inicial al arrancar =
        this.vista.PanelInicio.setVisible(true);
        this.vista.PanelMapa.setVisible(false);
        this.vista.PanelDetalle.setVisible(false);

        // ===== Listeners ====
        this.vista.btnIniciarSimulacion.addActionListener(this);
        this.vista.btnVolverDetalle.addActionListener(this);
        this.vista.btnCerrar.addActionListener(this);

        this.vista.listComunidades.addListSelectionListener(this);

        this.vista.comboFiltroMapa.addActionListener(this);
        this.vista.comboFiltroPersonas.addActionListener(this);

        // == Cargar combos ====
        cargarFiltros();

        // === Configurar progress bar =====
        vista.progressBar.setMinimum(0);
        vista.progressBar.setMaximum(100);
        vista.progressBar.setValue(0);

        // ===== Cargar comunidades ====
        cargarComunidadesAsync();
    }


    private void cargarFiltros() {

        // comboFiltroMapa
        DefaultComboBoxModel<String> modelMapa = new DefaultComboBoxModel<>();
        modelMapa.addElement("Selecciona filtro del mapa");
        modelMapa.addElement("Ganador por comunidad");
        modelMapa.addElement("Votos totales por comunidad");
        modelMapa.addElement("Participación (simulada)");
        modelMapa.addElement("Abstención (simulada)");
        vista.comboFiltroMapa.setModel(modelMapa);
        vista.comboFiltroMapa.setSelectedIndex(0);

        // comboFiltroPersonas (rangos)
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


    private void actualizarDetallePorRango(String rangoSeleccionado) {

        if (comunidadSeleccionada == null) return;

        int porcentaje = obtenerPorcentajePorRango(comunidadSeleccionada, rangoSeleccionado);
        int total = comunidadSeleccionada.getTotalHabitantes();

        // Calculamos "personas aproximadas"
        int personasAprox = (int) Math.round(total * (porcentaje / 100.0));

        // progressBar
        vista.progressBar.setValue(porcentaje);
        vista.progressBar.setStringPainted(true);
        vista.progressBar.setString(porcentaje + "%");

        // listDetalleeeee 
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("Comunidad: " + comunidadSeleccionada.getNombreComunidad());
        model.addElement("Rango seleccionado: " + rangoSeleccionado);
        model.addElement("Porcentaje: " + porcentaje + "%");
        model.addElement("Personas aproximadas: " + personasAprox);
        model.addElement("Total habitantes: " + total);

        vista.listDetalle.setModel(model);
    }

    private int obtenerPorcentajePorRango(Comunidad c, String rango) {
        if (rango == null) return 0;

        switch (rango) {
            case "1-9":
                return c.getRango1_9();
            case "10-17":
                return c.getRango10_17();
            case "18-25":
                return c.getRango18_25();
            case "26-40":
                return c.getRango26_40();
            case "41-65":
                return c.getRango41_65();
            case "66+":
                return c.getRangoMas66();
            default:
                return 0; // "Selecciona rango de edad"
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnCerrar) {
            System.exit(0);
        }

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

        // comboFiltroMapa
        if (e.getSource() == vista.comboFiltroMapa) {
            String opcion = (String) vista.comboFiltroMapa.getSelectedItem();
            if (opcion != null) System.out.println("Filtro mapa seleccionado: " + opcion);
            return;
        }

        // comboFiltroPersonas = actualiza progressBar y listDetalle
        if (e.getSource() == vista.comboFiltroPersonas) {
            String rango = (String) vista.comboFiltroPersonas.getSelectedItem();
            if (rango == null) return;

            // Si está el texto "Selecciona rango" no hacemos nada
            if (rango.equals("Selecciona rango de edad")) {
                vista.progressBar.setValue(0);
                vista.progressBar.setStringPainted(false);
                vista.listDetalle.setModel(new DefaultListModel<>());
                return;
            }

            actualizarDetallePorRango(rango);
            return;
        }
    }

    // =========================
    // CLICK EN LISTA DE CCAA -> IR A DETALLE
    // =========================
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

            // Nombre
            vista.lblNombre.setText(seleccionada.getNombreComunidad());

            // Al entrar en detalle, ponemos un rango por defecto (por ejemplo 1-9)
            vista.comboFiltroPersonas.setSelectedItem("1-9");
            actualizarDetallePorRango("1-9");
        }
    }


    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}