import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 *      **MainWindow Class**
 *
 * Defines the Main GUI of the application
 *
 * Several instance variables all representing the different components.
 * main is the main JFrame
 */
public class MainWindow {

    /*
     * Instance variables
     */
    private final JFrame main;      //Main JFrame
    private JPanel input, output;   //Input and Output panels for organizing
    private JTextField inputFile, classToRecompile; //Text fields for getting data
    private JTextPane order;    //Text pane for displaying data
    private JLabel inputLabel, classLabel;  //Labels for the inputs
    private JButton build, topological;     //Two buttons, build and get order
    private GridBagConstraints c;       //General constrains
    private Graph<String> graph;        //Built graph, only one instance at a time

    /*
     * 0 argument constructor
     * builds and shows the GUI
     */
    public MainWindow() {
        main = new JFrame("Class Dependency Graph");
        buildWindow();
        show();
    }

    /*
     * Creates and shows GUI
     */
    private void show() {
        main.setPreferredSize(new Dimension(600, 300));
        main.setSize(new Dimension(600, 350));
        main.setResizable(false);       //Sets size to be permanent
        main.setLocationRelativeTo(null);   //Center of the screen
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);   //Ensures the program terminates
        main.setVisible(true);      //Actually shows the window
    }

    /*
     * Creates all of the elements needed.  Defines anything special about them
     */
    private void createElements() {
        input = new JPanel();
        output = new JPanel();
        inputFile = new JTextField();
        classToRecompile = new JTextField();
        order = new JTextPane();
        order.setEditable(false);   //Makes order read only
        StyledDocument doc = order.getStyledDocument();     //MAkes the pane styles
        SimpleAttributeSet center = new SimpleAttributeSet();   //Makes a center attribute
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);   //Sets allignemnt to be senter
        doc.setParagraphAttributes(0, doc.getLength(), center, false);  //Uses constants to center text
        Font f = order.getFont();
        Font f2 = new Font(f.getFontName(), f.getStyle(), f.getSize() + 8); //Makes the font larger
        order.setFont(f2);
        order.setOpaque(false);     //Sets background to be transparent
        inputLabel = new JLabel("Input File Name", SwingConstants.CENTER);
        classLabel = new JLabel("Class to Recompile", SwingConstants.CENTER);
        build = new JButton("Build Directed Graph");
        build.addActionListener(e -> buildClick());  //Action on click
        topological = new JButton("Topological Order");
        topological.addActionListener(e -> topoClick());    //Action on click
        c = new GridBagConstraints();   //initializes
    }

    /*
     * Builds the input panel
     */
    private void buildInput() {
        input.setLayout(new GridBagLayout());

        //Row 1, Column 1
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 2);
        c.weightx = 0.1;
        c.weighty = 0.1;
        input.add(inputLabel, c);

        //Row 1, Column 2
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(5, 3, 5, 2);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        input.add(inputFile, c);

        //Row 1, Column 3
        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(5, 3, 5, 5);
        c.weightx = 0.0;
        c.weighty = 0.0;
        input.add(build, c);

        //Row 2, Column 1
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 5, 2);
        c.weightx = 0.1;
        c.weighty = 0.1;
        input.add(classLabel, c);

        //Row 2, Column 2
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 3, 5, 2);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        input.add(classToRecompile, c);

        //Row 2, Column 3
        c.gridx = 2;
        c.gridy = 1;
        c.insets = new Insets(5, 3, 5, 5);
        c.weightx = 0.0;
        c.weighty = 0.0;
        input.add(topological, c);
        input.setPreferredSize(new Dimension(600, 75));
        //input.setSize(600,100);
    }

    /*
     *  Builds the output panel
     */
    private void buildOutput() {
        output.setLayout(new GridLayout());
        output.setBorder(BorderFactory.createTitledBorder("Recompilation Order"));
        output.add(order);

        output.setPreferredSize(new Dimension(600, 225));
    }

    /*
     * Combines everything into one window
     */
    private void buildWindow() {
        main.setLayout(new GridBagLayout());
        createElements();
        buildInput();
        buildOutput();

        //Row 1
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        main.add(input, c);

        //Row 2
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridheight = 3;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        main.add(output, c);

    }

    /*
     * Event for clicking the build button
     */
    private void buildClick() {
        String file = inputFile.getText();  //Used to define file location
        BufferedReader br;      //Used to read file in
        String line;        //Current line
        String splitBy = " ";   //Delimiter
        ArrayList<String[]> wholeFile = new ArrayList<>();  //Stores the total array
        String[][] forToArray = new String[][]{{}}; //For converting to array
        try {
            br = new BufferedReader(new FileReader(file));  //Reads the file
            while ((line = br.readLine()) != null) {    //Goes line by line
                wholeFile.add(line.split(splitBy)); //Adds the row as an array
            }
            graph = new Graph<>(wholeFile.toArray(forToArray)); //Creates a graph from the read data
            JOptionPane.showMessageDialog(main, "Graph created successfully");
        } catch (FileNotFoundException e) {     //Self explanatory
            JOptionPane.showMessageDialog(main, "File not found");
        } catch (IOException e) {   //Self explanatory
            JOptionPane.showMessageDialog(main, "Unknown IO Error");
        }
    }

    /*
     * Gets the topological ordering
     */
    private void topoClick() {
        if (graph == null) {    //If the graph is not yet built
            JOptionPane.showMessageDialog(main, "You must first build a graph");
        } else {
            try {
                order.setText(graph.topologicalOrder(classToRecompile.getText()));  //Set the text as the order
            } catch (CycleDetectedException | InvalidClassNameException cycleDetectedException) {    //Self explanatory
                JOptionPane.showMessageDialog(main, cycleDetectedException.getMessage());
            }
        }
    }

}
