/*Name: Samuel Jothimuthu
 * Course: CS170-03
 * Assignment: Project
 * This is the operation class Table. It generates a table for the top 5 scores.
 * It is called in MathGame.java. Unique features are that it adjusts the row height
 * to include duplicate scores. 
 * 
 */
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

	public class Table extends JFrame
		{
			/*
			 * This is the constructor. Takes 3 parameters.
			 * 1st Parameter: 2-dimensional array that holds 3 values: rank,score, and names; 
			 * in that order. It takes the type of Object (I pass in String).
			 * 
			 * 2nd Parameter: Array that holds the column names. It takes the type of
			 * Object (I pass in String).
			 * 
			 * 3rd Parameter: Array of integers that holds the row sizes. Each individual row has 
			 * a different size so I need an array to store which size each row should be.
			 * The sizes are determined in MathGame.java. 
			 */
			public Table(Object[][] data, Object[] colNames,int[] rowSize)
			{
				//Vaiables for JFrame
				int height = 350;
				int width = 350;
				
				setSize(width,height);
				
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				//Declare table
				JTable table = new JTable(data, colNames);
				table.setEnabled(false); //No Editing 
				/*
				 * This is what sets the row height for each individual row
				 */
				for(int row = 0; row < 5; row++)
				{
						table.setRowHeight(row, rowSize[row]);
				}
				JScrollPane scroll = new JScrollPane(table);
				
				
				add(scroll);
				setLocationRelativeTo(null); //Forces window to appear in the center of the screen.
				setVisible(true);
				
			}			
}
