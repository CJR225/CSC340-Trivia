import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
    private List<JSONObject> questionList;
    private int currentQuestionIndex;

    public ClientWindow(String clientID, String questionsFilePath) {
        this.clientID = clientID;
        loadQuestionsFromJson(questionsFilePath);

        JOptionPane.showMessageDialog(window, "This is a trivia game");
        window = new JFrame("Networking Trivia Show");
        question = new JLabel();
        window.add(question);
        question.setBounds(50, 50, 350, 100);

        options = new JRadioButton[4];
        for (int i = 0; i < options.length; i++) {
            options[i] = new JRadioButton();
            window.add(options[i]);
            options[i].setBounds(10, 110 + (i * 20), 350, 20);
            options[i].addActionListener(this);
        }
        optionGroup = new ButtonGroup();
        for (int i = 0; i < options.length; i++) {
            optionGroup.add(options[i]);
        }

        timer = new JLabel("TIMER");
        timer.setBounds(250, 250, 100, 20);
        clock = new TimerCode(15);
        Timer t = new Timer();
        t.schedule(clock, 0, 1000);
        window.add(timer);

        score = new JLabel("SCORE");
        score.setBounds(50, 250, 100, 20);
        window.add(score);

        poll = new JButton("Poll");
        poll.setBounds(10, 300, 100, 20);
        poll.addActionListener(this);
        window.add(poll);

        submit = new JButton("Submit");
        submit.setBounds(200, 300, 100, 20);
        submit.addActionListener(this);
        window.add(submit);

        window.setSize(500, 500);
        window.setBounds(50, 50, 500, 500);
        window.setLayout(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        displayNextQuestion();
    }

    private void loadQuestionsFromJson(String filePath) {
        JSONParser parser = new JSONParser();
        questionList = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader(filePath));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray questions = (JSONArray) jsonObject.get("questions");
            for (Object q : questions) {
                questionList.add((JSONObject) q);
            }
            Collections.shuffle(questionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayNextQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            JSONObject questionObj = questionList.get(currentQuestionIndex++);
            String questionTitle = (String) questionObj.get("questionTitle");
            question.setText("Q" + currentQuestionIndex + ". " + questionTitle);

            for (int i = 0; i < options.length; i++) {
                options[i].setText((String) questionObj.get("option" + (i + 1)));
            }
        } else {
            System.out.println("End of questions.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
}
