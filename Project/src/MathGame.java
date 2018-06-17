/*Name: Samuel Jothimuthu
 * Course: CS170-03
 * Assignment: Project
 * This is the operation class MathGame. This class has all of the code for the initial
 * panel and problem panel. It uses data structures to store the scores of the Game and also
 * helps generate the data for the table. It also creates the problems that are displayed. 
 * 
 * NOTE: change to 60 seconds, Remove Console Prints, 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.lang.Object; 
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class MathGame extends JFrame {
		
		/*
		 * Misc. Variables 
		 */
		String problem,name;
		
		int num1, num2, ans, entry;
		static int right, wrong;
		static int correct, incorrect;
		static int total;  
		static int time = 60;
		//Creates the random numbers for the other answers (in a range)
		int wrongAns1 = ThreadLocalRandom.current().nextInt(1,11);
		int wrongAns2 = ThreadLocalRandom.current().nextInt(10,21);
		int wrongAns3 = ThreadLocalRandom.current().nextInt(1,21);
		static boolean timesUP = false;
		static String filepath; //Files
		File f = new File("."); // Current Directory
		FileFilter directoryFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};
		
		/*
		 * This takes the answer choices and turns them into strings.
		 */
		String ch1 = Integer.toString(ans);
		String ch2 = Integer.toString(wrongAns1);
		String ch3 = Integer.toString(wrongAns2); 
		String ch4 = Integer.toString(wrongAns3);
		String correctAnswer, wrongAnswer;
		
		/*
		 * These are the arrays for the Table.
		 */
		private static final String[] colNames = {"Rank", "Score (# correct)", "Names"}; //Column Names
		private static String[][] data = new String[5][3]; // stores the rank, scores, and name
		static int[] rowSize = new int[5]; // stores the row height for each row
		
		/*
		 * Data Structures
		 */
		ArrayList<String> answers = new ArrayList<String>(4); //Used for shuffling the answers for JFrame. see Answers()
		
		static Map<Integer, ArrayList<String>> topScores =  //Key is score; Value is Arraylist of Names
				new TreeMap<Integer,ArrayList<String>>(Collections.reverseOrder()); //Used for Table
		
		static Map<String, ArrayList<Integer>> userNames = //Key is Name; Value is ArrayList of Scores
				new TreeMap<String,ArrayList<Integer>>(); //Used for Search Function
		
		/*
		 * Variables for the GUI
		 */
		private JLabel messageLabel = new JLabel("Math Game");
		private JLabel Problem;
		private JButton Choice1;
		private JButton Choice2;
		private JButton Choice3;
		private JButton Choice4;
		private JLabel startLabel;
		private JButton Start = new JButton("Start");
		private JButton Exit = new JButton("Exit");
		private JButton Scores = new JButton("Top 5 Scores");
		private JButton searchScores = new JButton("Search Scores");
		private JPanel northpanel;
		private JPanel southpanel;
		private JPanel centerpanel;
		private JPanel mainpanel;
		private final int  WINDOW_HEIGHT = 150;
		private final int  WINDOW_WIDTH = 350;
		
		
		/*
		 * No Arg Constructor: That shows the main panel (Start Buttons, Table, Search, Exit)
		 */
		MathGame() throws IOException {
			super("Math Game");
			mainpanel();
			readScores();
		}
		/*
		 * 1 Arg Constructor: That is called when start button is pressed. Countdown() is called.  
		 */
		MathGame(int i){
			super("Math Game");
			buildpanel();
			Countdown();
		}
		/*
		 * 2 Arg Constructor: That is called after a new problem is answered. 
		 */
		MathGame(int i, int j){
			super("Math Game");
			buildpanel();
		}
		
		/*
		 * Creates the problem and answer variables
		 */
		public void Problem() {
			num1 = ThreadLocalRandom.current().nextInt(1,11);
			num2 = ThreadLocalRandom.current().nextInt(1,11);
			ans = num1 + num2;
		}
		
		/*
		 * Calls Problem() and initializes problem(String) 
		 */
		public void createQuestion() {
			Problem();
			problem = num1 + " + " + num2 + " = ";
		}	
		
		/* 
		 * Similar to Problem() and initializes ch1(String) & correctAnwser(String) 
		 */
		public void Question() {
			Problem();
			problem = num1 + " + " + num2 + " = ";
			ch1 = correctAnswer = Integer.toString(ans);
			
		}
		
		/*
		 * This function does 3 things
		 * 1) It makes sure no duplicate answers are created.
		 * 2) It adds the values to the Arraylist
		 * 3) Shuffles them in a random order so they are not 
		 * displayed in the same order each time
		 */
		public void Answers()
		{
			Boolean duplicate = true;
			
			do {
			if(ans == wrongAns1)
			{
				wrongAns1 = ThreadLocalRandom.current().nextInt(1,11);
				//System.out.println("duplicate");
			}
			else if(ans == wrongAns2)
			{
				wrongAns2 = ThreadLocalRandom.current().nextInt(10,21);
				//System.out.println("duplicate");
			}
			else if(ans == wrongAns3)
			{
				wrongAns3 = ThreadLocalRandom.current().nextInt(1,21);
				//System.out.println("duplicate");
			}
			else 
			{
				duplicate = false;
			}
			}while(duplicate == true);
			
			ch2 = Integer.toString(wrongAns1);
			ch3 = Integer.toString(wrongAns2); 
			ch4 = Integer.toString(wrongAns3);
			
			answers.add(ch1);
			answers.add(ch2);
			answers.add(ch3);
			answers.add(ch4);
			Collections.shuffle(answers);
			
		}
		
		/*
		 * This is a countdown timer called in 1 Arg Constructor 
		 */
		public static final void Countdown() {
	        final Timer timer = new Timer();
	        timer.scheduleAtFixedRate(new TimerTask() {
	        	
	            public void run() {
	            	time--;
	            	//System.out.println(time);
	                if (time < 0) {
	                    timer.cancel();
	                    timesUP = true;
	                    //System.out.println("TIME");
	                    correct = right;
	                    incorrect = wrong;
	                    total = correct + incorrect;
	                }
	            }
	        }, 0, 1000);
		}
		
		/*
		 * Gets the file topScores.txt and reads the data into the data-structures
		 */
		private void readScores() throws IOException{
			filepath = f.getCanonicalPath();
			File scoreData = new File(filepath + "\\topScores.txt");

			 BufferedReader in = new BufferedReader(
	                          new FileReader(scoreData));
			String line = in.readLine();
			while(line != null){
			   StringTokenizer token = new StringTokenizer(line, "\t");
			   
			   correct = Integer.parseInt(token.nextToken());
			   name = token.nextToken(); 
			   addtopScores(correct, name);
			   adduserNames(name,correct);
			   line = in.readLine();
			 }
			
			//displayNames()
	      	 in.close();
	      	
		}
		
		/*
		 * Simple display function in case I need it for debugging
		 */
		public void displayNames() {
			System.out.println("Top Scores: " + topScores);
	      	System.out.println("UserNames: " + userNames);
		}
		
		/*
		 * Adds a score to the file
		 */
		public void updateScores(int score, String name) throws IOException {
			PrintWriter out = new PrintWriter(
	                     	  new FileWriter("topScores.txt", true));
	        out.println(score + "\t" + name);

	        out.close();
		}
		
		/*
		 * Searches for the scores a person has
		 */
		public void search() throws IOException {
		   String  message = "Enter the name you want to search: ",
			       name = JOptionPane.showInputDialog(null, message);
		   try {
		   if (userNames.containsKey(name)) { 
			   display(name, userNames.get(name));
			   //System.out.println("found it");
		   }
		   else {
			   message = "This name is not in the records.\n"
			   			 + "Play a game to add it in.";
			   JOptionPane.showMessageDialog(null, message);
			   
			 } 
		   } catch(NullPointerException e) {}
		   
		 }
		
		/*
		 * called in search() to display the name and score.
		 */
		public void display(String name, ArrayList<Integer> arrayList) {
			String message = "Name: " + name + "\n" + 
		"Scores: " + arrayList;
			JOptionPane.showMessageDialog(null, message);
			
		}

		/*
		 * Creates arraylist of names for each score
		 */
		public void addtopScores(int correct, String name) {
		    ArrayList<String> scoreList = topScores.get(correct);

		    // if list does not exist create it
		    if(scoreList == null) {
		         scoreList = new ArrayList<String>();
		         scoreList.add(name);
		         topScores.put(correct, scoreList);
		    } else {
		        // add if item is not already in list
		        if(!scoreList.contains(name)) scoreList.add(name);
		    }
		}
		
		/*
		 * Creates arraylist of scores for each name
		 */
		public void adduserNames(String name, Integer correct) {
		    ArrayList<Integer> itemsList = userNames.get(name);

		    // if list does not exist create it
		    if(itemsList == null) {
		         itemsList = new ArrayList<Integer>();
		         itemsList.add(correct);
		         userNames.put(name, itemsList);
		    } else {
		        // add if item is not already in list
		        if(!itemsList.contains(correct)) itemsList.add(correct);
		    }
		}
		
		/*
		 * gets the name to add entry into the file topScores.txt
		 */
		public void getUserName() {
			name = JOptionPane.showInputDialog(null, "Please enter the name: ");
			try {
			if (name.equals("")) {
				JOptionPane.showMessageDialog(null, "Please enter a name (or nickname)");
				getUserName();
			}
			else {
				try {updateScores(correct,name);} catch (IOException e1) {}
				try {new MathGame();} catch (IOException e1) {}
			}
			} catch (NullPointerException e)
			{
				try {new MathGame();} catch (IOException e1) {}
			}
		}
		
		/*
		 * This function sets the data in the 2 dimensional array for the
		 * table of Top Scores. It takes the data from the tree map
		 * topScores. 
		 */
		public void setData() {
			
			//Array of the top 5 keys from topScores(tree map)
			Integer[] keys =  topScores.keySet().toArray(new Integer[5]);
			String[] names;
			String temp1;
			ArrayList<String> scoreList = null; //used as a temporary arraylist for names.
			
			
			for (int row = 0; row < 5; row++)
			{
				for( int col = 0; col < 3; col++)
				{
					if( col == 0)
					{
						//rank
						data[row][col] = Integer.toString(row + 1);
					}
					if( col == 1)
					{
						//places the keys in key array in data[][]
						temp1 = Integer.toString(keys[row]);
						//use key and initialize scoreList of names.
						scoreList = topScores.get(keys[row]);
						data[row][col] = temp1;
					}
					if( col == 2)
					{
						//create array of names from ArrayList
						names = scoreList.toArray(new String[scoreList.size()]);
						StringBuilder str = new StringBuilder();
						int rowHeight = 20; //create the height for rows
						//Html tags arrow my to store the string on multiple lines
						str.append("<html>");
						for(String temp2 : names)
						{
							str.append(temp2);
							str.append("<br>");
							rowHeight += 20; //increase row height for each new line.
						}
						str.append("</html>");
						rowSize[row] = rowHeight; //add to row height of arrays.
						data[row][col] = str.toString(); //add string of names to data[][]
					}
				}
			}
		}
		
		/*
		 * Plays a sound if the answer is correct
		 */
		public void sound() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
			filepath = f.getCanonicalPath();
			File rightSound = new File(filepath + "\\correct.wav");
			
			AudioInputStream audio = AudioSystem.getAudioInputStream(rightSound);
			
			Clip rightClip = AudioSystem.getClip();
			
			rightClip.open(audio);
			
			rightClip.start();
			
		}
		//This creates the main panel
		void mainpanel() {
			
			setSize(400,370);
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			setLayout(new BorderLayout());
			
			try {filepath = f.getCanonicalPath();} catch (IOException e) {}
			String math = filepath + "\\math.png";
			ImageIcon icon = new ImageIcon(math);
			
			JLabel background = new JLabel(icon);
			background.setLayout(new FlowLayout()); //adds Image
			
			String msg = "<html>Welcome to Samuel's Math Game.<br> "
					+ "The rules are simple. You have 60 <br> seconds "
					+ "to answer as many <br> questions as you can. <br>"
					+ "Select Start to begin.<html>";
			startLabel = new JLabel(msg);
			startLabel.setFont(new Font("Dialog",Font.PLAIN, 20));
			
			Start.setBackground(Color.GREEN); //Sets the color of the start button Green
			Exit.setBackground(Color.RED); //Sets the color of the exit button Red
			
			
			northpanel = new JPanel();
			northpanel.add(messageLabel);
			add(northpanel, BorderLayout.NORTH);
			
			mainpanel = new JPanel();
			mainpanel.add(startLabel);
			mainpanel.add(background);
			add(mainpanel, BorderLayout.CENTER);
			
			southpanel = new JPanel();
			southpanel.add(Start);
			southpanel.add(Scores);
			southpanel.add(searchScores);
			southpanel.add(Exit);
			add(southpanel, BorderLayout.SOUTH);
			
			Start.addActionListener(new ButtonListener());
			Scores.addActionListener(new ButtonListener());
			searchScores.addActionListener(new ButtonListener());
			Exit.addActionListener(new ButtonListener());
			
			setLocationRelativeTo(null);
			setVisible(true);
			
		}
		
		//This creates the math problems panel
		void buildpanel() {

			Question();
			Answers();
			
			setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
			
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			setLayout(new BorderLayout());
			
			Problem = new JLabel(problem);
			Problem.setFont(new Font("Dialog",Font.PLAIN, 25));
			
			
			Choice1 = new JButton(answers.get(0));
			Choice2 = new JButton(answers.get(1));
			Choice3 = new JButton(answers.get(2));
			Choice4 = new JButton(answers.get(3));
			
			//Buttons
			Choice1.addActionListener(new ButtonListener());
			Choice2.addActionListener(new ButtonListener());
			Choice3.addActionListener(new ButtonListener());
			Choice4.addActionListener(new ButtonListener());

			//Build Panel
			northpanel = new JPanel();
			northpanel.add(messageLabel);
			southpanel = new JPanel();
			
			southpanel.add(Choice1);
			southpanel.add(Choice2);
			southpanel.add(Choice3);
			southpanel.add(Choice4);
			
			centerpanel = new JPanel();
			centerpanel.add(Problem);

			add(northpanel, BorderLayout.NORTH);
			add(southpanel, BorderLayout.SOUTH);
			add(centerpanel, BorderLayout.CENTER);
			
			setLocationRelativeTo(null);
			setVisible(true);
			
		}
		
		//This is the class that handles the buttons
		private class ButtonListener implements ActionListener 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String actionCommand = e.getActionCommand();
				//System.out.println(actionCommand);
				
				if (actionCommand.equals(correctAnswer))
				 {
					try {sound();} 
					catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {}
					
					if(timesUP == false) { 
					right++;
					setVisible(false);
					new MathGame(1,1);
					}
					else { //When the time runs out
						setVisible(false);
						getUserName();
					}
				 }
				else if (actionCommand.equals("Start"))
				{
					setVisible(false);
					new MathGame(1);
					timesUP = false;
					time = 60;
				}
				else if (actionCommand.equals("Search Scores"))
				{
					try {search();} catch (IOException e1) {}
				}
				else if (actionCommand.equals("Top 5 Scores"))
				{
					setData();
					Table table = new Table(data,colNames,rowSize);
					
				}
				else if (actionCommand.equals("Exit")) //Exit the program
				 {
					int result1 = JOptionPane.showConfirmDialog(null, "Do you want to go Back?", 
			                "Math Game", 
			                JOptionPane.YES_NO_OPTION);
			    	
			    	if(result1 == JOptionPane.NO_OPTION)
			    	{
			    		setVisible(false);
						System.exit(0); //Exit the program
			    	}
					 
				 }
				else { //If the user enters a wrong answer
					if(timesUP == false) {
					wrong++;
					setVisible(false);
					new MathGame(1,1);
					}
					else {
						setVisible(false);
						getUserName();
					}
				}
			}
		}
		
}
