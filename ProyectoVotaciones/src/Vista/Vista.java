package Vista;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JProgressBar;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;

public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JLabel lblMapa;
	public JList listComunidades;
	public JLabel lblCeuta;
	public JLabel lblMelilla;
	public JLabel lblCanarias;
	public JLabel lblBaleares; 
	public JLabel lblExtremadura; 
	public JLabel lblAndalucia; 
	public JLabel lblMurcia; 
	public JLabel lblLaRioja; 
	public JLabel lblComunidadValenciana; 
	public JLabel lblCastillaLaMancha; 
	public JLabel lblMadrid; 
	public JLabel lblCataluna; 
	public JLabel lblAragon; 
	public JLabel lblNavarra; 
	public JLabel lblPaisVasco; 
	public JLabel lblCantabria; 
	public JLabel lblCastillaLeon; 
	public JLabel lblAsturias; 
	public JLabel lblGalicia; 
	public JPanel PanelInicio; 
	public JPanel PanelMapa; 
	public JButton btnIniciarSimulacion; 
	public JButton btnCerrar; 
	public JLabel lblFotoHurna; 
	public JLabel lblFondoInicio; 
	public JLabel lblFondoMapa;
	public JPanel PanelDetalle;
	public JLabel lblEspana;
	public JLabel lblBanderaEspana;
	public JComboBox comboFiltroMapa;
	public JComboBox comboFiltroPersonas;
	public JList listDetalle;
	public JButton btnVolverDetalle;
	public JLabel lblNombre;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Vista vista = new Vista();
					new Controlador.Controlador(vista);
					vista.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Vista() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 638);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		PanelInicio = new JPanel();
		PanelInicio.setBounds(0, 0, 986, 602);
		contentPane.add(PanelInicio);
		PanelInicio.setLayout(null);
		
		btnIniciarSimulacion = new JButton("INICIAR SIMULACION");
		btnIniciarSimulacion.setBounds(258, 330, 516, 89);
		PanelInicio.add(btnIniciarSimulacion);
		
		btnCerrar = new JButton("SALIR");
		btnCerrar.setBounds(384, 441, 259, 41);
		PanelInicio.add(btnCerrar);
		
		lblFotoHurna = new JLabel(new ImageIcon("src/Imagenes/FotoHurna.png"));
		lblFotoHurna.setBounds(353, 60, 341, 259);
		PanelInicio.add(lblFotoHurna);
		
		lblFondoInicio = new JLabel(new ImageIcon("src/Imagenes/FondoInicio.png"));
		lblFondoInicio.setBounds(-18, -26, 1018, 628);
		PanelInicio.add(lblFondoInicio);
		
		PanelMapa = new JPanel();
		PanelMapa.setVisible(false);
		
		PanelDetalle = new JPanel() {

		    private Image fondo = new ImageIcon(getClass().getResource("/Imagenes/FondoPaginaDetalle.png")).getImage();

		    @Override
		    protected void paintComponent(java.awt.Graphics g) {
		        super.paintComponent(g);
		        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
		    }
		};
		
				PanelDetalle.setBounds(0, 0, 986, 602);
				contentPane.add(PanelDetalle);
				PanelDetalle.setLayout(null);
				
						
						lblNombre = new JLabel("NombreComunidadAutonoma");
						lblNombre.setFont(new Font("Stencil", Font.BOLD, 26));
						lblNombre.setBounds(99, 25, 488, 111);
						PanelDetalle.add(lblNombre);
						
						btnVolverDetalle = new JButton("Volver");
						btnVolverDetalle.setBounds(26, 550, 145, 42);
						PanelDetalle.add(btnVolverDetalle);
						
						listDetalle = new JList();
						listDetalle.setBounds(586, 170, 352, 309);
						PanelDetalle.add(listDetalle);
						
						comboFiltroPersonas = new JComboBox();
						comboFiltroPersonas.setBounds(586, 112, 352, 33);
						PanelDetalle.add(comboFiltroPersonas);
		PanelMapa.setBounds(0, 0, 986, 602);
		contentPane.add(PanelMapa);
		PanelMapa.setLayout(null);
		
		comboFiltroMapa = new JComboBox();
		comboFiltroMapa.setBounds(612, 33, 352, 25);
		PanelMapa.add(comboFiltroMapa);
		
		lblBanderaEspana = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblBanderaEspana.setBounds(528, 83, 62, 58);
		PanelMapa.add(lblBanderaEspana);
		
		lblEspana = new JLabel(new ImageIcon("src/Imagenes/LogoEspana.png"));
		lblEspana.setBounds(465, 67, 100, 100);
		PanelMapa.add(lblEspana);
		
		listComunidades = new JList();
		listComunidades.setBounds(616, 75, 348, 493);
		PanelMapa.add(listComunidades);
		
		lblMelilla = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblMelilla.setBounds(274, 534, 62, 58);
		PanelMapa.add(lblMelilla);
		
		lblCeuta = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblCeuta.setBounds(172, 510, 62, 58);
		PanelMapa.add(lblCeuta);
		
		lblCanarias = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblCanarias.setBounds(462, 450, 62, 58);
		PanelMapa.add(lblCanarias);
		
		lblBaleares = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblBaleares.setBounds(528, 294, 62, 58);
		PanelMapa.add(lblBaleares);
		
		lblExtremadura = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblExtremadura.setBounds(144, 315, 62, 58);
		PanelMapa.add(lblExtremadura);
		
		lblAndalucia = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblAndalucia.setBounds(222, 408, 62, 58);
		PanelMapa.add(lblAndalucia);
		
		lblMurcia = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblMurcia.setBounds(348, 383, 62, 58);
		PanelMapa.add(lblMurcia);
		
		lblLaRioja = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblLaRioja.setBounds(307, 141, 62, 58);
		PanelMapa.add(lblLaRioja);
		
		lblComunidadValenciana = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblComunidadValenciana.setBounds(382, 315, 62, 58);
		PanelMapa.add(lblComunidadValenciana);
		
		lblCastillaLaMancha = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblCastillaLaMancha.setBounds(285, 315, 62, 58);
		PanelMapa.add(lblCastillaLaMancha);
		
		lblMadrid = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblMadrid.setBounds(249, 241, 62, 58);
		PanelMapa.add(lblMadrid);
		
		lblCataluna = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblCataluna.setBounds(462, 177, 62, 58);
		PanelMapa.add(lblCataluna);
		
		lblAragon = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblAragon.setBounds(371, 189, 62, 58);
		PanelMapa.add(lblAragon);
		
		lblNavarra = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblNavarra.setBounds(348, 110, 62, 58);
		PanelMapa.add(lblNavarra);
		
		lblPaisVasco = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblPaisVasco.setBounds(295, 93, 62, 58);
		PanelMapa.add(lblPaisVasco);
		
		lblCantabria = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblCantabria.setBounds(236, 83, 62, 58);
		PanelMapa.add(lblCantabria);
		
		lblCastillaLeon = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblCastillaLeon.setBounds(196, 189, 62, 58);
		PanelMapa.add(lblCastillaLeon);
		
		lblAsturias = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblAsturias.setBounds(161, 83, 62, 58);
		PanelMapa.add(lblAsturias);
		
		lblGalicia = new JLabel(new ImageIcon("src/Imagenes/Bandera.png"));
		lblGalicia.setBounds(75, 124, 62, 58);
		PanelMapa.add(lblGalicia);
		
		lblMapa = new JLabel(new ImageIcon("src/Imagenes/Mapa.png"));
		lblMapa.setBounds(33, 78, 573, 514);
		PanelMapa.add(lblMapa);
		
		lblFondoMapa = new JLabel(new ImageIcon("src/Imagenes/FondoMapa.png"));
		lblFondoMapa.setBounds(0, 0, 986, 602);
		PanelMapa.add(lblFondoMapa);
	}
}