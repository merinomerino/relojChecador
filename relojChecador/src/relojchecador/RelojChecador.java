package relojchecador;

import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.scene.control.RadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class RelojChecador extends JFrame {

    private JLabel lblIDTrabajador, lblFecha, lblEntradaSalida;
    private JTextField tfIDTrabajador, tfFecha, tfEntradaSalida;
    private JComboBox combo;
    private JCheckBox cBfecha, cBES;
    private JButton insertar, buscar, borrar, Restablecer;
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

    int tipo = 0;
    

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
        lblFecha = new JLabel("Fecha");
        lblEntradaSalida = new JLabel("I/O");

        tfIDTrabajador = new JTextField(10);
        tfFecha = new JTextField(10);
        tfEntradaSalida = new JTextField(3);

        combo = new JComboBox();
        cBfecha = new JCheckBox("Date", false);
        cBES = new JCheckBox("Entrada/Salida", false);

        insertar = new JButton("Insertar");
        buscar = new JButton("Buscar");
        borrar = new JButton("Borrar");
        Restablecer = new JButton("Restablecer ");

        dtm = new DefaultTableModel();

        tabla = new JTable(dtm);

        scroll = new JScrollPane(tabla);

        objManejador = new Manejador();
    }

    private void construir() {
        tfFecha.setText(null);
        tfEntradaSalida.setText(null);

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
                tfFecha.setText(arreglo[2].toString());
                tfEntradaSalida.setText(arreglo[4].toString());
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                tfIDTrabajador.setEditable(false);
                tfFecha.setEditable(false);
                tfEntradaSalida.setEditable(false);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                tfIDTrabajador.setEditable(true);
                tfFecha.setEditable(true);
                tfEntradaSalida.setEditable(true);
            }
        });

        scroll.setPreferredSize(new Dimension(520, 200));

        insertar.addActionListener(objManejador);
        buscar.addActionListener(objManejador);
        borrar.addActionListener(objManejador);
        Restablecer.addActionListener(objManejador);
    }

    private void agregar() {
        add(combo);
        add(lblIDTrabajador);
        add(tfIDTrabajador);
        add(lblFecha);
        add(tfFecha);
        add(lblEntradaSalida);
        add(tfEntradaSalida);
        add(cBfecha);
        add(cBES);
        add(insertar);
        add(buscar);
        add(borrar);
        add(Restablecer);
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
            if (ae.getSource() == Restablecer) {
                limpiar();
                mostrarTodo();
            }
        }
    }

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

    private void search() {
        String buscar = null;

        if (combo.getSelectedItem() == "id_trabajador") {
            buscar = (String) combo.getSelectedItem();
        } else {
            buscar = (String) combo.getSelectedItem();
            tipo = -1;
        }

        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(base, usuario, contraseña);

            if (!conexion.isClosed()) {

                st = conexion.createStatement();

                if (cBfecha.isSelected() && cBES.isSelected()) {
                    if (tipo == -1) {
                        rs = st.executeQuery("SELECT  * from Registro where fecha = '" + tfFecha.getText() + "' and entrada_salida = '" + tfEntradaSalida.getText() + "' and " + buscar + " ='" + tfIDTrabajador.getText() + "'");

                    } else {
                        rs = st.executeQuery("SELECT  * from Registro where fecha = '" + tfFecha.getText() + "' and entrada_salida = '" + tfEntradaSalida.getText() + "' and id_trabajador=" + tfIDTrabajador.getText());
                    }
                    System.out.println("ambos");
                } else if (cBfecha.isSelected()) {
                    if (tipo == -1) {
                        rs = st.executeQuery("SELECT  * from Registro where fecha = '" + tfFecha.getText() + "' AND " + buscar + " ='" + tfIDTrabajador.getText() + "'");
                    } else {
                        rs = st.executeQuery("SELECT  * from Registro where fecha = '" + tfFecha.getText() + "' AND id_trabajador=" + tfIDTrabajador.getText());
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
//            RelojChecador re = new RelojChecador();//CREAMOS UN OBJETO DE LA CLASE REPORTES
//        String ruta = "ReportesTUTO\\fotos.jasper";//RUTA DONDE TIENEN SU REPORTE --
//        //ABRIR CUADRO DE DIALOGO PARA GUARDAR EL ARCHIVO         
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("todos los archivos *.PDF", "pdf", "PDF"));//filtro para ver solo archivos .pdf
//        int seleccion = fileChooser.showSaveDialog(null);
//        try {
//            if (seleccion == JFileChooser.APPROVE_OPTION) {//comprueba si ha presionado el boton de aceptar
//                File JFC = fileChooser.getSelectedFile();
//                String PATH = JFC.getAbsolutePath();//obtenemos la direccion del archivo + el nombre a guardar
//                try (PrintWriter printwriter = new PrintWriter(JFC)) {
//                    printwriter.print(ruta);
//                }
//                re.(ruta, PATH);//mandamos como parametros la ruta del archivo a compilar y el nombre y ruta donde se guardaran    
//                //comprobamos si a la hora de guardar obtuvo la extension y si no se la asignamos
//                if (!(PATH.endsWith(".pdf"))) {
//                    File temp = new File(PATH + ".pdf");
//                    JFC.renameTo(temp);//renombramos el archivo
//                }
//                JOptionPane.showMessageDialog(null, "Esto puede tardar unos segundos,espere porfavor", "Estamos Generando el Reporte", JOptionPane.WARNING_MESSAGE);
//                JOptionPane.showMessageDialog(null, "Documento Exportado Exitosamente!", "Guardado exitoso!", JOptionPane.INFORMATION_MESSAGE);
//            }
//        } catch (FileNotFoundException | HeadlessException e) {//por alguna excepcion salta un mensaje de error
//            JOptionPane.showMessageDialog(null, "Error al Exportar el archivo!", "Oops! Error", JOptionPane.ERROR_MESSAGE);
//        }

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
        tfEntradaSalida.setText("");
        tfFecha.setText("");
        cBfecha.setSelected(false);
        cBES.setSelected(false);

        for (int i = 0; i < dtm.getRowCount(); i++) {
            dtm.removeRow(i);
            i -= 1;
        }
    }

    public static void main(String[] args) {
        RelojChecador base = new RelojChecador();

        base.setDefaultCloseOperation(EXIT_ON_CLOSE);
        base.setSize(580, 310);
        base.setLocation(200, 200);
        base.setVisible(true);

    }
}
