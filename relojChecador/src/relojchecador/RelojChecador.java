
package relojchecador;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class RelojChecador extends JFrame {

    private JLabel lblIDTrabajador;
    private JTextField tfIDTrabajador;
    private JComboBox combo;
    private JButton insertar, buscar, borrar;
    private JTable tabla;
    private DefaultTableModel dtm;
    private JScrollPane scroll;
    private Manejador objManejador;
    private Connection conexion;
    private Statement st;
    private ResultSet rs;

    final private String driver = "com.mysql.jdbc.Driver";
    final private String base = "jdbc:mysql://localhost:3306/RelojChecador";
    final private String usuario = "root";
    final private String contraseña = "12345678";

    public RelojChecador() {
        super("Ejemplo");
        setLayout(new FlowLayout());

        crear();
        construir();
        agregar();
        mostrarTodo();
    }

    private void crear() {
        lblIDTrabajador = new JLabel("ID trabajador");

        tfIDTrabajador = new JTextField(10);

        combo = new JComboBox();

        insertar = new JButton("Insertar");
        buscar = new JButton("Buscar");
        borrar = new JButton("Borrar");

        dtm = new DefaultTableModel();

        tabla = new JTable(dtm);

        scroll = new JScrollPane(tabla);

        objManejador = new Manejador();
    }

    private void construir() {
        combo.addItem("id_trabajador");
        combo.addItem("nombre");
        combo.addItem("fecha");
        combo.addItem("hora");

        dtm.addColumn("ID trabajador");
        dtm.addColumn("Nombre");
        dtm.addColumn("Fecha");
        dtm.addColumn("Hora");
        dtm.addColumn("Entrada_Salida");

        tabla.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                Object[] arreglo = new Object[dtm.getColumnCount()];
                for (int i = 0; i < dtm.getColumnCount(); i++) {
                    arreglo[i] = dtm.getValueAt(tabla.getSelectedRow(), i);
                }

                tfIDTrabajador.setText(arreglo[0].toString());
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                tfIDTrabajador.setEditable(false);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                tfIDTrabajador.setEditable(true);
            }
        });

        scroll.setPreferredSize(new Dimension(400, 200));

        insertar.addActionListener(objManejador);
        buscar.addActionListener(objManejador);
        borrar.addActionListener(objManejador);
    }

    private void agregar() {
        add(combo);
        add(lblIDTrabajador);
        add(tfIDTrabajador);
        add(insertar);
        add(buscar);
        add(borrar);
        add(scroll);
    }

    private void mostrarTodo() {
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(base, usuario, contraseña);

            if (!conexion.isClosed()) {
                st = conexion.createStatement();

                rs = st.executeQuery("SELECT * FROM Registro");

                while (rs.next()) {
                    Object[] fila = new Object[dtm.getColumnCount()];
                    for (int i = 0; i < dtm.getColumnCount(); i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    dtm.addRow(fila);
                }
                conexion.close();
            }

        } catch (Exception ex) {
            System.err.println("ERROR AL MOSTRAR TODOS EN TABLA\n" + ex.getMessage());
        }
    }

    private class Manejador implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == insertar) {
                insert();
            }

            if (ae.getSource() == buscar) {
                search();
            }

            if (ae.getSource() == borrar) {
                delete();
            }
        }
    }

    private void insert() {

        String texto = "";
        String[] cadenaseparada;
        String direccion = "C:/Users/lokua/Desktop/BD-reloj-checador.txt";

        try {
            FileReader fr = new FileReader(direccion);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            try {
                Connection miConexion = DriverManager.getConnection(base, usuario, contraseña);
                Statement mistate = miConexion.createStatement();
                while ((linea = br.readLine()) != null) {
                    cadenaseparada = linea.split("\t");
                    for (int i = 0; i < 1; i++) {

                        String instruccion = "INSERT INTO Registro (id_trabajador, nombre, fecha, hora, "
                                + "entrada_salida) VALUES(" + cadenaseparada[0] + ", '"
                                + cadenaseparada[1] + "', '" + cadenaseparada[2] + "', '" + cadenaseparada[3]
                                + "', '" + cadenaseparada[4] + "')";

                        mistate.executeUpdate(instruccion);

                        Object[] fila = {cadenaseparada[0], cadenaseparada[1], cadenaseparada[2], cadenaseparada[3], cadenaseparada[4]};
                        dtm.addRow(fila);
                    }
                }
                System.out.println("TERMINE");
            } catch (Exception e) {
                System.out.println("No conecta");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("No se encontro archivo");
        }
    }

    private void search() {
        String buscar = null;
        int tipo = 0;

        if (combo.getSelectedItem() == "id_trabajador") {
            buscar = (String) combo.getSelectedItem();
        }
        if (combo.getSelectedItem() == "nombre") {
            buscar = (String) combo.getSelectedItem();
            tipo = -1;
        }
        if (combo.getSelectedItem() == "fecha") {
            buscar = (String) combo.getSelectedItem();
            tipo = -1;
        }
       
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(base, usuario, contraseña);

            if (!conexion.isClosed()) {
                st = conexion.createStatement();
                if (tipo == 0) {
                    rs = st.executeQuery("SELECT * FROM Registro where " + buscar + " = " + tfIDTrabajador.getText());
                }
                rs = st.executeQuery("SELECT * FROM Registro where " + buscar + " = " + "'" + tfIDTrabajador.getText() + "'");

                limpiar();

                while (rs.next()) {
                    Object[] fila = new Object[5];
                    for (int i = 0; i < 5; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    dtm.addRow(fila);
                }

            }

        } catch (Exception ex) {
            System.err.println("ERROR AL BUSCAR\n" + ex.getMessage());
        }

        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(base, usuario, contraseña);

            if (!conexion.isClosed()) {
                st = conexion.createStatement();

//                st.executeUpdate("UPDATE Registro SET nombre = '" + tfNombre.getText() + "', apellidos = '" + tfFecha.getText() + "', carrera = '" + tfHora.getText() + "' WHERE matricula = " + tfIDTrabajador.getText());
            }

        } catch (Exception ex) {
            System.err.println("ERROR AL BUSCAR\n" + ex.getMessage());
        }
    }

    public void filtro() {

    }

    private void delete() {
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(base, usuario, contraseña);

            if (!conexion.isClosed()) {
                st = conexion.createStatement();

                st.executeUpdate("DELETE FROM Registro");
                limpiar();
                conexion.close();
            }

        } catch (Exception ex) {
            System.err.println("ERROR AL BORRAR\n" + ex.getMessage());
        }
    }

    private void limpiar() {
        tfIDTrabajador.setText("");

        for (int i = 0; i < dtm.getRowCount(); i++) {
            dtm.removeRow(i);
            i -= 1;
        }
    }

    public static void main(String[] args) {
        RelojChecador base = new RelojChecador();

        base.setDefaultCloseOperation(EXIT_ON_CLOSE);
        base.setSize(450, 330);
        base.setLocation(200, 200);
        base.setVisible(true);

    }
}

    

