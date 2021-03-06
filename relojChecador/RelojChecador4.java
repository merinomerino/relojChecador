package relojchecador;
//Importacion de las librerias
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
//Clase de reloj checador
public class RelojChecador4 extends JFrame {
//Nombre de los frames creados
    private JLabel lblBuscarPor, lblIDTrabajador, lblFechaInicial, lblFechaFinal, lblEntradaSalida;
    private JTextField tfIDTrabajador, tfFechaI, tfFechaF, tfEntradaSalida;
    private Font fuente;
    private JComboBox combo;
    private JCheckBox cBfecha, cBES;
    private JButton insertar, buscar, borrar, Restablecer, reporte;
    private JTable tabla;
    private DefaultTableModel dtm;
    private JScrollPane scroll;
    private Manejador objManejador;
    private Connection conexion;
    private Statement st;
    private ResultSet rs;
    private JPanel panel1, panel2, panel3, panel4, panel5, panel6, panel7, panel8;
//Direccion de la conexion de base de datos
    final private String driver = "com.mysql.jdbc.Driver";
    final private String base = "jdbc:mysql://localhost:3306/RelojChecador";
    final private String usuario = "root";
    final private String contraseña = "12345678";

    Object[] prueba;
    int tipo = 0;

    public RelojChecador4() {
        super("Ejemplo");
        setLayout(new BorderLayout());
//Inicializacion de los metodos
        crear();
        construir();
        agregar();
        mostrarTodo();
    }
//Creacion de las caracteristicas en la interfaz label,boton,fiel,box,etc...
    private void crear() {
        lblBuscarPor = new JLabel("Buscar por");
        lblIDTrabajador = new JLabel("ID trabajador");
        lblFechaInicial = new JLabel("Fecha Inicio");
        lblFechaFinal = new JLabel("Fecha Final");
        lblEntradaSalida = new JLabel("I/O");

        tfIDTrabajador = new JTextField(10);
        tfFechaI = new JTextField(10);
        tfFechaF = new JTextField(10);
        tfEntradaSalida = new JTextField(3);

        fuente = new Font("Arial", Font.BOLD, 18);

        combo = new JComboBox();
        cBfecha = new JCheckBox("Date", false);
        cBES = new JCheckBox("Entrada/Salida", false);

        insertar = new JButton("Insertar");
        buscar = new JButton("Buscar");
        borrar = new JButton("Borrar");
        Restablecer = new JButton("Restablecer ");
        reporte = new JButton("Reporte");

        dtm = new DefaultTableModel();

        tabla = new JTable(dtm);

        scroll = new JScrollPane(tabla);

        objManejador = new Manejador();

        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        panel5 = new JPanel();
        panel6 = new JPanel();
        panel7 = new JPanel();
        panel8 = new JPanel();
    }
//Construccion de los nombres de la fuente
    private void construir() {
        lblBuscarPor.setFont(fuente);
        lblIDTrabajador.setFont(fuente);
        lblFechaInicial.setFont(fuente);
        lblFechaFinal.setFont(fuente);
        lblEntradaSalida.setFont(fuente);

        combo.addItem("id_trabajador");
        combo.addItem("nombre");

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
                tfFechaI.setText(arreglo[2].toString());
                tfEntradaSalida.setText(arreglo[4].toString());
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                tfIDTrabajador.setEditable(false);
                tfFechaI.setEditable(false);
                tfEntradaSalida.setEditable(false);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                tfIDTrabajador.setEditable(true);
                tfFechaI.setEditable(true);
                tfEntradaSalida.setEditable(true);
            }
        });

        scroll.setPreferredSize(new Dimension(520, 200));

        insertar.addActionListener(objManejador);
        buscar.addActionListener(objManejador);
        borrar.addActionListener(objManejador);
        Restablecer.addActionListener(objManejador);
        reporte.addActionListener(objManejador);

        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));
        panel5.setLayout(new BoxLayout(panel5, BoxLayout.Y_AXIS));
        panel8.setLayout(new BorderLayout());
    }
//Agregacion al panel
    private void agregar() {
        panel1.add(lblBuscarPor);
        panel1.add(combo);
        panel1.add(cBfecha);
        panel1.add(cBES);

        panel2.add(lblIDTrabajador);
        panel2.add(Box.createVerticalStrut(20));
        panel2.add(lblFechaInicial);

        panel3.add(tfIDTrabajador);
        panel3.add(Box.createVerticalStrut(20));
        panel3.add(tfFechaI);

        panel4.add(lblEntradaSalida);
        panel4.add(Box.createVerticalStrut(20));
        panel4.add(lblFechaFinal);

        panel5.add(tfEntradaSalida);
        panel5.add(Box.createVerticalStrut(20));
        panel5.add(tfFechaF);

        panel6.add(insertar);
        panel6.add(buscar);
        panel6.add(borrar);
        panel6.add(Restablecer);
        panel6.add(reporte);

        panel7.add(panel2);
        panel7.add(panel3);
        panel7.add(panel4);
        panel7.add(panel5);

        panel8.add(panel1, BorderLayout.NORTH);
        panel8.add(panel7, BorderLayout.CENTER);
        panel8.add(panel6, BorderLayout.SOUTH);

        add(panel8, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

    }
//Mostrar la informacion del registro
    private void mostrarTodo() {
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(base, usuario, contraseña);

            if (!conexion.isClosed()) {
                st = conexion.createStatement();

                rs = st.executeQuery("SELECT * FROM Registro");

                while (rs.next()) {
                    prueba = new Object[dtm.getColumnCount()];
                    for (int i = 0; i < dtm.getColumnCount(); i++) {
                        prueba[i] = rs.getObject(i + 1);
                    }
                    dtm.addRow(prueba);
                }
                conexion.close();
            }

        } catch (Exception ex) {
            System.err.println("ERROR AL MOSTRAR TODOS EN TABLA\n" + ex.getMessage());
        }
    }
