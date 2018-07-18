import javax.swing.*;
import java.awt.*;

public class TxtWin extends JFrame {
    public TxtWin(String text){
        super("Лог текущего запуска FolderSort440P");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JTextArea area = new JTextArea(text);
        JScrollPane scrollPane = new JScrollPane(area);
        mainPanel.add(scrollPane,BorderLayout.CENTER);

        add(mainPanel);
        setSize(800,600);
        setVisible(true);
    }
}
