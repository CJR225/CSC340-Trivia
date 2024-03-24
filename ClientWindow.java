import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.*;

public class ClientWindow implements ActionListener
{
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
    
    // Placeholder for client ID
    private String clientID;
    
    public ClientWindow()
    {
        JOptionPane.showMessageDialog(window, "This is a trivia game");
        
        window = new JFrame("Networking Trivia Show");
        question = new JLabel("Q1. What does IP stand for in networking?"); // represents the question
        window.add(question);
        question.setBounds(10, 5, 350, 100);;
        
        options = new JRadioButton[4];
		options[0] = new JRadioButton("Internet Protocol");
        options[1] = new JRadioButton("Internal Process");
        options[2] = new JRadioButton("Interesting Packet");
        options[3] = new JRadioButton("Infinite Possibilities");

        optionGroup = new ButtonGroup();
        for(int index=0; index<options.length; index++)
        {
            // if a radio button is clicked, the event would be thrown to this class to handle
            options[index].addActionListener(this);
            options[index].setBounds(10, 110+(index*20), 350, 20);
            window.add(options[index]);
            optionGroup.add(options[index]);
        }

        timer = new JLabel("TIMER");  // represents the countdown shown on the window
        timer.setBounds(250, 250, 100, 20);
        clock = new TimerCode(15);  // 15 seconds countdown
        Timer t = new Timer();  // event generator
        t.schedule(clock, 0, 1000); // clock is called every second
        window.add(timer);
        
        
        score = new JLabel("SCORE"); // represents the score
        score.setBounds(50, 250, 100, 20);
        window.add(score);

        poll = new JButton("Poll");  // button that use clicks/ like a buzzer
        poll.setBounds(10, 300, 100, 20);
        poll.addActionListener(this);  // calls actionPerformed of this class
        window.add(poll);
        
        submit = new JButton("Submit");  // button to submit their answer
        submit.setBounds(200, 300, 100, 20);
        submit.addActionListener(this);  // calls actionPerformed of this class
        window.add(submit);
        
        
        window.setSize(400,400);
        window.setBounds(50, 50, 400, 400);
        window.setLayout(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
    }

    // this method is called when you check/uncheck any radio button
    // this method is called when you press either of the buttons- submit/poll
    @Override
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("You clicked " + e.getActionCommand());
        
        // input refers to the radio button you selected or button you clicked
        String input = e.getActionCommand();  
        switch(input)
        {
            case "Internet Protocol": System.out.println("Correct Option");
            case "Internal Process":
            case "Interesting Packet":
            case "Infinite Possibilities":
                // Send selected option to server using TCP
                sendSelectedOptionToServer(input);
                break;
            case "Poll":
                // Send poll message to server using UDP
                sendPollToServer();
                break;
            case "Submit":
                // Send submit message to server (if needed)
                break;
            default:
                System.out.println("Incorrect Option");
        }
        
        // Update UI or perform other actions as needed
    }
    
    // Placeholder method to send selected option to server using TCP
    private void sendSelectedOptionToServer(String selectedOption) {
        // Implement logic to send selected option to server using TCP
        // You can use the clientID to identify the client
    }
    
    // Placeholder method to send poll message to server using UDP
    private void sendPollToServer() {
        // Implement logic to send poll message to server using UDP
        // You can use the clientID to identify the client
    }
    
    // this class is responsible for running the timer on the window
    public class TimerCode extends TimerTask
    {
        private int duration;  // write setters and getters as you need
        public TimerCode(int duration)
        {
            this.duration = duration;
        }
        @Override
        public void run()
        {
            if(duration < 0)
            {
                timer.setText("Timer expired");
                window.repaint();
                this.cancel();  // cancel the timed task
                return;
                // you can enable/disable your buttons for poll/submit here as needed
            }
            
            if(duration < 6)
                timer.setForeground(Color.red);
            else
                timer.setForeground(Color.black);
            
            timer.setText(duration+"");
            duration--;
            window.repaint();
        }
    }
    
} 

