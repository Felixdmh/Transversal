package Controlador;

import java.sql.Connection;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;

import Vista.Vista;
import persistencias.Comunidad;
import Controlador.BaseDeDatos;

public class Controlador {

    private final Vista vista;
    private final BaseDeDatos bd;

    public Controlador(Vista vista) {
        this.vista = vista;
        this.bd = new BaseDeDatos();

        configurarEventos();
        cargarComunidadesAsync();
    }

    private void configurarEventos() {
        vista.btnCerrar.addActionListener(e -> System.exit(0));

        vista.btnIniciarSimulacion.addActionListener(e -> {
            System.out.println("Simulación pendiente...");
        });

        if (vista.btnVolver != null) {
            vista.btnVolver.addActionListener(e -> {
                System.out.println("Volver (pendiente)...");
            });
        }
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
}
