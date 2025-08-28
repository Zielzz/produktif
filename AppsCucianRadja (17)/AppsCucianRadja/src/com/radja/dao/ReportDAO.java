package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Pesanan;
import com.radja.model.BiayaOperasional;
import com.radja.model.Pemasukan;
import com.radja.model.Pengeluaran;
import com.radja.service.ServiceReport;
import com.radja.util.QRCodeGenerator;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;

public class ReportDAO implements ServiceReport {

    private final Connection conn;

    public ReportDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void printBarang() {
        try {
            String reportPath = "src/com/radja/report/jasper/ReportBarang.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

            UIManager.put("ComboBox.padding", new Insets(0, 10, 0, 0));
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan barang: " + e.getMessage());
        }
    }

    @Override
    public void saveBarangPDF() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            fileChooser.setSelectedFile(new File("Save Barang Report " + saveDate + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".pdf")) {
                    savePath += ".pdf";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportBarang.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF barang: " + e.getMessage());
        }
    }

    @Override
    public void saveBarangExcel() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            fileChooser.setSelectedFile(new File("Save Barang Report " + saveDate + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".xlsx")) {
                    savePath += ".xlsx";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportBarang.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_FORMAT", "XLSX");
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);
                exporter.setConfiguration(configuration);
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan Excel barang: " + e.getMessage());
        }
    }

    @Override
    public void printPelanggan() {
        try {
            String reportPath = "src/com/radja/report/jasper/ReportPelanggan.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

            UIManager.put("ComboBox.padding", new Insets(0, 10, 0, 0));
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan pelanggan: " + e.getMessage());
        }
    }

    @Override
    public void savePelangganPDF() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            fileChooser.setSelectedFile(new File("Save Pelanggan Report " + saveDate + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".pdf")) {
                    savePath += ".pdf";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportPelanggan.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF pelanggan: " + e.getMessage());
        }
    }

    @Override
    public void savePelangganExcel() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            fileChooser.setSelectedFile(new File("Save Pelanggan Report " + saveDate + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".xlsx")) {
                    savePath += ".xlsx";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportPelanggan.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_FORMAT", "XLSX");
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);
                exporter.setConfiguration(configuration);
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan Excel pelanggan: " + e.getMessage());
        }
    }

    @Override
    public void printLayanan() {
        try {
            String reportPath = "src/com/radja/report/jasper/ReportLayanan.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

            UIManager.put("ComboBox.padding", new Insets(0, 10, 0, 0));
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan layanan: " + e.getMessage());
        }
    }

    @Override
    public void saveLayananPDF() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            fileChooser.setSelectedFile(new File("Save Layanan Report " + saveDate + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".pdf")) {
                    savePath += ".pdf";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportLayanan.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF layanan: " + e.getMessage());
        }
    }

    @Override
    public void saveLayananExcel() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            fileChooser.setSelectedFile(new File("Save Layanan Report " + saveDate + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".xlsx")) {
                    savePath += ".xlsx";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportLayanan.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_FORMAT", "XLSX");
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);
                exporter.setConfiguration(configuration);
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan Excel layanan: " + e.getMessage());
        }
    }

    @Override
    public void printKaryawan() {
        try {
            String reportPath = "src/com/radja/report/jasper/ReportKaryawan.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

            UIManager.put("ComboBox.padding", new Insets(0, 10, 0, 0));
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan karyawan: " + e.getMessage());
        }
    }

    @Override
    public void saveKaryawanPDF() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            fileChooser.setSelectedFile(new File("Save Karyawan Report " + saveDate + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".pdf")) {
                    savePath += ".pdf";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportKaryawan.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF karyawan: " + e.getMessage());
        }
    }

    @Override
    public void saveKaryawanExcel() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            fileChooser.setSelectedFile(new File("Save Karyawan Report " + saveDate + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".xlsx")) {
                    savePath += ".xlsx";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportKaryawan.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_FORMAT", "XLSX");
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);
                exporter.setConfiguration(configuration);
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan Excel karyawan: " + e.getMessage());
        }
    }

    @Override
    public void printPesananPeriode(Date startDate, Date endDate) {
        try {
            String reportPath = "src/com/radja/report/jasper/ReportPesananPeriode.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

            UIManager.put("ComboBox.padding", new Insets(0, 10, 0, 0));
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan pesanan periode: " + e.getMessage());
        }
    }

    @Override
    public void savePesananPeriodePDF(Date startDate, Date endDate) {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            fileChooser.setSelectedFile(new File("Save Pesanan Report " + saveDate + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".pdf")) {
                    savePath += ".pdf";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportPesananPeriode.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("startDate", startDate);
                parameters.put("endDate", endDate);
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF pesanan periode: " + e.getMessage());
        }
    }

    @Override
    public void savePesananPeriodeExcel(Date startDate, Date endDate) {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            fileChooser.setSelectedFile(new File("Save Pesanan Report " + saveDate + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".xlsx")) {
                    savePath += ".xlsx";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportPesananPeriode.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("startDate", startDate);
                parameters.put("endDate", endDate);
                parameters.put("REPORT_FORMAT", "XLSX");
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);
                exporter.setConfiguration(configuration);
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan Excel pesanan periode: " + e.getMessage());
        }
    }

    @Override
    public void generateQRCodeBuktiTransaksi(List<Pesanan> list) {
        try {
            if (list == null || list.isEmpty()) {
                JOptionPane.showMessageDialog(null, "List pesanan kosong! Tidak dapat menghasilkan QR Code.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String reportPath = "src/com/radja/report/jasper/QRCodeBuktiTransaksi.jasper";
            HashMap<String, Object> parameters = new HashMap<>();

            StringBuilder qrData = new StringBuilder();
            String idPesanan = null;

            Pesanan pesanan = list.get(0);
            idPesanan = pesanan.getIdPesanan();
            String tanggal = pesanan.getTanggalPesanan() != null ? pesanan.getTanggalPesanan().toString() : "N/A";
            String namaPelanggan = pesanan.getNamaPelanggan() != null ? pesanan.getNamaPelanggan() : "N/A";
            String noHp = pesanan.getPelanggan() != null && pesanan.getPelanggan().getTelepon() != null
                    ? pesanan.getPelanggan().getTelepon() : "N/A";
            String merkMobil = pesanan.getMerkMobil() != null ? pesanan.getMerkMobil() : "N/A";
            String nomorPlat = pesanan.getNomorPlat() != null ? pesanan.getNomorPlat() : "N/A";
            String namaLayanan = pesanan.getLayanan() != null && pesanan.getLayanan().getNamaLayanan() != null
                    ? pesanan.getLayanan().getNamaLayanan() : "N/A";
            Double hargaLayanan = pesanan.getLayanan() != null ? pesanan.getLayanan().getHarga() : 0.0;
            Double totalHarga = pesanan.getTotalHarga() != null ? pesanan.getTotalHarga() : 0.0;

            qrData.append("ID Pesanan: ").append(idPesanan).append("\n")
                  .append("Tanggal Order: ").append(tanggal).append("\n")
                  .append("Nama Pelanggan: ").append(namaPelanggan).append("\n")
                  .append("Nomor Handphone: ").append(noHp).append("\n")
                  .append("Merk Kendaraan: ").append(merkMobil).append("\n")
                  .append("Nomor Plat Kendaraan: ").append(nomorPlat).append("\n")
                  .append("Layanan: ").append(namaLayanan).append("\n")
                  .append("Harga Layanan: ").append(hargaLayanan).append("\n")
                  .append("Total Harga: ").append(totalHarga);

            String qrDataString = qrData.toString();
            if (qrDataString.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Data QR Code kosong! Tidak dapat menghasilkan QR Code.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int qrCodeSize = 40;
            if (qrCodeSize <= 0) {
                JOptionPane.showMessageDialog(null, "Ukuran QR Code tidak valid!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BufferedImage qrCodeImage = QRCodeGenerator.generateQRCodeImage(qrDataString, qrCodeSize);
            if (qrCodeImage == null) {
                JOptionPane.showMessageDialog(null, "Gagal menghasilkan gambar QR Code.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            parameters.put("QRCodeImage", qrCodeImage);
            parameters.put("id_pesanan", idPesanan);

            UIManager.put("ComboBox.padding", new Insets(0, 10, 0, 0));
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
            viewer.setZoomRatio(2.0f);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghasilkan QR Code: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Error saat mengisi laporan Jasper: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void printAbsensi() {
        try {
            String reportPath = "src/com/radja/report/jasper/ReportAbsensi.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

            UIManager.put("ComboBox.padding", new Insets(0, 10, 0, 0));
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan absensi: " + e.getMessage());
        }
    }

    @Override
    public void saveAbsensiPDF() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            fileChooser.setSelectedFile(new File("Save Absensi Report " + saveDate + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".pdf")) {
                    savePath += ".pdf";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportAbsensi.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF absensi: " + e.getMessage());
        }
    }

    @Override
    public void saveAbsensiExcel() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            fileChooser.setSelectedFile(new File("Save Absensi Report " + saveDate + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".xlsx")) {
                    savePath += ".xlsx";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportAbsensi.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_FORMAT", "XLSX");
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);
                exporter.setConfiguration(configuration);
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan Excel absensi: " + e.getMessage());
        }
    }

    @Override
    public List<Pesanan> getPemasukanData(String filter) {
        List<Pesanan> list = new ArrayList<>();
        String sql = "SELECT p.id_pesanan, p.tanggal_pesanan, p.merk_mobil, p.nomor_plat, p.total_harga, p.status, " +
                     "p.id_pelanggan, pl.nama_pelanggan, p.id_layanan, p.no_antrian, p.insert_by, p.update_by, " +
                     "p.delete_by, p.insert_at, p.update_at, p.delete_at, p.is_delete " +
                     "FROM pesanan p JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan";
        
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            sql += " WHERE p.is_delete = FALSE";
        } else if (filter.equals("Terhapus")) {
            sql += " WHERE p.is_delete = TRUE";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Pesanan pesanan = new Pesanan();
                pesanan.setIdPesanan(rs.getString("id_pesanan"));
                pesanan.setTanggalPesanan(rs.getDate("tanggal_pesanan").toLocalDate());
                pesanan.setMerkMobil(rs.getString("merk_mobil"));
                pesanan.setNomorPlat(rs.getString("nomor_plat"));
                pesanan.setTotalHarga(rs.getDouble("total_harga"));
                pesanan.setStatus(rs.getString("status"));
                pesanan.setIdPelanggan(rs.getString("id_pelanggan"));
                pesanan.setNamaPelanggan(rs.getString("nama_pelanggan"));
                pesanan.setIdLayanan(rs.getInt("id_layanan"));
                pesanan.setNoAntrian(rs.getInt("no_antrian"));
                pesanan.setInsertBy(rs.getObject("insert_by", Integer.class));
                pesanan.setUpdateBy(rs.getObject("update_by", Integer.class));
                pesanan.setDeleteBy(rs.getObject("delete_by", Integer.class));
                pesanan.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                pesanan.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                pesanan.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                pesanan.setIsDelete(rs.getBoolean("is_delete"));
                list.add(pesanan);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mengambil data pemasukan: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Pesanan> searchPemasukanData(String keyword, String filter) {
        List<Pesanan> list = new ArrayList<>();
        String sql = "SELECT p.id_pesanan, p.tanggal_pesanan, p.merk_mobil, p.nomor_plat, p.total_harga, p.status, " +
                     "p.id_pelanggan, pl.nama_pelanggan, p.id_layanan, p.no_antrian, p.insert_by, p.update_by, " +
                     "p.delete_by, p.insert_at, p.update_at, p.delete_at, p.is_delete " +
                     "FROM pesanan p JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan " +
                     "WHERE (p.id_pesanan LIKE ? OR pl.nama_pelanggan LIKE ? OR p.merk_mobil LIKE ? OR p.nomor_plat LIKE ?)";
        
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            sql += " AND p.is_delete = FALSE";
        } else if (filter.equals("Terhapus")) {
            sql += " AND p.is_delete = TRUE";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");
            pstmt.setString(4, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pesanan pesanan = new Pesanan();
                    pesanan.setIdPesanan(rs.getString("id_pesanan"));
                    pesanan.setTanggalPesanan(rs.getDate("tanggal_pesanan").toLocalDate());
                    pesanan.setMerkMobil(rs.getString("merk_mobil"));
                    pesanan.setNomorPlat(rs.getString("nomor_plat"));
                    pesanan.setTotalHarga(rs.getDouble("total_harga"));
                    pesanan.setStatus(rs.getString("status"));
                    pesanan.setIdPelanggan(rs.getString("id_pelanggan"));
                    pesanan.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    pesanan.setIdLayanan(rs.getInt("id_layanan"));
                    pesanan.setNoAntrian(rs.getInt("no_antrian"));
                    pesanan.setInsertBy(rs.getObject("insert_by", Integer.class));
                    pesanan.setUpdateBy(rs.getObject("update_by", Integer.class));
                    pesanan.setDeleteBy(rs.getObject("delete_by", Integer.class));
                    pesanan.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    pesanan.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                    pesanan.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                    pesanan.setIsDelete(rs.getBoolean("is_delete"));
                    list.add(pesanan);
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mencari data pemasukan: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Pengeluaran> getPengeluaranData(String filter) {
        List<Pengeluaran> list = new ArrayList<>();
        String sql = 
            "SELECT id_biaya AS id_pengeluaran, NULL AS id_barang, deskripsi AS nama_item, 1 AS jumlah, " +
            "jumlah_biaya AS harga_total, 'Operasional' AS tipe_pengeluaran, tanggal_biaya AS tanggal_pengeluaran, " +
            "deskripsi AS keterangan, insert_by, insert_at, FALSE AS is_delete " +
            "FROM biaya_operasional " +
            "UNION ALL " +
            "SELECT id_log AS id_pengeluaran, id_barang, nama_barang AS nama_item, stok AS jumlah, " +
            "(stok * harga_satuan) AS harga_total, 'Pembelian' AS tipe_pengeluaran, " +
            "DATE(tanggal_masuk) AS tanggal_pengeluaran, CONCAT('Pembelian barang: ', nama_barang) AS keterangan, " +
            "id_users AS insert_by, tanggal_masuk AS insert_at, FALSE AS is_delete " +
            "FROM log_barang_masuk " +
            "ORDER BY tanggal_pengeluaran, id_pengeluaran";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            int biayaCount = 0;
            int barangCount = 0;
            while (rs.next()) {
                Pengeluaran pengeluaran = new Pengeluaran();
                pengeluaran.setIdPengeluaran(rs.getInt("id_pengeluaran"));
                pengeluaran.setIdBarang(rs.getInt("id_barang") == 0 ? null : rs.getInt("id_barang"));
                pengeluaran.setNamaItem(rs.getString("nama_item"));
                pengeluaran.setJumlah(rs.getInt("jumlah"));
                pengeluaran.setHargaTotal(rs.getDouble("harga_total"));
                pengeluaran.setTipePengeluaran(rs.getString("tipe_pengeluaran"));
                pengeluaran.setTanggalPengeluaran(rs.getDate("tanggal_pengeluaran") != null 
                        ? rs.getDate("tanggal_pengeluaran").toLocalDate() : null);
                pengeluaran.setKeterangan(rs.getString("keterangan"));
                pengeluaran.setInsertBy(rs.getObject("insert_by", Integer.class));
                pengeluaran.setInsertAt(rs.getTimestamp("insert_at") != null 
                        ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                pengeluaran.setIsDelete(rs.getBoolean("is_delete"));
                list.add(pengeluaran);
                if (pengeluaran.getTipePengeluaran().equals("Operasional")) {
                    biayaCount++;
                } else {
                    barangCount++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil data pengeluaran: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public List<Pengeluaran> searchPengeluaranData(String keyword, String filter) {
        List<Pengeluaran> list = new ArrayList<>();
        String sql = 
            "SELECT id_biaya AS id_pengeluaran, NULL AS id_barang, deskripsi AS nama_item, 1 AS jumlah, " +
            "jumlah_biaya AS harga_total, 'Operasional' AS tipe_pengeluaran, tanggal_biaya AS tanggal_pengeluaran, " +
            "deskripsi AS keterangan, insert_by, insert_at, FALSE AS is_delete " +
            "FROM biaya_operasional " +
            "WHERE deskripsi LIKE ? " +
            "UNION ALL " +
            "SELECT id_log AS id_pengeluaran, id_barang, nama_barang AS nama_item, stok AS jumlah, " +
            "(stok * harga_satuan) AS harga_total, 'Pembelian' AS tipe_pengeluaran, " +
            "DATE(tanggal_masuk) AS tanggal_pengeluaran, CONCAT('Pembelian barang: ', nama_barang) AS keterangan, " +
            "id_users AS insert_by, tanggal_masuk AS insert_at, FALSE AS is_delete " +
            "FROM log_barang_masuk " +
            "WHERE nama_barang LIKE ? " +
            "ORDER BY tanggal_pengeluaran, id_pengeluaran";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                int biayaCount = 0;
                int barangCount = 0;
                while (rs.next()) {
                    Pengeluaran pengeluaran = new Pengeluaran();
                    pengeluaran.setIdPengeluaran(rs.getInt("id_pengeluaran"));
                    pengeluaran.setIdBarang(rs.getInt("id_barang") == 0 ? null : rs.getInt("id_barang"));
                    pengeluaran.setNamaItem(rs.getString("nama_item"));
                    pengeluaran.setJumlah(rs.getInt("jumlah"));
                    pengeluaran.setHargaTotal(rs.getDouble("harga_total"));
                    pengeluaran.setTipePengeluaran(rs.getString("tipe_pengeluaran"));
                    pengeluaran.setTanggalPengeluaran(rs.getDate("tanggal_pengeluaran") != null 
                            ? rs.getDate("tanggal_pengeluaran").toLocalDate() : null);
                    pengeluaran.setKeterangan(rs.getString("keterangan"));
                    pengeluaran.setInsertBy(rs.getObject("insert_by", Integer.class));
                    pengeluaran.setInsertAt(rs.getTimestamp("insert_at") != null 
                            ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    pengeluaran.setIsDelete(rs.getBoolean("is_delete"));
                    list.add(pengeluaran);
                    if (pengeluaran.getTipePengeluaran().equals("Operasional")) {
                        biayaCount++;
                    } else {
                        barangCount++;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mencari data pengeluaran: " + e.getMessage(), e);
        }

        return list;
    }

    @Override
    public void printPemasukan() {
        try {
            String reportPath = "src/com/radja/report/jasper/ReportPemasukan.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

            UIManager.put("ComboBox.padding", new Insets(0, 10, 0, 0));
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan pemasukan: " + e.getMessage());
        }
    }

    @Override
    public void savePemasukanPDF() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            fileChooser.setSelectedFile(new File("Save Pemasukan Report " + saveDate + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".pdf")) {
                    savePath += ".pdf";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportPemasukan.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF pemasukan: " + e.getMessage());
        }
    }

    @Override
    public void savePemasukanExcel() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            fileChooser.setSelectedFile(new File("Save Pemasukan Report " + saveDate + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".xlsx")) {
                    savePath += ".xlsx";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportPemasukan.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_FORMAT", "XLSX");
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);
                exporter.setConfiguration(configuration);
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan Excel pemasukan: " + e.getMessage());
        }
    }

    @Override
    public void printPengeluaran() {
        try {
            String reportPath = "src/com/radja/report/jasper/ReportPengeluaran.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

            UIManager.put("ComboBox.padding", new Insets(0, 10, 0, 0));
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan pengeluaran: " + e.getMessage());
        }
    }

    @Override
    public void savePengeluaranPDF() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            fileChooser.setSelectedFile(new File("Save Pengeluaran Report " + saveDate + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".pdf")) {
                    savePath += ".pdf";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportPengeluaran.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF pengeluaran: " + e.getMessage());
        }
    }

    @Override
    public void savePengeluaranExcel() {
        try {
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            fileChooser.setSelectedFile(new File("Save Pengeluaran Report " + saveDate + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".xlsx")) {
                    savePath += ".xlsx";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "The file already exists. Do you want to overwrite it?",
                            "Overwrite Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportPengeluaran.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("REPORT_FORMAT", "XLSX");
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);
                exporter.setConfiguration(configuration);
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Report successfully saved to: " + savePath,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan Excel pengeluaran: " + e.getMessage());
        }
    }
    
    @Override
public List<BiayaOperasional> getBiayaOperasionalData(String filter) {
    List<BiayaOperasional> list = new ArrayList<>();
    String sql = "SELECT id_biaya, deskripsi, jumlah_biaya, tanggal_biaya, insert_by, insert_at, is_delete " +
                 "FROM biaya_operasional";
    if (filter.equals("Filter") || filter.equals("Aktif")) {
        sql += " WHERE is_delete = FALSE";
    } else if (filter.equals("Terhapus")) {
        sql += " WHERE is_delete = TRUE";
    }

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            BiayaOperasional biaya = new BiayaOperasional();
            biaya.setIdBiaya(rs.getInt("id_biaya"));
            biaya.setDeskripsi(rs.getString("deskripsi"));
            biaya.setJumlahBiaya(rs.getDouble("jumlah_biaya"));
            biaya.setTanggalBiaya(rs.getDate("tanggal_biaya").toLocalDate());
            biaya.setInsertBy(rs.getObject("insert_by", Integer.class));
            biaya.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
            biaya.setIsDelete(rs.getBoolean("is_delete"));
            list.add(biaya);
        }
    } catch (SQLException e) {
        throw new IllegalArgumentException("Gagal mengambil data biaya operasional: " + e.getMessage());
    }
    return list;
}

@Override
public List<BiayaOperasional> searchBiayaOperasionalData(String keyword, String filter) {
    List<BiayaOperasional> list = new ArrayList<>();
    String sql = "SELECT id_biaya, deskripsi, jumlah_biaya, tanggal_biaya, insert_by, insert_at, is_delete " +
                 "FROM biaya_operasional WHERE deskripsi LIKE ?";
    if (filter.equals("Filter") || filter.equals("Aktif")) {
        sql += " AND is_delete = FALSE";
    } else if (filter.equals("Terhapus")) {
        sql += " AND is_delete = TRUE";
    }

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, "%" + keyword + "%");
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                BiayaOperasional biaya = new BiayaOperasional();
                biaya.setIdBiaya(rs.getInt("id_biaya"));
                biaya.setDeskripsi(rs.getString("deskripsi"));
                biaya.setJumlahBiaya(rs.getDouble("jumlah_biaya"));
                biaya.setTanggalBiaya(rs.getDate("tanggal_biaya").toLocalDate());
                biaya.setInsertBy(rs.getObject("insert_by", Integer.class));
                biaya.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                biaya.setIsDelete(rs.getBoolean("is_delete"));
                list.add(biaya);
            }
        }
    } catch (SQLException e) {
        throw new IllegalArgumentException("Gagal mencari data biaya operasional: " + e.getMessage());
    }
    return list;
}

   @Override
    public void printPengeluaranPeriode(Date startDate, Date endDate) {
        try {
            String reportPath = "src/com/radja/report/jasper/ReportPengeluaranPeriode.jasper";
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan pengeluaran periode: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Failed to print pengeluaran periode: " + e.getMessage(), e);
        }
    }

    @Override
    public void savePengeluaranPeriodePDF(Date startDate, Date endDate) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(new Date());

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
            fileChooser.setSelectedFile(new File("Pengeluaran_Report_" + saveDate + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".pdf")) {
                    savePath += ".pdf";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "File sudah ada. Apakah Anda ingin menimpa?",
                            "Konfirmasi Timpa", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportPengeluaranPeriode.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("startDate", startDate);
                parameters.put("endDate", endDate);
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Laporan berhasil disimpan ke: " + savePath,
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan PDF pengeluaran periode: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Failed to save pengeluaran periode PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public void savePengeluaranPeriodeExcel(Date startDate, Date endDate) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(new Date());

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Report");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
            fileChooser.setSelectedFile(new File("Pengeluaran_Report_" + saveDate + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String savePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!savePath.toLowerCase().endsWith(".xlsx")) {
                    savePath += ".xlsx";
                }

                File fileToSave = new File(savePath);
                if (fileToSave.exists()) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "File sudah ada. Apakah Anda ingin menimpa?",
                            "Konfirmasi Timpa", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String reportPath = "src/com/radja/report/jasper/ReportPengeluaranPeriode.jasper";
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("startDate", startDate);
                parameters.put("endDate", endDate);
                JasperPrint print = JasperFillManager.fillReport(reportPath, parameters, conn);

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);
                exporter.setConfiguration(configuration);
                exporter.exportReport();

                JOptionPane.showMessageDialog(null, "Laporan berhasil disimpan ke: " + savePath,
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (JRException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan Excel pengeluaran periode: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Failed to save pengeluaran periode Excel: " + e.getMessage(), e);
        }
    }

    @Override
public List<Pengeluaran> getPengeluaranDataByDateRange(Date startDate, Date endDate, String filter) {
    List<Pengeluaran> list = new ArrayList<>();
    String isDeleteClause = filter.equals("Terhapus") ? "is_delete = TRUE" : "is_delete = FALSE";
    String sql = 
        "SELECT id_biaya AS id_pengeluaran, NULL AS id_barang, deskripsi AS nama_item, 1 AS jumlah, " +
        "jumlah_biaya AS harga_total, 'Operasional' AS tipe_pengeluaran, tanggal_biaya AS tanggal_pengeluaran, " +
        "deskripsi AS keterangan, insert_by, insert_at, is_delete " +
        "FROM biaya_operasional " +
        "WHERE " + isDeleteClause + " AND tanggal_biaya BETWEEN ? AND ?" +
        " UNION ALL " +
        "SELECT pb.id_pengeluaran, pb.id_barang, b.nama_barang AS nama_item, pb.jumlah, pb.harga_total, " +
        "pb.tipe_pengeluaran, pb.tanggal_pengeluaran, pb.keterangan, pb.insert_by, pb.insert_at, pb.is_delete " +
        "FROM pengeluaran_barang pb " +
        "JOIN barang b ON pb.id_barang = b.id_barang " +
        "WHERE pb.tipe_pengeluaran = 'Pembelian' AND pb." + isDeleteClause +
        " AND pb.tanggal_pengeluaran BETWEEN ? AND ?" +
        " ORDER BY tanggal_pengeluaran, id_pengeluaran";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
        pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
        pstmt.setDate(3, new java.sql.Date(startDate.getTime()));
        pstmt.setDate(4, new java.sql.Date(endDate.getTime()));
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Pengeluaran pengeluaran = new Pengeluaran();
                pengeluaran.setIdPengeluaran(rs.getInt("id_pengeluaran"));
                pengeluaran.setIdBarang(rs.getInt("id_barang") == 0 ? null : rs.getInt("id_barang"));
                pengeluaran.setNamaItem(rs.getString("nama_item"));
                pengeluaran.setJumlah(rs.getInt("jumlah"));
                pengeluaran.setHargaTotal(rs.getDouble("harga_total"));
                pengeluaran.setTipePengeluaran(rs.getString("tipe_pengeluaran"));
                pengeluaran.setTanggalPengeluaran(rs.getDate("tanggal_pengeluaran") != null 
                        ? rs.getDate("tanggal_pengeluaran").toLocalDate() : null);
                pengeluaran.setKeterangan(rs.getString("keterangan"));
                pengeluaran.setInsertBy(rs.getObject("insert_by", Integer.class));
                pengeluaran.setInsertAt(rs.getTimestamp("insert_at") != null 
                        ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                pengeluaran.setIsDelete(rs.getBoolean("is_delete"));
                list.add(pengeluaran);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Failed to retrieve pengeluaran data by date range: " + e.getMessage(), e);
    }

    return list;
}

    @Override
public List<Pengeluaran> searchPengeluaranDataByDateRange(String keyword, Date startDate, Date endDate, String filter) {
    List<Pengeluaran> list = new ArrayList<>();
    String isDeleteClause = filter.equals("Terhapus") ? "is_delete = TRUE" : "is_delete = FALSE";
    String sql = 
        "SELECT id_biaya AS id_pengeluaran, NULL AS id_barang, deskripsi AS nama_item, 1 AS jumlah, " +
        "jumlah_biaya AS harga_total, 'Operasional' AS tipe_pengeluaran, tanggal_biaya AS tanggal_pengeluaran, " +
        "deskripsi AS keterangan, insert_by, insert_at, is_delete " +
        "FROM biaya_operasional " +
        "WHERE " + isDeleteClause + " AND tanggal_biaya BETWEEN ? AND ? AND deskripsi LIKE ?" +
        " UNION ALL " +
        "SELECT pb.id_pengeluaran, pb.id_barang, b.nama_barang AS nama_item, pb.jumlah, pb.harga_total, " +
        "pb.tipe_pengeluaran, pb.tanggal_pengeluaran, pb.keterangan, pb.insert_by, pb.insert_at, pb.is_delete " +
        "FROM pengeluaran_barang pb " +
        "JOIN barang b ON pb.id_barang = b.id_barang " +
        "WHERE pb.tipe_pengeluaran = 'Pembelian' AND pb." + isDeleteClause +
        " AND pb.tanggal_pengeluaran BETWEEN ? AND ? AND (b.nama_barang LIKE ? OR pb.keterangan LIKE ? OR pb.tipe_pengeluaran LIKE ?)" +
        " ORDER BY tanggal_pengeluaran, id_pengeluaran";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
        pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
        pstmt.setString(3, "%" + keyword + "%");
        pstmt.setDate(4, new java.sql.Date(startDate.getTime()));
        pstmt.setDate(5, new java.sql.Date(endDate.getTime()));
        pstmt.setString(6, "%" + keyword + "%");
        pstmt.setString(7, "%" + keyword + "%");
        pstmt.setString(8, "%" + keyword + "%");
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Pengeluaran pengeluaran = new Pengeluaran();
                pengeluaran.setIdPengeluaran(rs.getInt("id_pengeluaran"));
                pengeluaran.setIdBarang(rs.getInt("id_barang") == 0 ? null : rs.getInt("id_barang"));
                pengeluaran.setNamaItem(rs.getString("nama_item"));
                pengeluaran.setJumlah(rs.getInt("jumlah"));
                pengeluaran.setHargaTotal(rs.getDouble("harga_total"));
                pengeluaran.setTipePengeluaran(rs.getString("tipe_pengeluaran"));
                pengeluaran.setTanggalPengeluaran(rs.getDate("tanggal_pengeluaran") != null 
                        ? rs.getDate("tanggal_pengeluaran").toLocalDate() : null);
                pengeluaran.setKeterangan(rs.getString("keterangan"));
                pengeluaran.setInsertBy(rs.getObject("insert_by", Integer.class));
                pengeluaran.setInsertAt(rs.getTimestamp("insert_at") != null 
                        ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                pengeluaran.setIsDelete(rs.getBoolean("is_delete"));
                list.add(pengeluaran);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Failed to search pengeluaran data by date range: " + e.getMessage(), e);
    }

    return list;
}
    
@Override
public List<Pemasukan> getPemasukanList() {
    List<Pemasukan> list = new ArrayList<>();
    String sql = "SELECT p.id_pesanan, l.nama_layanan, l.harga " +
                 "FROM pesanan p " +
                 "JOIN layanan l ON p.id_layanan = l.id_layanan " +
                 "WHERE p.status = 'Selesai' " +
                 "ORDER BY p.id_pesanan DESC";

    try (PreparedStatement st = conn.prepareStatement(sql);
         ResultSet rs = st.executeQuery()) {

        while (rs.next()) {
            Pemasukan p = new Pemasukan(
                rs.getString("id_pesanan"),
                rs.getString("nama_layanan"),
                rs.getDouble("harga")
            );
            list.add(p);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}




@Override
public List<Pemasukan> searchPemasukan(String keyword) {
    List<Pemasukan> list = new ArrayList<>();
    String sql = "SELECT p.id_pesanan, l.nama_layanan, l.harga " +
                 "FROM pesanan p " +
                 "JOIN layanan l ON p.id_layanan = l.id_layanan " +
                 "WHERE p.status = 'Selesai' " +
                 "AND (p.id_pesanan LIKE ? OR l.nama_layanan LIKE ?) " +
                 "ORDER BY p.id_pesanan DESC";

    try (PreparedStatement st = conn.prepareStatement(sql)) {
        st.setString(1, "%" + keyword + "%");
        st.setString(2, "%" + keyword + "%");
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            Pemasukan p = new Pemasukan(
                rs.getString("id_pesanan"),
                rs.getString("nama_layanan"),
                rs.getDouble("harga")
            );
            list.add(p);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

}