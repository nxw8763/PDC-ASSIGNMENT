package gui.admin.users;

import javax.swing.table.AbstractTableModel;

import model.users.User;

import java.util.ArrayList;
import java.util.List;

public class UserTableModel extends AbstractTableModel {

    private final String[] columns = {
            "ID",
            "Username",
            "Name",
            "Email",
            "Role"
    };

    private List<User> users = new ArrayList<>();

    public void setUsers(List<User> users) {
        this.users = users;
        fireTableDataChanged();
    }

    public User getUserAt(int row) {
        return users.get(row);
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        User user = users.get(rowIndex);

        return switch (columnIndex) {

            case 0 -> user.getUserID();
            case 1 -> user.getUsername();
            case 2 -> user.getName();
            case 3 -> user.getEmail();
            case 4 -> user.getRole();

            default -> "";
        };
    }
}