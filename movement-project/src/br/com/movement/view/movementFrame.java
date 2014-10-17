package br.com.movement.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import br.com.movement.entity.Movement;
import br.com.movement.service.MovementService;

public class movementFrame extends JFrame {

	/**
	 * Constante para serializa��o.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Inst�ncia principal do servi�o de Movimenta��o.	 
	 */
	MovementService movementService = new MovementService();	
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					movementFrame frame = new movementFrame();
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
	public movementFrame() {
		setForeground(Color.LIGHT_GRAY);
		setTitle("Movement Project v1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException
				| InstantiationException | IllegalAccessException e) {
			System.out.println("[Visualiza��o] N�o foi poss�vel carrega a skin Nimbus.");
		}

		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Panel.background"));
		contentPane.add(panel, BorderLayout.CENTER);
		
		JButton brBetterSubsidiaryProfit = new JButton("Filial com maior lucro no per�odo");
		brBetterSubsidiaryProfit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				// Chama o servi�o.
				Movement movement = movementService.subsidiaryWhoSoldMoreOnPeriod();
				JOptionPane.showMessageDialog(null, "Nome: ".concat(movement.getSubsidiaryName().concat("\nValor total em vendas: " 
						+ NumberFormat.getCurrencyInstance().format(movement.getMovementValue()))),"Filial com maior lucro no per�odo", 1);
			}
			
		});
		
		JButton btHigherGrowth = new JButton("Filial com o maior crescimento no per�odo");
		btHigherGrowth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
				decimalFormatSymbols.setDecimalSeparator(',');
				decimalFormatSymbols.setGroupingSeparator('.');
				DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);
				
				// Chama o servi�o.
				Movement movement = movementService.subsidiaryWithHigherLowerGrowth("Janeiro", "Mar�o", 'h');
				JOptionPane.showMessageDialog(null, "Nome: ".concat(movement.getSubsidiaryName().concat("\nPorcentual de crescimento: " 
						+ decimalFormat.format(movement.getPercentOfGrowthRate()).concat("%"))),"Filial com maior crescimento no per�odo", 1);

			}
		});
		
		JButton btLowerGrowth = new JButton("Filial com o menor crescimento no per�odo");
		btLowerGrowth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
				decimalFormatSymbols.setDecimalSeparator(',');
				decimalFormatSymbols.setGroupingSeparator('.');
				DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);
				
				// Chama o servi�o.
				Movement movement = movementService.subsidiaryWithHigherLowerGrowth("Janeiro", "Mar�o", 'l');
				JOptionPane.showMessageDialog(null, "Nome: ".concat(movement.getSubsidiaryName().concat("\nPorcentual de crescimento: " 
						+ decimalFormat.format(movement.getPercentOfGrowthRate()).concat("%"))),"Filial com o menor crescimento no per�odo", 1);
			}
		});
		
		JButton btMonthWhenEntrepriseHaveMoreProfit = new JButton("M�s em que a empresa mais teve lucro");
		btMonthWhenEntrepriseHaveMoreProfit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				// Chama o servi�o.
				JOptionPane.showMessageDialog(null, "M�s: ".concat(movementService.monthWhenEntrepriseHaveMoreProfit()), "M�s em que a empresa mais vendeu", 1);
			}
		});
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblMovementProject = new JLabel("Movement Project");
		lblMovementProject.setHorizontalAlignment(SwingConstants.CENTER);
		lblMovementProject.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMovementProject.setForeground(Color.BLACK);
		panel.add(lblMovementProject);
		
		panel.add(brBetterSubsidiaryProfit);
		panel.add(btHigherGrowth);
		panel.add(btLowerGrowth);
		panel.add(btMonthWhenEntrepriseHaveMoreProfit);
				
	}

}
