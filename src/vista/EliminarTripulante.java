package vista;
import modelo.ConexionBD;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class EliminarTripulante {
    public void actionPerformed(ActionEvent e,JList miembrosList, DefaultListModel<String> miembrosModel) {
        String selectedMember = String.valueOf ( miembrosList.getSelectedValue() );
        System.out.println (selectedMember );
        if (selectedMember != null) {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar el miembro seleccionado?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Eliminar el registro de la base de datos
                String[] memberInfo = selectedMember.split(" ");
                String phone = memberInfo[0];
                ConexionBD conexionBD = new ConexionBD();
                String query = "DELETE FROM miembros WHERE telefono = '" + phone + "'";
                conexionBD.ejecutarConsulta (query);

                // Actualizar la lista de miembros
                miembrosModel.removeElement(selectedMember);

                // Mostrar un mensaje de confirmación
                JOptionPane.showMessageDialog(null, "El miembro ha sido eliminado correctamente.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un miembro para eliminar.");
        }
    }

}
