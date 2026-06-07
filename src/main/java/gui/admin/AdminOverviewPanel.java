package gui.admin;

import service.OverviewService;
import model.users.Admin;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.Map;

public class AdminOverviewPanel extends JPanel {

    private final OverviewService overviewService;
    private final Admin admin;

    public AdminOverviewPanel(Admin admin) {

        this.admin = admin;
        this.overviewService = new OverviewService();

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        buildUI();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            refresh();
        }
        super.setVisible(visible);
    }

    private void buildUI() {

        JPanel content = new JPanel(new BorderLayout(15, 15));

        content.add(createKpiPanel(), BorderLayout.NORTH);
        content.add(createChartsPanel(), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    // ======================
    // KPI PANEL (CLEAN MAP VERSION)
    // ======================
    private JPanel createKpiPanel() {

        Map<String, Integer> counts =
                overviewService.getStatusCounts(admin);

        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));

        panel.add(createKpiCard("Open", counts.getOrDefault("OPEN", 0)));
        panel.add(createKpiCard("Assigned", counts.getOrDefault("ASSIGNED", 0)));
        panel.add(createKpiCard("In Progress", counts.getOrDefault("IN_PROGRESS", 0)));
        panel.add(createKpiCard("Resolved", counts.getOrDefault("RESOLVED", 0)));

        return panel;
    }

    private JPanel createKpiCard(String title, int value) {

        JPanel card = new JPanel(new BorderLayout());

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }

    // ======================
    // CHARTS LAYOUT
    // ======================
    private JPanel createChartsPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createTicketTrendChart(), gbc);

        gbc.gridx = 1;
        panel.add(createPriorityChart(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(createCategoryChart(), gbc);

        gbc.gridx = 1;
        panel.add(createStatusChart(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(createTechnicianChart(), gbc);

        return panel;
    }

    // ======================
    // TREND CHART (MAP CLEANUP)
    // ======================
    private ChartPanel createTicketTrendChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Integer> counts =
                overviewService.getTicketTrend(admin);

        for (int hour = 0; hour < 24; hour++) {

            String label = String.format("%02d:00", hour);

            dataset.addValue(
                    counts.getOrDefault(label, 0),
                    "Tickets",
                    label
            );
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Tickets Created Today",
                "Hour",
                "Tickets",
                dataset
        );

        return new ChartPanel(chart);
    }

    // ======================
    // PRIORITY CHART
    // ======================
    private ChartPanel createPriorityChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        overviewService.getPriorityCounts(admin)
                .forEach((k, v) -> dataset.addValue(v, "Tickets", k));

        JFreeChart chart = ChartFactory.createBarChart(
                "Priority Distribution",
                "Priority",
                "Tickets",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();

        NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return new ChartPanel(chart);
    }

    // ======================
    // STATUS CHART
    // ======================
    private ChartPanel createStatusChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        overviewService.getStatusCounts(admin)
                .forEach((k, v) -> dataset.addValue(v, "Tickets", k));

        JFreeChart chart = ChartFactory.createBarChart(
                "Status Distribution",
                "Status",
                "Tickets",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();

        NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return new ChartPanel(chart);
    }

    // ======================
    // CATEGORY PIE CHART
    // ======================
    private ChartPanel createCategoryChart() {

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        overviewService.getCategoryCounts(admin)
                .forEach(dataset::setValue);

        JFreeChart chart = ChartFactory.createPieChart(
                "Ticket Categories",
                dataset,
                true,
                true,
                false
        );

        return new ChartPanel(chart);
    }

    // ======================
    // TECHNICIAN CHART
    // ======================
    private ChartPanel createTechnicianChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        overviewService.getTechnicianWorkload(admin)
                .forEach((k, v) -> dataset.addValue(v, "Tickets", k));

        JFreeChart chart = ChartFactory.createBarChart(
                "Technician Workload",
                "Technician",
                "Open Tickets",
                dataset,
                PlotOrientation.HORIZONTAL,
                false,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();

        NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultItemLabelGenerator(
                new StandardCategoryItemLabelGenerator()
        );

        return new ChartPanel(chart);
    }

    // ======================
    // REFRESH
    // ======================
    public void refresh() {

        removeAll();
        buildUI();

        revalidate();
        repaint();
    }
}