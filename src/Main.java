import javax.swing.*;

public class Main {

    public static void main(String[] args){
        boolean lookAndFeel;
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            lookAndFeel = true;
        } catch (Exception e) {
            lookAndFeel = false;
        }
        if(!lookAndFeel){
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Look and Feel not set");
            }
        }

        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}
