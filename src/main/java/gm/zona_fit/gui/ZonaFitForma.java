package gm.zona_fit.gui;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.ClienteServicio;
import gm.zona_fit.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

//@Component
public class ZonaFitForma extends JFrame {
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField membresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    IClienteServicio clienteServicio;
    private DefaultTableModel tablaModeloClientes;
    private Integer IdCliente;


@Autowired
    public ZonaFitForma(ClienteServicio clienteServicio){
    this.clienteServicio = clienteServicio;
    iniciarForma();
    guardarButton.addActionListener(e -> guardarCliente());
    clientesTabla.addComponentListener(new ComponentAdapter() {
    });
    clientesTabla.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            cargarClienteSelecionado();
        }
    });
    eliminarButton.addActionListener(e -> eliminarCliente());

    limpiarButton.addActionListener(e -> limpiarFormulario());
}


    private void iniciarForma(){
    setContentPane(panelPrincipal);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900, 700);
    setLocationRelativeTo(null);//Centramos la ventana.


    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        //this.tablaModeloClientes = new DefaultTableModel(0, 4);
        //Evitamos la Edicion de los valores de la tabla con doble click
        this.tablaModeloClientes = new DefaultTableModel(0, 4){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        String[] cabeceros = {"Id", "Nombre", "Apellido", "Membresia"};
        this.tablaModeloClientes.setColumnIdentifiers(cabeceros);
        this.clientesTabla = new JTable(tablaModeloClientes);
        //Restringimos la selecion de la tabla a un solo valor
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Cargar listado de tipo Clientes
        listarClientes();
    }
    private void listarClientes(){
    this.tablaModeloClientes.setRowCount(0);
    var clientes = this.clienteServicio.listarClientes();
    clientes.forEach(cliente -> {
        Object[] renglonCliente = {
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getMembresia()
        };
        this.tablaModeloClientes.addRow(renglonCliente);
    });
    }
    private void guardarCliente () {
        if (nombreTexto.getText().equals("")) {
            mostrarMensaje("Proporciona un Nombre");
            nombreTexto.requestFocusInWindow();
            return;
        }
        if (membresiaTexto.getText().equals("")) {
            mostrarMensaje("Proporciona una Membresia");
            membresiaTexto.requestFocusInWindow();
            return;
        }
        //Recuperar los valores del formulario
        var nombre = nombreTexto.getText();
        var apellido = apellidoTexto.getText();
        var membresia = Integer.parseInt(membresiaTexto.getText());
        var cliente = new Cliente(this.IdCliente, nombre, apellido, membresia);
        this.clienteServicio.guardarCliente(cliente);//Se inserta el cliente
        if(this.IdCliente == null)
            mostrarMensaje("Se agrego un nuevo Cliente");
        else
            mostrarMensaje("Se modifico el cliente");
        limpiarFormulario();
        listarClientes();
    }
    private void cargarClienteSelecionado(){
        var renglon = clientesTabla.getSelectedRow();
        if (renglon != -1){//-1 signifca que ningun renglon fue selecionado
            var id = clientesTabla.getModel().getValueAt(renglon, 0).toString();
            this.IdCliente = Integer.parseInt(id);
            var nombre = clientesTabla.getModel().getValueAt(renglon, 1).toString();
            this.nombreTexto.setText(nombre);
            var apellido = clientesTabla.getModel().getValueAt(renglon, 2).toString();
            this.apellidoTexto.setText(apellido);
            var membresia = clientesTabla.getModel().getValueAt(renglon, 3).toString();
            this.membresiaTexto.setText(membresia);
        }
    }
    private void eliminarCliente(){
        var renglon = clientesTabla.getSelectedRow();
        if (renglon != -1){
            var idClienteStr = clientesTabla.getModel().getValueAt(renglon, 0).toString();
            this.IdCliente = Integer.parseInt(idClienteStr);
            var cliente = new Cliente();
            cliente.setId(this.IdCliente);
            clienteServicio.eliminarCliente(cliente);
            mostrarMensaje("Cliente con id " + this.IdCliente + " eliminado");
            limpiarFormulario();
            listarClientes();
        }else{
            mostrarMensaje("Elige un Cliente a eliminar");
        }
    }
    private void limpiarFormulario(){
    nombreTexto.setText("");
    apellidoTexto.setText("");
    membresiaTexto.setText("");
    //Limpiamos el valor de ID del cliente selecionado
        this.IdCliente = null;
    //Desselcionamos el regristo de la tabla selecionada
    this.clientesTabla.getSelectionModel().clearSelection();
    }
    private void mostrarMensaje(String mensaje){
    JOptionPane.showMessageDialog(this, mensaje);
    }
}
