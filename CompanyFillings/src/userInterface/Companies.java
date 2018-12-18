package userInterface;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import dbConnection.*;
import findCompanyFillings.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

public class Companies extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Companies frame = new Companies();
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
	public Companies() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		List<CompaniesList> companiesList = Arrays.asList(CompaniesList.values());
		JList companies = new JList(companiesList.toArray());
		panel.add(companies, BorderLayout.CENTER);
		panel.add(new JScrollPane(companies));
		
		JTextPane textPane = new JTextPane();
		splitPane.setRightComponent(new JScrollPane(textPane));
		
		
		JButton GetValues = new JButton("Afiseaza Valori");
		GetValues.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = companies.getSelectedIndex();
				
				String companyName = companiesList.get(index).toString();
				System.out.println(companiesList.get(index).toString());
				String activityName = new String();
				long activityValue;
				Hashtable<String, Long> nameAndValue = new Hashtable<String,Long>();
				OpenURLStream urlStream = new OpenURLStream(companyName);
				nameAndValue = urlStream.consolidatedBalanceSheetData();
				FinancialActivityDAO objectConnection = new FinancialActivityDAO();
				
				int year = urlStream.getDocumentYear();
				
				//System.out.println(nameAndValue.keySet());
				Set<String> keys = nameAndValue.keySet();
				for(String s : keys) {
					activityName = s;
					activityValue = nameAndValue.get(s);
					FinancialActivity financialActivity = new FinancialActivity(activityName, activityValue, year, companyName);
					objectConnection.save(financialActivity, "consolidated_balance_sheets");
				}
				
				System.out.println(urlStream.getCompanyInformation());
				textPane.setText(urlStream.getCompanyInformation());
			}
		});
		panel.add(GetValues, BorderLayout.NORTH);
		
	}

}
