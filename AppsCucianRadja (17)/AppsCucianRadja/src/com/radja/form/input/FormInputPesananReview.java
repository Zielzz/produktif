package com.radja.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.radja.main.FormManager;
import com.radja.dao.PelangganDAO;
import com.radja.dao.PesananDAO;
import com.radja.model.Layanan;
import com.radja.model.Pelanggan;
import com.radja.model.Pesanan;
import com.radja.model.User;
import com.radja.service.ServicePelanggan;
import com.radja.service.ServicePesanan;
import static com.radja.util.AlertUtils.getOptionAlert;
import java.awt.Color;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputPesananReview extends JPanel {

    private JLabel lbNama;
    private JLabel lbTelepon;
    private JLabel lbMerk;
    private JLabel lbNoPlat;
    private JLabel lbLayanan;
    private JLabel lbHargaLayanan;
    private JLabel lbTanggalOrder;
    private JLabel lbTotalHarga;
    
    private JLabel lbNamaResult;
    private JLabel lbTeleponResult;
    private JLabel lbMerkResult;
    private JLabel lbNoPlatResult;
    private JLabel lbLayananResult;
    private JLabel lbHargaLayananResult;
    private JLabel lbTanggalOrderResult;
    private JLabel lbTotalHargaResult;
    
    private final ServicePelanggan servisPelanggan = new PelangganDAO();
    private final ServicePesanan servisPesanan = new PesananDAO();
    private final User loggedInUser;
    private Pelanggan pelanggan;
    private Pesanan pesanan;
    private String idPesanan;
            
    public FormInputPesananReview(Pelanggan pelanggan, Pesanan pesanan) {
        this.loggedInUser = FormManager.getLoggedInUser();
        this.pelanggan = pelanggan != null ? pelanggan : new Pelanggan();
        this.pesanan = pesanan != null ? pesanan : new Pesanan();
        
        init();
        generateIdPesanan();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap, width 300"));
        
        lbNama = new JLabel("Nama");
        lbNama.setForeground(Color.GRAY);
        lbTelepon = new JLabel("No. Hp");
        lbTelepon.setForeground(Color.GRAY);
        lbMerk = new JLabel("Merk Kendaraan");
        lbMerk.setForeground(Color.GRAY);
        lbNoPlat = new JLabel("Nomor Plat");
        lbNoPlat.setForeground(Color.GRAY);
        lbLayanan = new JLabel("Layanan");
        lbLayanan.setForeground(Color.GRAY);
        lbHargaLayanan = new JLabel("Harga Layanan");
        lbHargaLayanan.setForeground(Color.GRAY);
        lbTanggalOrder = new JLabel("Tanggal Order");
        lbTanggalOrder.setForeground(Color.GRAY);
        lbTotalHarga = new JLabel("Total Harga");
        lbTotalHarga.putClientProperty(FlatClientProperties.STYLE, "font:bold 16;");
        
        lbNamaResult = new JLabel();
        lbTeleponResult = new JLabel();
        lbMerkResult = new JLabel();
        lbNoPlatResult = new JLabel();
        lbLayananResult = new JLabel();
        lbHargaLayananResult = new JLabel();
        lbTotalHargaResult = new JLabel();
        lbTotalHargaResult.putClientProperty(FlatClientProperties.STYLE, "font:bold 16;");
        
        lbTanggalOrderResult = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        add(createSeparator(), "span, growx, height 2!, gapy 0 10");
        add(lbNama, "span, split 2, align left, hmin 30");
        add(lbNamaResult, "hmin 30, gapx push n");
        add(lbTelepon, "span, split 2, align left, hmin 30");
        add(lbTeleponResult, "hmin 30, gapx push n");
        add(lbMerk, "span, split 2, align left, hmin 30");
        add(lbMerkResult, "hmin 30, gapx push n");
        add(lbNoPlat, "span, split 2, align left, hmin 30");
        add(lbNoPlatResult, "hmin 30, gapx push n");
        add(lbLayanan, "span, split 2, align left, hmin 30");
        add(lbLayananResult, "hmin 30, gapx push n");
        add(lbHargaLayanan, "span, split 2, align left, hmin 30");
        add(lbHargaLayananResult, "hmin 30, gapx push n");
        add(lbTanggalOrder, "span, split 2, align left, hmin 30");
        add(lbTanggalOrderResult, "hmin 30, gapx push n");
        add(lbTotalHarga, "span, split 2, align left, hmin 30, gapy 10");
        add(lbTotalHargaResult, "hmin 30, gapx push n");
        
        add(createSeparator(), "span, growx, height 2!, gapy 10");
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }
    
    public void updateData() {
        lbNamaResult.setText(pelanggan.getNamaPelanggan() != null ? pelanggan.getNamaPelanggan() : "");
        lbTeleponResult.setText(pelanggan.getTelepon() != null ? pelanggan.getTelepon() : "");
        
        lbMerkResult.setText(pesanan.getMerkMobil() != null ? pesanan.getMerkMobil() : "");
        lbNoPlatResult.setText(pesanan.getNomorPlat() != null ? pesanan.getNomorPlat() : "");
        lbLayananResult.setText(pesanan.getLayanan() != null && pesanan.getLayanan().getNamaLayanan() != null ? pesanan.getLayanan().getNamaLayanan() : "");
        
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String hargaLayanan = pesanan.getLayanan() != null ? df.format(pesanan.getLayanan().getHarga()) : "0.00";
        lbHargaLayananResult.setText("Rp." + hargaLayanan);
        
        String totalHarga = df.format(pesanan.getTotalHarga());
        lbTotalHargaResult.setText("Rp." + totalHarga);
        
        System.out.println("ID Layanan: " + (pesanan.getLayanan() != null ? pesanan.getLayanan().getIdLayanan() : "null"));
    }
    
    private void generateIdPesanan() {
        idPesanan = servisPesanan.generateIdPesanan();
        System.out.println("ID Pesanan: " + idPesanan);
    }
    
    private boolean validateInput() {
        if (pelanggan.getNamaPelanggan() == null || pelanggan.getNamaPelanggan().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nama pelanggan tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (pelanggan.getTelepon() == null || pelanggan.getTelepon().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nomor telepon tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (pelanggan.getTelepon().trim().length() <= 10) {
            Toast.show(this, Toast.Type.INFO, "Nomor telepon harus lebih dari 10 digit", getOptionAlert());
            return false;
        }
        if (pelanggan.getAlamat() == null || pelanggan.getAlamat().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Alamat tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (pesanan.getMerkMobil() == null || pesanan.getMerkMobil().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Merk kendaraan tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (pesanan.getNomorPlat() == null || pesanan.getNomorPlat().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nomor plat kendaraan tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (pesanan.getLayanan() == null || pesanan.getLayanan().getNamaLayanan() == null || 
            pesanan.getLayanan().getNamaLayanan().trim().isEmpty() || 
            "Pilih Layanan".equals(pesanan.getLayanan().getNamaLayanan())) {
            Toast.show(this, Toast.Type.INFO, "Layanan harus dipilih", getOptionAlert());
            return false;
        }
        return true;
    }
    
    public void insertData() {
        try {
            if (!validateInput()) {
                throw new IllegalArgumentException("Validasi data gagal");
            }

            Pelanggan modelPelanggan = new Pelanggan();
            modelPelanggan.setIdPelanggan(pelanggan.getIdPelanggan());
            modelPelanggan.setNamaPelanggan(pelanggan.getNamaPelanggan() != null && !pelanggan.getNamaPelanggan().trim().isEmpty() ? pelanggan.getNamaPelanggan() : "Pelanggan");
            modelPelanggan.setTelepon(pelanggan.getTelepon());
            modelPelanggan.setAlamat(pelanggan.getAlamat() != null ? pelanggan.getAlamat() : "");
            modelPelanggan.setMemberStatus("tidak aktif");
            modelPelanggan.setInsertBy(loggedInUser.getIdUser());

            Pesanan modelPesanan = new Pesanan();
            modelPesanan.setIdPesanan(idPesanan);
            modelPesanan.setTanggalPesanan(LocalDate.parse(lbTanggalOrderResult.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            modelPesanan.setMerkMobil(lbMerkResult.getText());
            modelPesanan.setNomorPlat(lbNoPlatResult.getText());
            
            String totalHargaText = lbTotalHargaResult.getText().replaceAll("[^0-9,]", "").replace(",", "");
            modelPesanan.setTotalHarga(Double.parseDouble(totalHargaText));
            
            modelPesanan.setStatus("Menunggu Antrian");
            modelPesanan.setIdLayanan(pesanan.getLayanan().getIdLayanan());
            modelPesanan.setInsertBy(loggedInUser.getIdUser());
            
            modelPesanan.setPelanggan(modelPelanggan);

            servisPesanan.insertData(modelPesanan);
            servisPesanan.updateStok(idPesanan);

            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully submitted", getOptionAlert());
            ModalDialog.closeModal("form input");
        } catch (Exception e) {
            Toast.show(this, Toast.Type.ERROR, "Failed to submit data: " + e.getMessage(), getOptionAlert());
            e.printStackTrace();
        }
    }
}