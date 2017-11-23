package app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.io.*;
import javax.swing.*;

public class AccesoDatosTxt {

    frmPrincipal pdf = new frmPrincipal();

    private String archivo = "src/reporte.pdf";

    public void guardar(Persona per) {
        try {
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo, true));
            bw.write(per.getId_registro() + ","
                    + per.getId_trabajador() + ","
                    + per.getNombre() + ","
                    + per.getFecha() + ","
                    + per.getHora() + ","
                    + per.getEntrada_salida());
            bw.newLine();
            bw.close();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "no se pudieron guardar los datos\n" + ex.getMessage());
        }
    }
}
