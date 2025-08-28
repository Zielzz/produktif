package com.radja.form;

import com.radja.dao.DashboardDAO;
import com.radja.main.Form;
import com.radja.service.ServiceDashboard;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

public class FormDashboard extends Form{

    private final ServiceDashboard servisDashboard = new DashboardDAO();
    
    private final JPanel panelBarChart = new JPanel();
    private final JPanel panelLineChart = new JPanel();
    
    public FormDashboard() {
        init(); 
        changeMode();
    }

    private void changeMode() {
        UIManager.addPropertyChangeListener(evt -> {
            if ("lookAndFeel".equals(evt.getPropertyName())) {
                SwingUtilities.invokeLater(() -> {
                    createBarChart();
                    createLineChart();
                });
            }
        });
    }
    
    @Override
    public void formOpen() {
        super.formOpen();
        removeAll();
        init();
        revalidate();
        repaint();
    }
    
    private void init(){
        setLayout(new MigLayout("insets 10, fill, wrap","fill","[]0[fill, grow]"));
        add(panelCard());
        add(panelChart());
    }
    
    private JPanel panelCard() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 0 0 10 0", "[grow,fill]", "[]0[]"));
        
        JLabel lbLogoJumlahTransaksi = new JLabel(new FlatSVGIcon("com/radja/icon/jumlah_transaksi.svg", 50, 50));
        JLabel lbLogoPendapatan = new JLabel(new FlatSVGIcon("com/radja/icon/pendapatan.svg", 50, 50));
        JLabel lbLogoProses = new JLabel(new FlatSVGIcon("com/radja/icon/proses.svg", 50, 50));
        JLabel lbLogoSelesai = new JLabel(new FlatSVGIcon("com/radja/icon/selesai.svg", 50, 50));
        
        int totalTransaksi = servisDashboard.getJumlahTransaksi();
        int pendapatanPerHari = servisDashboard.getPendapatanPerHari();
        int pesananStatusProses = servisDashboard.getPesananByStatusByProses();
        int pesananStatusSelesai = servisDashboard.getPesananByStatusBySelesai();
        
        panel.add(createCard("Jumlah Transaksi", totalTransaksi, lbLogoJumlahTransaksi), "hmin 100");
        panel.add(createCard("Pendapatan /hari", pendapatanPerHari, lbLogoPendapatan), "hmin 100");
        panel.add(createCard("Sedang Berlangsung", pesananStatusProses, lbLogoProses), "hmin 100");
        panel.add(createCard("Selesai", pesananStatusSelesai, lbLogoSelesai), "hmin 100");
        return panel;
    }
    
    private JPanel createCard(String title, int total, JLabel logo) {
        JPanel card = new JPanel(new MigLayout("fill"));
        card.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:rgb(255,255,255);" +
                "[dark]background:tint($Panel.background,10%);");

        JLabel titleLabel = new JLabel(title);
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font:16;foreground:$Label.disabledForeground");
        
        JLabel valueLabel = new JLabel();
        valueLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold 24");
        
        if (title.equalsIgnoreCase("Pendapatan /hari")) {
            NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String formattedRupiah = rupiahFormat.format(total);

            formattedRupiah = formattedRupiah.replace("Rp", "Rp. ").replace(",00", "");
            valueLabel.setText(formattedRupiah);
        } else {
            valueLabel.setText(String.valueOf(total));
        }
        
        card.add(valueLabel, "cell 0 0");
        card.add(titleLabel, "cell 0 1");
        if (logo != null) {
            card.add(logo, "cell 1 0 1 2, align right");
        }
        
        return card;
    }
    
    private JPanel panelChart(){
        JPanel panel = new JPanel(new MigLayout("fill, insets 10 0 10 0, wrap","fill"));
        
        panel.add(createLineChart());
        panel.add(createBarChart(),"gapy 10");
        return panel;
    }

    private JPanel createLineChart() {
        Map<String, Double> data = servisDashboard.getPendapatanPerBulan();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), "Pendapatan", entry.getKey());
        }
        
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Pendapatan Per Bulan",
                "Bulan",
                "Pendapatan (Rp)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        TextTitle title = new TextTitle(lineChart.getTitle().getText(),
                new Font("SansSerif", Font.BOLD, 14));
        lineChart.setTitle(title);
        lineChart.setBackgroundPaint(null);
        lineChart.getLegend().setBackgroundPaint(null);
        lineChart.getLegend().setBorder(0, 0, 0, 0);
        lineChart.getLegend().setItemPaint(Color.GRAY);
        
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(null);
        plot.setOutlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
        
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(52, 152, 219));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(new Color(52, 152, 219));
        plot.setRangeGridlinesVisible(true);
        plot.setRenderer(renderer);
        
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(800, 400));
        chartPanel.setMaximumDrawWidth(1920);
        chartPanel.setMaximumDrawHeight(1080);


        panelLineChart.removeAll();
        panelLineChart.setLayout(new MigLayout("fill, insets 10","fill","fill"));
        panelLineChart.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "background:rgb(255,255,255);");
        panelLineChart.add(chartPanel);
        panelLineChart.revalidate();
        panelLineChart.repaint();

        return panelLineChart;
    }

    private JPanel createBarChart() {
        Map<String, Integer> data = servisDashboard.getJumlahTransaksiPerBulan();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), "Jumlah Transaksi", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Jumlah Transaksi Per Bulan",
                "Bulan",
                "Jumlah Transaksi",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        TextTitle title = new TextTitle(barChart.getTitle().getText(),
                new Font("SansSerif", Font.BOLD, 14));
        barChart.setTitle(title);
        barChart.setBackgroundPaint(null);
        barChart.getLegend().setBackgroundPaint(null);
        barChart.getLegend().setBorder(0, 0, 0, 0);
        barChart.getLegend().setItemPaint(Color.GRAY);
        
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(null);
        plot.setOutlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(52, 152, 219));
        renderer.setMaximumBarWidth(0.1);
        
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(800, 400));
        chartPanel.setMaximumDrawWidth(1920);
        chartPanel.setMaximumDrawHeight(1080);

        panelBarChart.removeAll();
        panelBarChart.setLayout(new MigLayout("fill, insets 10","fill","fill"));
        panelBarChart.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "background:rgb(255,255,255);");
        panelBarChart.add(chartPanel);
        panelBarChart.revalidate();
        panelBarChart.repaint();

        return panelBarChart;
    }

}
