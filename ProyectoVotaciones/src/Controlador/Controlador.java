package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Vista.Vista;
import persistencias.Comunidad;
import Controlador.BaseDeDatos;

public class Controlador implements MouseListener, ActionListener, ListSelectionListener{

    private final Vista vista;
    private final BaseDeDatos bd;

    public Controlador(Vista vista){    	
        this.vista = vista;
        this.bd = new BaseDeDatos();

        configurarEventos();
        cargarComunidadesAsync();
        
    	this.vista.PanelMapa.setVisible(false);
    	this.vista.PanelDetalle.setVisible(false);
        
        this.vista.btnIniciarSimulacion.addActionListener(this);
        this.vista.btnVolverDetalle.addActionListener(this);
        this.vista.listComunidades.addListSelectionListener(this);

    }

    private void configurarEventos() {
        vista.btnCerrar.addActionListener(e -> System.exit(0));

        vista.btnIniciarSimulacion.addActionListener(e -> {
            System.out.println("Simulación pendiente...");
        });

        if (vista.btnVolverDetalle != null) {
            vista.btnVolverDetalle.addActionListener(e -> {
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

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == vista.btnIniciarSimulacion) {
			vista.PanelInicio.setVisible(false);
			vista.PanelMapa.setVisible(true);
		}
		
		if(e.getSource() == vista.btnVolverDetalle) {
			vista.PanelDetalle.setVisible(false);
			vista.PanelMapa.setVisible(true);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void valueChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting()) return;

        if (e.getSource() == vista.listComunidades) {

            Comunidad seleccionada = (Comunidad) vista.listComunidades.getSelectedValue();
            if (seleccionada == null) return;

            // 1) Cambiar al panel detalle
            vista.PanelDetalle.setVisible(true);
            vista.PanelMapa.setVisible(false);

            // 2) Poner nombre en lblNombre
            vista.lblNombre.setText(seleccionada.getNombreComunidad());
        }
    }
}