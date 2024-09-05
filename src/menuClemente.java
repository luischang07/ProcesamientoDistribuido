
import java.sql.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;

import mode.Rutinas2;
import raven.toast.Notifications;

public class MenuClemente extends JPanel implements ComponentListener, ActionListener, ItemListener, FocusListener {
    static final String TICKETSD = "TICKETSD";
    static final String TICKETSH = "TICKETSH";

    private JPanel panel;
    private JPanel panelContent;
    private JPanel pnlTabla;

    private JComboBox<String> tableDropdown;

    private JLabel lblAtributoFiltro;
    private JComboBox<String> priceDropdown;

    private JButton btnLimpiar;
    private JButton btnPrecioMasUno;

    private JRadioButton rdEstado;
    private JRadioButton rdCiudad;
    private JRadioButton rdTienda;
    private ButtonGroup grupo2;

    private JTable table;
    private JScrollPane scroll;
    private DefaultTableModel modelo;

    private boolean selected = false;

    private ComponenteHeader componenteHeader;

    public MenuClemente(Connection conexion) {
        init();
        HazEscuchas();
    }

    private void HazEscuchas() {
        addComponentListener(this);
        tableDropdown.addItemListener(this);
        tableDropdown.addActionListener(this);
        btnPrecioMasUno.addActionListener(this);
        btnLimpiar.addActionListener(this);
        rdEstado.addActionListener(this);
        rdCiudad.addActionListener(this);
        rdTienda.addActionListener(this);
    }

    public void init() {
        setLayout(null);

        componenteHeader = new ComponenteHeader(this);
        add(componenteHeader, BorderLayout.NORTH);

        // PANEL--------------------------------------------------------
        panel = new JPanel();
        panel.setVisible(true);
        panel.setLayout(null);
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");

        tableDropdown = new JComboBox<String>(new String[] { "Seleccione" });
        llenarCombo();
        panel.add(tableDropdown);

        rdEstado = new JRadioButton("Estado");
        rdCiudad = new JRadioButton("Ciudad");
        rdTienda = new JRadioButton("Tienda");

        rdEstado.setEnabled(false);
        rdCiudad.setEnabled(false);
        rdTienda.setEnabled(false);

        panel.add(rdEstado);
        panel.add(rdCiudad);
        panel.add(rdTienda);

        grupo2 = new ButtonGroup();
        grupo2.add(rdEstado);
        grupo2.add(rdCiudad);
        grupo2.add(rdTienda);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setEnabled(false);
        panel.add(btnLimpiar);

        btnPrecioMasUno = new JButton("Mas +1");
        btnPrecioMasUno.setEnabled(false);
        panel.add(btnPrecioMasUno);

        add(panel);
        // FIN.PANEL-------------------------------------------------------

        panelContent = new JPanel();
        panelContent.setLayout(new Content());
        panelContent.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");
        add(panelContent);

        lblAtributoFiltro = new JLabel();
        panelContent.add(lblAtributoFiltro);
        priceDropdown = new JComboBox<String>(new String[] { "Seleccione" });
        panelContent.add(priceDropdown);
        priceDropdown.setVisible(false);

        // PNLTABLA-------------------------------------------------------
        pnlTabla = new JPanel();
        pnlTabla.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "border:10,20,30,10");

        modelo = new DefaultTableModel();
        table = new JTable(modelo);
        table.setDefaultEditor(Object.class, null);

        scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pnlTabla.add(scroll, BorderLayout.CENTER);

