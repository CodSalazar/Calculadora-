import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class InterfazCalculadora {

    private final Calculadora calculadora = new Calculadora();

    private JFrame ventana;
    private JTextField visor;
    private JLabel etiquetaOperacion;

    private DefaultListModel<String> modeloHistorial;
    private JList<String> listaHistorial;
    private JPanel panelHistorial;

    private double numeroAnterior = 0;
    private String operador = "";
    private boolean iniciarNuevoNumero = true;
    private boolean error = false;
    private boolean resultadoMostrado = false;

    private final Color fondo = new Color(23, 23, 29);
    private final Color panel = new Color(32, 32, 40);
    private final Color gris = new Color(65, 65, 77);
    private final Color grisClaro = new Color(88, 88, 103);
    private final Color morado = new Color(120, 60, 220);
    private final Color rojo = new Color(190, 55, 65);
    private final Color verde = new Color(35, 150, 95);
    private final Color texto = Color.WHITE;

    public void iniciar() {
        ventana = new JFrame("Calculadora");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setMinimumSize(new Dimension(560, 500));
        ventana.setSize(760, 560);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout(12, 12));
        ventana.getContentPane().setBackground(fondo);

        crearPanelCalculadora();
        crearPanelHistorial();

        ventana.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarHistorial();
            }
        });

        ventana.setVisible(true);
        ajustarHistorial();
    }

    private void crearPanelCalculadora() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 12));
        panelPrincipal.setBackground(fondo);
        panelPrincipal.setBorder(new EmptyBorder(14, 14, 14, 8));

        JPanel panelVisor = new JPanel(new BorderLayout(4, 5));
        panelVisor.setBackground(fondo);

        etiquetaOperacion = new JLabel(" ");
        etiquetaOperacion.setForeground(new Color(180, 180, 190));
        etiquetaOperacion.setFont(new Font("Arial", Font.PLAIN, 15));
        etiquetaOperacion.setHorizontalAlignment(SwingConstants.RIGHT);

        visor = new JTextField("0");
        visor.setFont(new Font("Verdana", Font.BOLD, 32));
        visor.setHorizontalAlignment(JTextField.RIGHT);
        visor.setEditable(false);
        visor.setFocusable(false);
        visor.setBackground(panel);
        visor.setForeground(texto);
        visor.setCaretColor(texto);
        visor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 55, 65)),
                new EmptyBorder(14, 12, 14, 12)
        ));

        panelVisor.add(etiquetaOperacion, BorderLayout.NORTH);
        panelVisor.add(visor, BorderLayout.CENTER);
        panelPrincipal.add(panelVisor, BorderLayout.NORTH);

        String[] nombres = {
            "C", "Borrar", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "−",
            "1", "2", "3", "+",
            "±", "0", ".", "="
        };

        JPanel panelBotones = new JPanel(new GridLayout(5, 4, 8, 8));
        panelBotones.setBackground(fondo);

        for (String nombre : nombres) {
            JButton boton = new JButton(nombre);
            boton.setFont(new Font("Arial", Font.BOLD, 22));
            boton.setForeground(texto);
            boton.setFocusPainted(false);
            boton.setBorderPainted(false);
            boton.setOpaque(true);
            boton.setBackground(colorBoton(nombre));
            boton.setToolTipText(descripcionBoton(nombre));
            boton.addActionListener(e -> manejarBoton(nombre));
            panelBotones.add(boton);
        }

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        ventana.add(panelPrincipal, BorderLayout.CENTER);
    }

    private void crearPanelHistorial() {
        panelHistorial = new JPanel(new BorderLayout(8, 8));
        panelHistorial.setBackground(panel);
        panelHistorial.setBorder(new EmptyBorder(14, 10, 14, 14));
        panelHistorial.setPreferredSize(new Dimension(220, 100));

        JLabel titulo = new JLabel("Historial");
        titulo.setForeground(texto);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        modeloHistorial = new DefaultListModel<>();
        listaHistorial = new JList<>(modeloHistorial);
        listaHistorial.setBackground(new Color(40, 40, 49));
        listaHistorial.setForeground(texto);
        listaHistorial.setFont(new Font("Arial", Font.PLAIN, 14));
        listaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaHistorial.setFixedCellHeight(30);

        JScrollPane desplazamiento = new JScrollPane(listaHistorial);
        desplazamiento.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70)));

        JButton limpiarHistorial = new JButton("Borrar historial");
        limpiarHistorial.setForeground(texto);
        limpiarHistorial.setBackground(gris);
        limpiarHistorial.setFocusPainted(false);
        limpiarHistorial.addActionListener(e -> modeloHistorial.clear());

        panelHistorial.add(titulo, BorderLayout.NORTH);
        panelHistorial.add(desplazamiento, BorderLayout.CENTER);
        panelHistorial.add(limpiarHistorial, BorderLayout.SOUTH);

        ventana.add(panelHistorial, BorderLayout.EAST);
    }

    private void ajustarHistorial() {
        if (ventana == null || panelHistorial == null) {
            return;
        }

        // En ventanas estrechas se oculta el historial para dar más espacio
        // a los botones. Al ampliar la ventana, vuelve a aparecer.
        boolean mostrar = ventana.getWidth() >= 680;
        panelHistorial.setVisible(mostrar);
        ventana.revalidate();
    }

    private Color colorBoton(String nombre) {
        if (nombre.equals("=")) {
            return verde;
        }
        if (nombre.equals("C") || nombre.equals("Borrar")) {
            return rojo;
        }
        if (esOperador(nombre) || nombre.equals("%") || nombre.equals("±")) {
            return morado;
        }
        if (nombre.equals(".")) {
            return grisClaro;
        }
        return gris;
    }

    private String descripcionBoton(String nombre) {
        switch (nombre) {
            case "C": return "Borrar la operación actual";
            case "Borrar": return "Borrar el último dígito";
            case "%": return "Convertir el número actual en porcentaje (dividir entre 100)";
            case "±": return "Cambiar el signo del número";
            case "=": return "Calcular el resultado";
            default: return nombre;
        }
    }

    private boolean esOperador(String texto) {
        return texto.equals("+") || texto.equals("−")
                || texto.equals("×") || texto.equals("÷");
    }

    private void manejarBoton(String texto) {
        if (texto.equals("C")) {
            limpiar();
        } else if (texto.equals("Borrar")) {
            borrarUltimo();
        } else if (texto.equals("%")) {
            aplicarPorcentaje();
        } else if (texto.equals("±")) {
            cambiarSigno();
        } else if (texto.equals("=")) {
            calcularResultado();
        } else if (esOperador(texto)) {
            seleccionarOperador(texto);
        } else {
            agregarNumero(texto);
        }
    }

    private void agregarNumero(String texto) {
        if (error) {
            limpiar();
        }

        if (resultadoMostrado) {
            visor.setText("0");
            etiquetaOperacion.setText(" ");
            resultadoMostrado = false;
            iniciarNuevoNumero = true;
        }

        if (iniciarNuevoNumero) {
            if (texto.equals(".")) {
                visor.setText("0.");
            } else {
                visor.setText(texto);
            }
            iniciarNuevoNumero = false;
            return;
        }

        if (texto.equals(".") && visor.getText().contains(".")) {
            return;
        }

        if (visor.getText().equals("0") && !texto.equals(".")) {
            visor.setText(texto);
        } else if (visor.getText().equals("-0") && !texto.equals(".")) {
            visor.setText("-" + texto);
        } else {
            visor.setText(visor.getText() + texto);
        }
    }

    private void seleccionarOperador(String nuevoOperador) {
        if (error) {
            return;
        }

        if (resultadoMostrado) {
            resultadoMostrado = false;
        }

        if (operador.isEmpty()) {
            if (iniciarNuevoNumero && visor.getText().equals("0")) {
                return;
            }
            numeroAnterior = obtenerNumeroVisor();
        } else if (!iniciarNuevoNumero) {
            calcularOperacionPendiente(true);
            if (error) {
                return;
            }
        }

        operador = nuevoOperador;
        iniciarNuevoNumero = true;
        etiquetaOperacion.setText(formatear(numeroAnterior) + " " + operadorVisible(operador));
    }

    private void calcularResultado() {
        if (error || operador.isEmpty() || iniciarNuevoNumero) {
            return;
        }

        String expresion = formatear(numeroAnterior) + " "
                + operadorVisible(operador) + " " + visor.getText();

        if (calcularOperacionPendiente(false)) {
            String resultado = visor.getText();
            agregarAlHistorial(expresion + " = " + resultado);
            etiquetaOperacion.setText(expresion + " =");
            operador = "";
            iniciarNuevoNumero = true;
            resultadoMostrado = true;
        }
    }

    private boolean calcularOperacionPendiente(boolean continuar) {
        double numeroNuevo = obtenerNumeroVisor();
        double resultado;

        try {
            switch (operador) {
                case "+":
                    resultado = calculadora.sumar(numeroAnterior, numeroNuevo);
                    break;
                case "−":
                    resultado = calculadora.restar(numeroAnterior, numeroNuevo);
                    break;
                case "×":
                    resultado = calculadora.multiplicar(numeroAnterior, numeroNuevo);
                    break;
                case "÷":
                    resultado = calculadora.dividir(numeroAnterior, numeroNuevo);
                    break;
                default:
                    return false;
            }

            if (!Double.isFinite(resultado)) {
                throw new ArithmeticException("El resultado está fuera del rango permitido.");
            }

            visor.setText(formatear(resultado));
            numeroAnterior = resultado;

            if (continuar) {
                etiquetaOperacion.setText(formatear(numeroAnterior));
            }

            return true;

        } catch (ArithmeticException ex) {
            mostrarError(ex.getMessage());
            return false;
        }
    }

    private void aplicarPorcentaje() {
        if (error || visor.getText().isEmpty()) {
            return;
        }

        double valor = obtenerNumeroVisor();
        double porcentaje = valor / 100.0;

        visor.setText(formatear(porcentaje));
        iniciarNuevoNumero = false;
        resultadoMostrado = false;
        etiquetaOperacion.setText(formatear(valor) + " %");
    }

    private void cambiarSigno() {
        if (error) {
            return;
        }

        double valor = obtenerNumeroVisor();
        visor.setText(formatear(-valor));
        iniciarNuevoNumero = false;
        resultadoMostrado = false;
    }

    private void borrarUltimo() {
        if (error || resultadoMostrado) {
            limpiar();
            return;
        }

        if (iniciarNuevoNumero) {
            return;
        }

        String contenido = visor.getText();

        if (contenido.length() <= 1
                || (contenido.length() == 2 && contenido.startsWith("-"))) {
            visor.setText("0");
            iniciarNuevoNumero = true;
        } else {
            visor.setText(contenido.substring(0, contenido.length() - 1));
        }
    }

    private void limpiar() {
        visor.setText("0");
        etiquetaOperacion.setText(" ");
        numeroAnterior = 0;
        operador = "";
        iniciarNuevoNumero = true;
        error = false;
        resultadoMostrado = false;
    }

    private double obtenerNumeroVisor() {
        return Double.parseDouble(visor.getText());
    }

    private String formatear(double numero) {
        if (numero == 0) {
            return "0";
        }
        return BigDecimal.valueOf(numero).stripTrailingZeros().toPlainString();
    }

    private String operadorVisible(String op) {
        return op;
    }

    private void mostrarError(String mensaje) {
        visor.setText("Error");
        etiquetaOperacion.setText(mensaje);
        operador = "";
        iniciarNuevoNumero = true;
        error = true;
        resultadoMostrado = false;
    }

    private void agregarAlHistorial(String operacion) {
        modeloHistorial.add(0, operacion);
        listaHistorial.setSelectedIndex(0);
    }
}
