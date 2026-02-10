package Vista;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Vista frame = new Vista();
					frame.setVisible(true);
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
		setBounds(100, 100, 1002, 641);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel PanelMapa = new JPanel();
		PanelMapa.setBounds(0, 0, 986, 602);
		contentPane.add(PanelMapa);
		PanelMapa.setLayout(null);
		
		JLabel lblFondoMapa = new JLabel("New label");
		lblFondoMapa.setBounds(0, 0, 986, 602);
		PanelMapa.add(lblFondoMapa);
		
		JPanel PanelInicio = new JPanel();
		PanelInicio.setVisible(false);
		PanelInicio.setBounds(0, 0, 986, 602);
		contentPane.add(PanelInicio);
		PanelInicio.setLayout(null);
		
		JButton btnIniciarSimulacion = new JButton("INICIAR SIMULACION");
		btnIniciarSimulacion.setBounds(258, 330, 516, 89);
		PanelInicio.add(btnIniciarSimulacion);
		
		JButton btnCerrar = new JButton("SALIR");
		btnCerrar.setBounds(384, 441, 259, 41);
		PanelInicio.add(btnCerrar);
		
		JLabel lblFotoHurna = new JLabel(new ImageIcon("src/Imagenes/FotoHurna.png"));
		lblFotoHurna.setBounds(353, 60, 341, 259);
		PanelInicio.add(lblFotoHurna);
		
		JLabel lblFondoInicio = new JLabel(new ImageIcon("src/Imagenes/FondoInicio.png"));
		lblFondoInicio.setBounds(-32, -26, 1018, 628);
		PanelInicio.add(lblFondoInicio);
	}
}