        pnlTabla.add(scroll, BorderLayout.CENTER);
        pnlTabla.setLayout(null);
        add(pnlTabla);
        // PNLTABLA-------------------------------------------------------

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == tableDropdown) {
            rdEstado.setEnabled(true);
            rdCiudad.setEnabled(true);
            rdTienda.setEnabled(true);
            // if (!band) {
            // if (rdNuevo.isSelected()) {
            // txtTipid.setText("*");
            // txtTipid.setEditable(false);
            // txtClienteId.setText("*");
            // txtClienteId.setEditable(false);
            // }
            // if (rdModificar.isSelected()) {
            // txtTipid.setText("");
            // txtTipid.setEditable(true);
            // txtClienteId.setText("");
            // txtClienteId.setEditable(true);
            // }
            // }

        }
        if (evt.getSource() == btnLimpiar) {
            limpiar();
            return;
        }

        if (evt.getSource() == btnPrecioMasUno) {
            String atributo = "";
            if (rdEstado.isSelected()) {
                atributo = rdEstado.getText();
            }
            if (rdCiudad.isSelected()) {
                atributo = rdCiudad.getText();
            }
            if (rdTienda.isSelected()) {
                atributo = rdTienda.getText();
            }

            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de sumar uno al precio de la tabla: " + tableDropdown.getSelectedItem().toString()
                            + ",  Atributo: " + atributo + ", Valor: " + priceDropdown.getSelectedItem().toString()
                            + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (dialogResult != JOptionPane.YES_OPTION) {
                return;
            }

            for (int i = 0; i < 200; i++) {
                try {
                    // if (sumarUnoPrecio() == 0) {
                    // ErrorHandler.showNotification("No se pudo sumar uno al precio");
                    // break;
                    // } else {
                    // ErrorHandler.showNotification("Se sumó uno al precio");
                    // }
                    sumarUnoPrecio();
                } catch (Exception e) {
                    ErrorHandler.showNotification("Error: " + e.getMessage());
                }
            }
        }
        if (evt.getSource() == rdEstado) {
            btnPrecioMasUno.setEnabled(true);
            lblAtributoFiltro.setText("IDESTADO");
            llenarComboFK("IDESTADO");
            priceDropdown.setVisible(true);
        }
        if (evt.getSource() == rdCiudad) {
            btnPrecioMasUno.setEnabled(true);
            lblAtributoFiltro.setText("IDCIUDAD");
            llenarComboFK("IDCIUDAD");
            priceDropdown.setVisible(true);
        }
        if (evt.getSource() == rdTienda) {
            btnPrecioMasUno.setEnabled(true);
            lblAtributoFiltro.setText("IDTIENDA");
            llenarComboFK("IDTIENDA");
            priceDropdown.setVisible(true);
        }

        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s
                    .executeQuery(
                            "SELECT top 1 TABLE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'"
                                    + tableDropdown.getSelectedItem().toString()
                                    + "' ");
            rs.next();

            llenarTabla(rs.getString(1));

        } catch (Exception e) {
            ErrorHandler.showNotification("Error: " + e.getMessage());
        }
    }

    // limpieza de los campos
    private void limpiar() {

    }

    private int sumarUnoPrecio() throws SQLException, InterruptedException {
        Statement s = null;
        boolean success = false;
        int rowsAffected = 0;

        while (!success) {
            try {
                System.out.println(lblAtributoFiltro.getText() + " " + priceDropdown.getSelectedItem().toString());
                s = ConexionDB.conexion.createStatement();
                s.executeUpdate("BEGIN transaction");
                String query = "UPDATE TICKETSD " +
                        "SET TICKETSD.PRECIO = TICKETSD.PRECIO + 1 " +
                        "FROM TICKETSD " +
                        "INNER JOIN TICKETSH ON TICKETSD.TICKET = TICKETSH.TICKET " +
                        "WHERE TICKETSH." + lblAtributoFiltro.getText() + " = "
                        + priceDropdown.getSelectedItem().toString() +
                        " AND (SELECT COUNT(DISTINCT TICKETSD.IDPRODUCTO) " +
                        "     FROM TICKETSD " +
                        "     WHERE TICKETSD.TICKET = TICKETSH.TICKET) >= 3";
                rowsAffected = s.executeUpdate(query);
                s.executeUpdate("commit transaction");
                success = true;
            } catch (SQLException e) {
                if (e.getErrorCode() == 1205) {
                    ErrorHandler.showNotification("Error: " + e.getMessage());
                } else {
                    ErrorHandler.handleSqlException(e);
                    success = true;
                }
            }
        }
        return rowsAffected;
    }

    private void llenarTabla(String Tabla) {
        try {
            modelo.setColumnCount(0);
            modelo.setRowCount(0);

            Statement s = ConexionDB.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs1 = s
                    .executeQuery(
                            "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'" + Tabla
                                    + "' ");
            while (rs1.next()) {
                modelo.addColumn(rs1.getString("COLUMN_NAME"));
            }

            ResultSet rs2 = s.executeQuery("SELECT top 1000 * FROM " + Tabla);

            while (rs2.next()) {
                Object[] fila = new Object[modelo.getColumnCount()];
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    fila[i] = rs2.getObject(i + 1);
                }
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void llenarCombo() {
        try {
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s
                    .executeQuery("select TABLE_NAME from INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = N'"
                            + TICKETSD + "' ");
            while (rs.next()) {
                tableDropdown.addItem(rs.getString("TABLE_NAME"));
            }
            tableDropdown.setSelectedIndex(0);
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                    "Error, " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void llenarComboFK(String filtro) {
        try {
            priceDropdown.removeAllItems();
            priceDropdown.addItem("Seleccione");
            Statement s = ConexionDB.conexion.createStatement();
            ResultSet rs = s
                    .executeQuery(
                            "SELECT distinct(" + filtro + ") FROM " + tableDropdown.getSelectedItem().toString() + " D "
                                    + " inner join " + TICKETSH + " H on " + "D.TICKET = H.TICKET order by " + filtro);
            while (rs.next()) {
                priceDropdown.addItem(rs.getString(filtro));
            }
            priceDropdown.setSelectedIndex(0);
        } catch (Exception e) {
            ErrorHandler.showNotification("Error: " + e.getMessage());
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        componenteHeader.setBounds(0, 0, getWidth(), (int) (getHeight() * .2));
        panel.setBounds(0, componenteHeader.getHeight(), getWidth(), (int) (getHeight() * .12));

        panelContent.setBounds(0, panel.getY() + panel.getHeight(), getWidth(),
                (int) (getHeight() * .5) - panel.getHeight());

        pnlTabla.setBounds(0, panelContent.getY() + panelContent.getHeight(),
                getWidth(),
                getHeight() - panelContent.getY() - panelContent.getHeight());

        int w = panel.getWidth();
        int h = panel.getHeight();

        int anchoComponente = (int) (w * .4);
        int altoComponente = (int) (h * .8);
        // -----------------------------------------------------------
        tableDropdown.setBounds((int) ((w * .025)), (int) (h * .03),
                anchoComponente,
                altoComponente);
        tableDropdown.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 350));

        rdEstado.setBounds((int) (tableDropdown.getX() * 1.2) + tableDropdown.getWidth(), (int) (h * .05),
                (int) (w * .20),
                (int) (h * .30));
        rdEstado.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 370));

        rdCiudad.setBounds(rdEstado.getX(), rdEstado.getY() + rdEstado.getHeight(),
                rdEstado.getWidth(),
                (int) (h * .30));
        rdCiudad.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 370));

        rdTienda.setBounds(rdEstado.getX(), rdCiudad.getY() + rdCiudad.getHeight(),
                rdEstado.getWidth(),
                (int) (h * .30));
        rdTienda.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 370));

        btnLimpiar.setBounds(rdEstado.getX() + rdEstado.getWidth(), (int) (h * .05),
                (int) (w * .97) - tableDropdown.getWidth() - rdEstado.getWidth() - tableDropdown.getWidth(),
                (int) (h * .45));
        btnLimpiar.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));

        btnPrecioMasUno.setBounds(rdEstado.getX() + rdEstado.getWidth(), (int) (h * .05),
                (int) (w * .25),
                (int) (h * .85));
        btnPrecioMasUno.setFont(Rutinas2.getFont("SegoeUI", false, 10, getWidth(), getHeight(), 400));
        // -----------------------------------------------------------)

        scroll.setBounds((int) (pnlTabla.getWidth() * .03), (int) (pnlTabla.getHeight() * .05),
                (int) (pnlTabla.getWidth() * .95), (int) (pnlTabla.getHeight() * .9));
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void itemStateChanged(ItemEvent evt) {
        if (evt.getSource() == tableDropdown) {
            if (!selected) {
                tableDropdown.removeItem("Seleccione");
                selected = true;
                return;
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

}