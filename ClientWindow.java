import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.*;

public class ClientWindow implements ActionListener {
    private JButton poll;
    private JButton submit;
    private JRadioButton options[];
    private ButtonGroup optionGroup;
    private JLabel question;
    private JLabel timer;
    private JLabel score;
    private TimerTask clock;
    private JFrame window;
    private static SecureRandom random = new SecureRandom();
    private String clientID;
    private List<Question> questionList;
    private int currentQuestionIndex;

    public ClientWindow(String clientID, String questionsFilePath) {
        this.clientID = clientID;
        loadQuestionsFromFile(questionsFilePath);

        JOptionPane.showMessageDialog(window, "This is a trivia game");
        window = new JFrame("Networking Trivia Show");
        question = new JLabel();
        window.add(question);
        question.setBounds(50, 0, 700, 100);

        options = new JRadioButton[4];
        for (int i = 0; i < options.length; i++) {
            options[i] = new JRadioButton();
            window.add(options[i]);
            options[i].setBounds(10, 100 + (i * 40), 350, 20); // Adjusted Y-coordinate
            options[i].addActionListener(this);
        }
        
        optionGroup = new ButtonGroup();
        for (int i = 0; i < options.length; i++) {
            optionGroup.add(options[i]);
        }

        timer = new JLabel("TIMER");
        timer.setBounds(250, 270, 100, 20);
        clock = new TimerCode(15);
        Timer t = new Timer();
        t.schedule(clock, 0, 1000);
        window.add(timer);

        score = new JLabel("SCORE");
        score.setBounds(50, 270, 100, 20);
        window.add(score);

        poll = new JButton("Poll");
        poll.setBounds(10, 300, 100, 20);
        poll.addActionListener(this);
        window.add(poll);

        submit = new JButton("Submit");
        submit.setBounds(200, 300, 100, 20);
        submit.addActionListener(this);
        window.add(submit);

        window.setSize(800, 500);
        window.setBounds(50, 50, 800, 500);
        window.setLayout(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        displayNextQuestion();
    }

    private void loadQuestionsFromFile(String filePath) {
        questionList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder questionTitle = new StringBuilder();
            List<String> optionsList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Q")) {
                    // Save the previous question
                    if (questionTitle.length() > 0) {
                        questionList.add(new Question(questionTitle.toString(), new ArrayList<>(optionsList)));
                        questionTitle.setLength(0);
                        optionsList.clear();
                    }
                    questionTitle.append(line).append("\n");
                } else if (line.startsWith("Option")) {
                    optionsList.add(line.substring(line.indexOf(":") + 2)); // Extract option text
                }
            }
            if (questionTitle.length() > 0) {
                questionList.add(new Question(questionTitle.toString(), new ArrayList<>(optionsList)));
            }
            Collections.shuffle(questionList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void displayNextQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question currentQuestion = questionList.get(currentQuestionIndex++);
            question.setText(currentQuestion.getQuestionTitle());

            List<String> optionsList = currentQuestion.getOptions();
            // Update the text for each option button
            for (int i = 0; i < optionsList.size() && i < options.length; i++) {
                options[i].setText(optionsList.get(i));
            }
            window.revalidate();
            window.repaint();
        } else {
            System.out.println("End of questions.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action performed: " + e.getActionCommand()); // Debug print
        String selectedOption = e.getActionCommand();
        switch (selectedOption) {
            case "Poll":
                sendPollToServer();
                break;
            case "Submit":
                // Implement logic to handle submit button click
                break;
            default:
                sendSelectedOptionToServer(selectedOption);
                displayNextQuestion(); // Display the next question after selecting an option
                break;
        }
        System.out.println("Options after action: "); // Debug print
        for (JRadioButton option : options) {
            System.out.println(option.getText()); // Debug print
        }
    }

    private void sendPollToServer() {
        // Implement logic to send poll message to server using UDP
    }

    private void sendSelectedOptionToServer(String selectedOption) {
        // Implement logic to send selected option to server using TCP
    }

    public class TimerCode extends TimerTask {
        private int duration;

        public TimerCode(int duration) {
            this.duration = duration;
        }

        @Override
        public void run() {
            if (duration < 0) {
                timer.setText("Timer expired");
                window.repaint();
                this.cancel();
                return;
            }

            if (duration < 6)
                timer.setForeground(Color.red);
            else
                timer.setForeground(Color.black);

            timer.setText(duration + "");
            duration--;
            window.repaint();
        }
    }

    private class Question {
        private String questionTitle;
        private List<String> options;

        public Question(String questionTitle, List<String> options) {
            this.questionTitle = questionTitle;
            this.options = options;
        }

        public String getQuestionTitle() {
            return questionTitle;
        }

        public List<String> getOptions() {
            return options;
        }
    }
}
