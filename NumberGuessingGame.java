import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class NumberGuessingGame extends JFrame {
    private int randomNumber;
    private int attemptsLeft;
    private JTextField guessField;
    private JTextArea resultArea;

    public NumberGuessingGame() {
        setTitle("Riya - Number Guessing Game");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeGame();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(238, 156, 167);
                Color color2 = new Color(255, 221, 225);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        panel.setLayout(null);

        JLabel guessLabel = new JLabel("Enter your guess:");
        guessLabel.setBounds(100, 40, 270, 50);
        guessLabel.setFont(new Font("Arial", Font.BOLD, 20));

        guessField = new JTextField();
        guessField.setBounds(350, 40, 70, 50);
        guessField.setFont(new Font("Arial", Font.BOLD, 20));

        JButton guessButton = new JButton("GUESS");
        guessButton.setBounds(70, 120, 200, 50);
        guessButton.setBackground(new Color(152, 251, 152)); // Peace color
        guessButton.setFont(new Font("Arial", Font.BOLD, 25));
        guessButton.addActionListener(new GuessButtonListener());

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setBounds(300, 120, 200, 50);
        playAgainButton.setBackground(new Color(173, 216, 230)); // Light blue color
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 25)); // Bold text
        playAgainButton.addActionListener(new PlayAgainButtonListener());

        // Horizontal line
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBounds(20, 200, 560, 10);

        JLabel resultLabel = new JLabel("Result");
        resultLabel.setBounds(250, 200, 100, 50);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));

        resultArea = new JTextArea();
        resultArea.setBounds(50, 240, 500, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(guessLabel);
        panel.add(guessField);
        panel.add(guessButton);
        panel.add(playAgainButton);
        panel.add(separator);
        panel.add(resultLabel);
        panel.add(resultArea);

        add(panel);

        setVisible(true);
    }

    private void initializeGame() {
        randomNumber = (int) (Math.random() * 100) + 1;
        attemptsLeft = 10;
    }

    private class GuessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int guess = Integer.parseInt(guessField.getText());
                checkGuess(guess);
            } catch (NumberFormatException ex) {
                resultArea.setText("Invalid input. Please enter a number.");
            }
        }
    }

    private class PlayAgainButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            initializeGame();
            resultArea.setText("");
            guessField.setText("");
            guessField.requestFocus();
        }
    }

    private void checkGuess(int guess) {
        attemptsLeft--;

        if (guess == randomNumber) {
            resultArea.setText("Congratulations! You guessed the correct number.");
            saveScore(attemptsLeft, randomNumber);
            initializeGame();
        } else {
            String hint = (guess < randomNumber) ? "Too low" : "Too high";
            resultArea.setText(String.format("%s. Attempts left: %d", hint, attemptsLeft));

            if (attemptsLeft == 0) {
                resultArea.append(
                        String.format("\nSorry, you've run out of attempts. The correct number was %d.", randomNumber));
                saveScore(attemptsLeft, randomNumber); // Save even if the player runs out of attempts
                initializeGame();
            }
        }

        guessField.setText("");
        guessField.requestFocus();
    }

    private void saveScore(int score, int correctNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", true))) {
            writer.write(String.format("Attempts left: %d, Correct Number: %d%n", score, correctNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberGuessingGame::new);
    }
}