//Clase manejadora para las ejecuciones en el programa
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
            if (ae.getSource() == Restablecer) {
                limpiar();
                mostrarTodo();
            }
            if (ae.getSource() == reporte) {
                Reportes();
            }
        }
    }
//Tomado de la informacion de txt 
    private void insert() {

        String texto = "";
        String[] particion;
        String dir = "C:/Users/lokua/Desktop/BD-reloj-checador.txt";

        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            try {
                Connection miConexion = DriverManager.getConnection(base, usuario, contraseña);
                Statement mistate = miConexion.createStatement();
                while ((linea = br.readLine()) != null) {
                    particion = linea.split("\t");
                    for (int i = 0; i < 1; i++) {

                        String instruccion = "INSERT INTO Registro (id_trabajador, nombre, fecha, hora, "
                                + "entrada_salida) VALUES(" + particion[0] + ", '"
                                + particion[1] + "', '" + particion[2] + "', '" + particion[3]
                                + "', '" + particion[4] + "')";

                        mistate.executeUpdate(instruccion);
                        Object[] fila = {particion[0], particion[1], particion[2], particion[3], particion[4]};
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
//Metodo de buscar la informacion de la tabla
    private void search() {
        String buscar = null;

        if (combo.getSelectedItem() == "id_trabajador") {
            buscar = (String) combo.getSelectedItem();
        } else {
            buscar = (String) combo.getSelectedItem();
            tipo = -1;
        }
        if (tfFechaF != null) {
            tipo = 3;
        }

        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(base, usuario, contraseña);

            if (!conexion.isClosed()) {

                st = conexion.createStatement();

                if (cBfecha.isSelected() && cBES.isSelected()) {
                    if (tipo == -1) {
                        rs = st.executeQuery("SELECT  * from Registro where fecha = '" + tfFechaI.getText() + "' and entrada_salida = '" + tfEntradaSalida.getText() + "' and " + buscar + " ='" + tfIDTrabajador.getText() + "'");

                    } else {
                        rs = st.executeQuery("SELECT  * from Registro where fecha = '" + tfFechaI.getText() + "' and entrada_salida = '" + tfEntradaSalida.getText() + "' and id_trabajador=" + tfIDTrabajador.getText());
                    }
                    System.out.println("ambos");
                } else if (cBfecha.isSelected()) {
                    if (tipo == -1) {
                        rs = st.executeQuery("SELECT  * from Registro where fecha = '" + tfFechaI.getText() + "' AND " + buscar + " ='" + tfIDTrabajador.getText() + "'");
                    } else {
                        rs = st.executeQuery("SELECT  * from Registro where fecha = '" + tfFechaI.getText() + "' AND id_trabajador=" + tfIDTrabajador.getText());
                        System.out.println("fecha");
                    }
                } else if (cBES.isSelected()) {
                    if (tipo == -1) {
                        rs = st.executeQuery("SELECT  * from Registro where entrada_salida = '" + tfEntradaSalida.getText() + "' AND " + buscar + " ='" + tfIDTrabajador.getText() + "'");

                    } else {
                        rs = st.executeQuery("SELECT  * from Registro where entrada_salida = '" + tfEntradaSalida.getText() + "' AND id_trabajador=" + tfIDTrabajador.getText());
                        System.out.println("ES");
                    }
                } else if (tipo == 0) {
                    System.out.println("entro");
                    rs = st.executeQuery("SELECT * FROM Registro where " + buscar + " = " + tfIDTrabajador.getText());
                } else if (tipo == -1) {
                    System.out.println("entro");
                    rs = st.executeQuery("SELECT * FROM Registro where " + buscar + " = '" + tfIDTrabajador.getText() + "'");
                } else if (tipo == 3) {
                    rs = st.executeQuery("select * from Registro where fecha between '" + tfFechaI.getText() + "' and '" + tfFechaF.getText() + "'");
                }
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

    public void Reportes() {
//        try {
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\lokua\\Desktop\\Report.pdf"));
//
//            document.open();
//            document.add(new Paragraph("Reporte \n", FontFactory.getFont(FontFactory.TIMES_BOLD, 30, Font.ITALIC, BaseColor.RED)));
//            document.add(new Paragraph("    "));
//            PdfPTable table = new PdfPTable(dtm.getColumnCount());
//            table.addCell("id_trabajador");
//            table.addCell("nombre");
//            table.addCell("Fecha");
//            table.addCell("Hora");
//            table.addCell("EntradasSalidas");
//            for (int i = 0; i < dtm.getRowCount(); i++) {
//                for (int j = 0; j < dtm.getColumnCount(); j++) {
//                    table.addCell(dtm.getValueAt(i, j).toString());
//                }
//
//            }
//            document.add(table);
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e);
//        }

    }
//Borrar el registros de la tabla
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
//Limpiar las areas de los text
    private void limpiar() {
        tfIDTrabajador.setText("");
        tfEntradaSalida.setText("");
        tfFechaI.setText("");
        cBfecha.setSelected(false);
        cBES.setSelected(false);

        for (int i = 0; i < dtm.getRowCount(); i++) {
            dtm.removeRow(i);
            i -= 1;
        }
    }
//Visibilidad de la ventana
    public static void main(String[] args) {
        RelojChecador4 reloj = new RelojChecador4();

        reloj.setDefaultCloseOperation(EXIT_ON_CLOSE);
        reloj.setSize(580, 310);
        reloj.setLocation(200, 200);
        reloj.setVisible(true);

    }
}
