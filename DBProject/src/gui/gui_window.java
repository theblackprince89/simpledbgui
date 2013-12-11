package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

import java.sql.ResultSetMetaData;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import java.awt.Color;

import javax.swing.ImageIcon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class gui_window {

	private JFrame frmSimpleDatabaseManipulator;
	private JTextPane queryPane;
	private JTextPane resultPane;
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private Font font;
	private Font fontError;
	private JButton runSQLQuery;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					gui_window window = new gui_window();
					window.frmSimpleDatabaseManipulator.setVisible(true);
					window.initDatabaseConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gui_window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSimpleDatabaseManipulator = new JFrame();
		frmSimpleDatabaseManipulator.setResizable(false);
		frmSimpleDatabaseManipulator.setTitle("Simple Database Manipulator");
		frmSimpleDatabaseManipulator.setBounds(100, 100, 1026, 523);
		frmSimpleDatabaseManipulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setForeground(Color.WHITE);
		panel.setBackground(Color.BLACK);
		frmSimpleDatabaseManipulator.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Results");
		lblNewLabel.setForeground(Color.WHITE);
		
		queryPane = new JTextPane();
		queryPane.setText("select * from employee_address");
		
		JLabel lblNewLabel_1 = new JLabel("Query Editor");
		lblNewLabel_1.setForeground(Color.WHITE);
		
		runSQLQuery = new JButton("");
		
		
		runSQLQuery.setIcon(new ImageIcon(gui_window.class.getResource("/img/try_icon.png")));
		runSQLQuery.setPressedIcon(new ImageIcon(gui_window.class.getResource("/img/try_icon_pressed.png")));
		runSQLQuery.setBounds(new Rectangle(0, 0, 19, 19));
		runSQLQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				parseSQLQuery();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(20)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 972, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(queryPane, GroupLayout.PREFERRED_SIZE, 891, GroupLayout.PREFERRED_SIZE)
									.addGap(26)
									.addComponent(runSQLQuery, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblNewLabel_1))))
					.addContainerGap(28, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(12)
					.addComponent(lblNewLabel)
					.addGap(8)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 337, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(runSQLQuery, 0, 0, Short.MAX_VALUE)
						.addComponent(queryPane, GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE))
					.addContainerGap(39, Short.MAX_VALUE))
		);
		
		resultPane = new JTextPane();
		scrollPane.setViewportView(resultPane);
		resultPane.setEditable(false);
		panel.setLayout(gl_panel);
		
		
		
		
	}
	
	public void initDatabaseConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			JOptionPane.showMessageDialog(frmSimpleDatabaseManipulator, "Loaded driver successfully", "SUCCESS: database driver loaded", JOptionPane.INFORMATION_MESSAGE);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(frmSimpleDatabaseManipulator, "Could not load driver\n\n" , "ERROR: Could not connect to database", JOptionPane.OK_OPTION);
			e1.printStackTrace();
			System.out.println("Could not load database driver.");
			
		}
	      // Setup the connection with the DB
	      try {
			connection = DriverManager
			      .getConnection("jdbc:mysql://localhost/mydb?"
			          + "user=root&password=password");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getSQLCommand(){
		String q = queryPane.getText();
		queryPane.setText("");
		return q;
	}
	
	public void parseSQLQuery(){
		try {
			statement = connection.createStatement();
			String queryString = getSQLCommand();
			if(queryString.startsWith("select")){
			resultSet = statement.executeQuery(queryString);
			}
			else
			{
				resultPane.setText("");
				statement.execute(queryString);
				System.out.println("data modified. ");
				resultPane.setText("data modified." + statement.getUpdateCount() + " rows affected.");
				return;
			}
			resultSet.last();
			int count = resultSet.getRow();
			System.out.println("query string = " + queryString + " result count = " + count);
			if(count<1){
				resultPane.setText("No records to display");
			}
			resultSet.beforeFirst();
			StringBuilder r = new StringBuilder();
			ResultSetMetaData metadata = resultSet.getMetaData();
			int numColumns = metadata.getColumnCount();
			for(int i = 1; i<=numColumns; i++){
				r.append(metadata.getColumnName(i)).append("\t").append("\t");
				
			}
			r.append("\n\n");
			
			while(!resultSet.isLast()){
				resultSet.next();
				for(int i = 1; i<=numColumns; i++){
					r.append(resultSet.getString(i)).append("\t").append("\t");
				}
				r.append("\n");
				
			}
			resultPane.setText(r.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultPane.setText("SQL ERROR : " + e.getErrorCode());
		}
		
		 
	}
}
