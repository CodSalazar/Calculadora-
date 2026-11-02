import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazCalculadora interfaz = new InterfazCalculadora();
            interfaz.iniciar();
        });
    }
}
