package gui.admin;

import model.AuditLog;
import model.users.Admin;
import service.AuditService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminAuditLogPanel extends JPanel {

    private final Admin admin;

    private JTable auditTable;
    private DefaultTableModel tableModel;

    public AdminAuditLogPanel(Admin admin) {

        this.admin = admin;

        buildUI();
        loadAuditLogs();
    }

    private void buildUI() {

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Audit Logs");
        titleLabel.setFont(
                titleLabel.getFont().deriveFont(
                        Font.BOLD,
                        24f
                )
        );

        JButton refreshButton =
                new JButton("Refresh");

        refreshButton.addActionListener(
                e -> loadAuditLogs()
        );

        JPanel topPanel = new JPanel(
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
                        new Object[]{
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
                            int column
                    ) {
                        return false;
                    }
                };

        auditTable =
                new JTable(tableModel);
        
        auditTable.getTableHeader().setReorderingAllowed(false);

        auditTable.setAutoCreateRowSorter(true);

        TableRowSorter<DefaultTableModel> sorter =
                new TableRowSorter<>(tableModel);

        auditTable.setRowSorter(sorter);

        auditTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(auditTable);

        add(
                scrollPane,
                BorderLayout.CENTER
        );
    }

    private void loadAuditLogs() {

        tableModel.setRowCount(0);

        try {

            List<AuditLog> logs =
                    AuditService.getAllAuditLogs(admin);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd HH:mm:ss"
                    );

            for (AuditLog log : logs) {

                tableModel.addRow(
                        new Object[] {
                                log.getAuditId(),
                                log.getUserId(),
                                log.getAction(),
                                log.getEntity(),
                                log.getEntityId(),
                                log.getDetails(),
                                log.getTimestamp()
                                        .format(formatter)
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}