import javax.swing.text.AttributeSet;  import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import java.awt.*;
import java.awt.event.ActionEvent;

public class ConcreteGui extends JFrame implements InterfaceGui {
	private static final long serialVersionUID = 1L;

	private JTextArea displayArea;
	private JTextField nameField, repsField, setsField, weightField, secondsField;
	private JButton addButton, clearButton, displayWorkoutsButton, exportDataButton, viewStatsButton;
	private Controller controller;

	public ConcreteGui(Controller initialController) {
		super("Fitness Tracker App");
		this.controller = initialController;
		initialize();
	}

	// Method to set the controller if not set at construction
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void initialize() {
		setSize(600, 500); // Increased size for additional buttons
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		displayArea = new JTextArea();
		displayArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(displayArea);

		JPanel formPanel = createFormPanel();
		JPanel buttonPanel = createButtonPanel();

		add(scrollPane, BorderLayout.CENTER);
		add(formPanel, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void addButtonActionPerformed(ActionEvent e) {
	    try {
	        String name = nameField.getText().trim();
	        int reps = parseIntField(repsField.getText(), "Reps");
	        int sets = parseIntField(setsField.getText(), "Sets");
	        double weight = parseDoubleField(weightField.getText(), "Weight");
	        int seconds = parseIntField(secondsField.getText(), "Seconds");

	        AbstractExercise exercise = new ConcreteExercise(name, reps, sets, weight, seconds);
	        controller.addExercise(exercise);
	        JOptionPane.showMessageDialog(this, "Exercise added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	    } catch (IllegalArgumentException iae) {
	        JOptionPane.showMessageDialog(this, iae.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, "Failed to add exercise: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}




	private int parseIntField(String input, String fieldName) throws IllegalArgumentException {
	    input = input.trim();
	    if (input.isEmpty()) {
	        throw new IllegalArgumentException(fieldName + " cannot be blank.");
	    }
	    try {
	        return Integer.parseInt(input);
	    } catch (NumberFormatException e) {
	        throw new IllegalArgumentException("Invalid input for " + fieldName + ": '" + input + "'. Please ensure it's a valid integer.");
	    }
	}

	private double parseDoubleField(String input, String fieldName) throws IllegalArgumentException {
	    input = input.trim();
	    if (input.isEmpty()) {
	        throw new IllegalArgumentException(fieldName + " cannot be blank.");
	    }
	    try {
	        return Double.parseDouble(input);
	    } catch (NumberFormatException e) {
	        throw new IllegalArgumentException("Invalid input for " + fieldName + ": '" + input + "'. Please ensure it's a valid number.");
	    }
	}


	 private JPanel createFormPanel() {
	        JPanel formPanel = new JPanel(new GridLayout(5, 2));
	        formPanel.add(new JLabel("Name:"));
	        nameField = new JTextField();
	        formPanel.add(nameField);

	        formPanel.add(new JLabel("Reps:"));
	        repsField = new JTextField();
	        ((PlainDocument) repsField.getDocument()).setDocumentFilter(new DocumentFilter() {
	            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
	                if (string.matches("\\d*")) {  // Regex to allow only digits
	                    super.insertString(fb, offset, string, attr);
	                }
	            }

	            @Override
	            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
	                if (text.matches("\\d*")) {  // Regex to allow only digits
	                    super.replace(fb, offset, length, text, attrs);
	                }
	            }
	        });
	        formPanel.add(repsField);

	        formPanel.add(new JLabel("Sets:"));
	        setsField = new JTextField();
	        ((PlainDocument) setsField.getDocument()).setDocumentFilter(new DocumentFilter() {
	            @Override
	            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
	                if (string.matches("\\d*")) {  // Regex to allow only digits
	                    super.insertString(fb, offset, string, attr);
	                }
	            }

	            @Override
	            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
	                if (text.matches("\\d*")) {  // Regex to allow only digits
	                    super.replace(fb, offset, length, text, attrs);
	                }
	            }
	        });
	        formPanel.add(setsField);

	        formPanel.add(new JLabel("Weight:"));
	        weightField = new JTextField();
	        formPanel.add(weightField);

	        formPanel.add(new JLabel("Seconds:"));
	        secondsField = new JTextField();
	        formPanel.add(secondsField);

	        return formPanel;
	    }
	            
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		addButton = new JButton("Add Exercise");
		clearButton = new JButton("Clear");
		displayWorkoutsButton = new JButton("Display Workouts");
		exportDataButton = new JButton("Export Data");
		viewStatsButton = new JButton("View Stats");

		addButton.addActionListener(this::addButtonActionPerformed);
		clearButton.addActionListener(e -> clear());
		displayWorkoutsButton.addActionListener(this::displayWorkoutsActionPerformed); // Correct reference
		exportDataButton.addActionListener(e -> controller.exportWorkoutData());
		viewStatsButton.addActionListener(e -> controller.displayStatistics());

		buttonPanel.add(addButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(displayWorkoutsButton);
		buttonPanel.add(exportDataButton);
		buttonPanel.add(viewStatsButton);
		return buttonPanel;
	}

	private void displayWorkoutsActionPerformed(ActionEvent e) {
		controller.displayWorkouts();
	}

	@Override
	public void update() {
		displayArea.setText(controller.getWorkoutsDisplayText());
	}

	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	@Override
	public void clear() {
		nameField.setText("");
		repsField.setText("");
		setsField.setText("");
		weightField.setText("");
		secondsField.setText("");
		displayArea.setText("");
	}
}
