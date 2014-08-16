import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JTextField;


public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField tfGridWidthSmall;
	private JTextField tfGridWidthBig;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
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
	public MainWindow() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		contentPane.setLayout(gbl_contentPane);
		
		JPanel pnlGrid = new JPanel();
		GridBagConstraints gbc_pnlGrid = new GridBagConstraints();
		gbc_pnlGrid.insets = new Insets(0, 0, 5, 5);
		gbc_pnlGrid.fill = GridBagConstraints.BOTH;
		gbc_pnlGrid.gridx = 0;
		gbc_pnlGrid.gridy = 0;
		contentPane.add(pnlGrid, gbc_pnlGrid);
		GridBagLayout gbl_pnlGrid = new GridBagLayout();
		pnlGrid.setLayout(gbl_pnlGrid);
		
		JLabel lblNewLabel_1 = new JLabel("Grid settings:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		pnlGrid.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		tfGridWidthSmall = new JTextField();
		GridBagConstraints gbc_tfGridWidthSmall = new GridBagConstraints();
		gbc_tfGridWidthSmall.insets = new Insets(0, 0, 5, 0);
		gbc_tfGridWidthSmall.gridx = 0;
		gbc_tfGridWidthSmall.gridy = 1;
		pnlGrid.add(tfGridWidthSmall, gbc_tfGridWidthSmall);
		tfGridWidthSmall.setColumns(10);
		
		tfGridWidthBig = new JTextField();
		GridBagConstraints gbc_tfGridWidthBig = new GridBagConstraints();
		gbc_tfGridWidthBig.insets = new Insets(0, 0, 5, 0);
		gbc_tfGridWidthBig.gridx = 1;
		gbc_tfGridWidthBig.gridy = 1;
		pnlGrid.add(tfGridWidthBig, gbc_tfGridWidthBig);
		tfGridWidthBig.setColumns(10);
		
		JPanel pnlActions = new JPanel();
		GridBagConstraints gbc_pnlActions = new GridBagConstraints();
		gbc_pnlActions.insets = new Insets(0, 0, 5, 5);
		gbc_pnlActions.gridx = 0;
		gbc_pnlActions.gridy = 1;
		contentPane.add(pnlActions, gbc_pnlActions);
		GridBagLayout gbl_pnlActions = new GridBagLayout();
		pnlActions.setLayout(gbl_pnlActions);
		
		JButton btnRender = new JButton("Render to SVG");
		btnRender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TranscoderDemo.writeSVG(getData());
			}
		});
		
		JLabel lblNewLabel = new JLabel("Actions:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		pnlActions.add(lblNewLabel, gbc_lblNewLabel);
		GridBagConstraints gbc_btnRender = new GridBagConstraints();
		gbc_btnRender.insets = new Insets(0, 0, 0, 5);
		gbc_btnRender.gridx = 0;
		gbc_btnRender.gridy = 1;
		pnlActions.add(btnRender, gbc_btnRender);
		
		JButton btnQuit = new JButton("QUIT");
		GridBagConstraints gbc_btnQuit = new GridBagConstraints();
		gbc_btnQuit.insets = new Insets(0, 0, 0, 5);
		gbc_btnQuit.gridx = 1;
		gbc_btnQuit.gridy = 1;
		pnlActions.add(btnQuit, gbc_btnQuit);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainWindow.this.dispose();
			}
		});


		// init data
		setData(new Project());
	}

	/**
	 * move project data to fields in view
	 * @param project the project
	 */
	private void setData(Project project)
	{
		tfGridWidthBig.setText(String.valueOf(project.getGridWidthBig()));
		tfGridWidthSmall.setText(String.valueOf(project.getGridWidthSmall()));
		// TODO variables missing
	}

	/**
	 * get project data from fields in view
	 * @returns the project data
	 */
	private Project getData()
	{
		Project project = new Project();
		// TODO variables missing, too much default configuration
		
		try
		{
			project.setGridWidthBig(Float.parseFloat(tfGridWidthBig.getText()));
		}
		catch (NumberFormatException e)
		{
			// TODO display error instead of silently keeping the default value
		}
		
		try
		{
			project.setGridWidthSmall(Float.parseFloat(tfGridWidthSmall.getText()));
		}
		catch (NumberFormatException e)
		{
			// TODO display error instead of silently keeping the default value
		}
		
		return project;
	}

}
