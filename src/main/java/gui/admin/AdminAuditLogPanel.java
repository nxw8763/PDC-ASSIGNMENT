package gui.admin;

import controller.AuditController;
import dto.AuditLogDTO;
import model.users.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class AdminAuditLogPanel
        extends JPanel
        implements AuditLogView {

    private final Admin admin;

    private final AuditController controller;

    private JTable auditTable;
    private DefaultTableModel tableModel;

    public AdminAuditLogPanel(
            Admin admin,
            AuditController controller) {

        this.admin = admin;
        this.controller = controller;

        buildUI();

        controller.loadAuditLogs(
                admin,
                this
        );
    }

    private void buildUI() {

        setLayout(
                new BorderLayout()
        );

        JLabel titleLabel =
                new JLabel(
                        "Audit Logs"
                );

        titleLabel.setFont(
                titleLabel.getFont()
                        .deriveFont(
                                Font.BOLD,
                                24f
                        )
        );

        JButton refreshButton =
                new JButton(
                        "Refresh"
                );

        refreshButton.addActionListener(
                e -> controller.loadAuditLogs(
                        admin,
                        this
                )
        );

        JPanel topPanel =
                new JPanel(
                        new BorderLayout()
                );

        topPanel.add(
                titleLabel,
                BorderLayout.WEST
        );

        topPanel.add(
                refreshButton,
                BorderLayout.EAST
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        tableModel =
                new DefaultTableModel(
                        new Object[] {
                                "Audit ID",
                                "User ID",
                                "Action",
                                "Entity",
                                "Entity ID",
                                "Details",
                                "Timestamp"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        auditTable =
                new JTable(
                        tableModel
                );

        auditTable
                .getTableHeader()
                .setReorderingAllowed(
                        false
                );

        auditTable.setAutoCreateRowSorter(
                true
        );

        auditTable.setRowSorter(
                new TableRowSorter<>(
                        tableModel
                )
        );

        auditTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        add(
                new JScrollPane(
                        auditTable
                ),
                BorderLayout.CENTER
        );
    }

    @Override
    public void displayAuditLogs(
            List<AuditLogDTO> logs) {

        tableModel.setRowCount(0);

        for (AuditLogDTO log : logs) {

            tableModel.addRow(
                    new Object[] {
                            log.getAuditId(),
                            log.getUserId(),
                            log.getAction(),
                            log.getEntity(),
                            log.getEntityId(),
                            log.getDetails(),
                            log.getTimestamp()
                    }
            );
        }
    }

    @Override
    public void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}