package com.radja.main;

import com.radja.model.User;
import com.formdev.flatlaf.FlatClientProperties;
import com.radja.form.FormBarang;
import com.radja.form.FormBiayaOperasional;
import com.radja.form.FormDashboard;
import com.radja.form.FormFasilitas;
import com.radja.form.FormKaryawan;
import com.radja.form.FormLayanan;
import com.radja.form.FormOpnameStok;
import com.radja.form.FormPelanggan;
import com.radja.form.FormPesananDalamAntrian;
import com.radja.form.FormPesananDalamProses;
import com.radja.form.FormPesananMenungguAntrian;
import com.radja.form.FormPesananSelesai;
import com.radja.form.FormPesananSemua;
import com.radja.form.FormTransaksi;
import com.radja.form.FormUser;
import com.radja.form.input.FormChangePassword;
import com.radja.form.input.FormUserProfile;
import com.radja.report.ReportAbsensi;
import com.radja.report.ReportBarang;
import com.radja.report.ReportKaryawan;
import com.radja.report.ReportLabaRugi;
import com.radja.report.ReportLayanan;
import com.radja.report.ReportPelanggan;
import com.radja.report.ReportPemasukan;
import com.radja.report.ReportPengeluaran;
import com.radja.report.ReportPesananPeriode;
import com.radja.util.MessageModal;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import raven.extras.AvatarIcon;
import raven.modal.ModalDialog;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.data.Item;
import raven.modal.drawer.data.MenuItem;
import raven.modal.drawer.menu.MenuAction;
import raven.modal.drawer.menu.MenuEvent;
import raven.modal.drawer.menu.MenuOption;
import raven.modal.drawer.menu.MenuStyle;
import raven.modal.drawer.renderer.DrawerStraightDotLineStyle;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.LightDarkButtonFooter;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;
import raven.modal.listener.ModalCallback;
import raven.modal.listener.ModalController;
import raven.modal.option.Option;

public class Menu extends SimpleDrawerBuilder{

    private final int SHADOW_SIZE = 0;
    
    public Menu() {
        super(createSimpleMenuOption());
        LightDarkButtonFooter lightDarkButtonFooter = (LightDarkButtonFooter) footer;
        lightDarkButtonFooter.removeAll();
        setColorTitle();
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        AvatarIcon icon = new AvatarIcon(getClass().getResource("/com/radja/icon/Logo.png"), 50, 50, 0f);
        icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        
        changeAvatarIconBorderColor(icon);

        UIManager.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("lookAndFeel")) {
                changeAvatarIconBorderColor(icon);
            }
        });
        
        return new SimpleHeaderData()
                .setIcon(icon)
                .setTitle("RADJA CAR WASH")
                .setDescription("Excellent Auto Washing & Detailing");
    }
    
    private void changeAvatarIconBorderColor(AvatarIcon icon) {
        icon.setBorderColor(new AvatarIcon.BorderColor(UIManager.getColor("Component.accentColor"), 0.7f));
    }
    
    private void setColorTitle() {
        if (header instanceof JComponent) {
            for (Component comp : ((JComponent) header).getComponents()) {
                if (comp instanceof JPanel) {
                    int labelCount = 0;

                    for (Component subComp : ((JPanel) comp).getComponents()) {
                        if (subComp instanceof JLabel) {
                            if (labelCount == 0) {
                                ((JLabel) subComp).setForeground(Color.WHITE);
                            } else if (labelCount == 1) {
                                ((JLabel) subComp).setForeground(Color.GRAY);
                            }
                            labelCount++;
                        }
                    }
                }
            }
        }
    }


    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData()
                .setTitle("Swing Modal Dialog")
                .setDescription("Version " + Main.APP_VERSION);
    }
    
   public static MenuOption createSimpleMenuOption() {
    User loggedInUser = FormManager.getLoggedInUser();
    String role = loggedInUser != null ? loggedInUser.getRole() : "Guest";
    
    // create simple menu option
    MenuOption simpleMenuOption = new MenuOption();
    
    MenuItem[] adminMenu = new MenuItem[] {
    new Item.Label("MENU UTAMA"),
    new Item("Dashboard", "dashboard.svg", FormDashboard.class),
    new Item("Pesanan", "pesanan.svg")
        .subMenu("Semua", FormPesananSemua.class)
        .subMenu("Pesanan Baru", FormPesananMenungguAntrian.class)
        .subMenu("Dalam Antrian", FormPesananDalamAntrian.class)
        .subMenu("Dalam Pencucian", FormPesananDalamProses.class)
        .subMenu("Selesai", FormPesananSelesai.class),
    new Item("Pelanggan", "pelanggan.svg", FormPelanggan.class),
    new Item("Transaksi", "transaksi.svg", FormTransaksi.class),
    new Item("Laporan", "laporan.svg")
        .subMenu("Laporan Barang", ReportBarang.class)
        .subMenu("Laporan Pesanan", ReportPesananPeriode.class)
        .subMenu("Laporan Layanan", ReportLayanan.class)
        .subMenu("Laporan Pelanggan", ReportPelanggan.class)
        .subMenu("Laporan Karyawan", ReportKaryawan.class)
        .subMenu("Laporan Absensi", ReportAbsensi.class)
        .subMenu("Laporan Pemasukan", ReportPemasukan.class)
        .subMenu("Laporan Pengeluaran", ReportPengeluaran.class)
        .subMenu("Laporan Keuntungan dan Kerugian", ReportLabaRugi.class),
    new Item.Label("MASTER"),
    new Item("Karyawan", "karyawan.svg", FormKaryawan.class),
    new Item("Barang", "barang.svg", FormBarang.class),
    new Item("Opname Stok", "opname1.svg", FormOpnameStok.class),
    new Item("Fasilitas", "fasilitas.svg", FormFasilitas.class),
    new Item("Layanan", "layanan.svg", FormLayanan.class),
    new Item("Biaya Operasional", "biayaO.svg", FormBiayaOperasional.class),
    new Item("Management User", "management_user.svg")
        .subMenu("Data User", FormUser.class)
        .subMenu("Profile", FormUserProfile.class)
        .subMenu("Change Password", FormChangePassword.class),
    new Item("Logout", "logout.svg")
};

    MenuItem[] userMenu = new MenuItem[] {
        new Item.Label("MENU UTAMA"),
        new Item("Dashboard", "dashboard.svg", FormDashboard.class),
        new Item("Pesanan", "pesanan.svg")
            .subMenu("Semua", FormPesananSemua.class)
            .subMenu("Pesanan Baru", FormPesananMenungguAntrian.class)
            .subMenu("Dalam Antrian", FormPesananDalamAntrian.class)
            .subMenu("Dalam Pencucian", FormPesananDalamProses.class)
            .subMenu("Selesai", FormPesananSelesai.class),
        new Item("Pelanggan", "pelanggan.svg", FormPelanggan.class),
        new Item("Transaksi", "transaksi.svg", FormTransaksi.class),
        new Item("Laporan", "laporan.svg")
            .subMenu("Laporan Barang", ReportBarang.class)
            .subMenu("Laporan Pesanan", ReportPesananPeriode.class)
            .subMenu("Laporan Layanan", ReportLayanan.class)
            .subMenu("Laporan Pelanggan", ReportPelanggan.class)
            .subMenu("Laporan Karyawan", ReportKaryawan.class)
            .subMenu("Laporan Absensi", ReportAbsensi.class), // Menambahkan Laporan Absensi
        new Item("Management User", "management_user.svg")
            .subMenu("Profile", FormUserProfile.class)
            .subMenu("Change Password", FormChangePassword.class),
        new Item("Logout", "logout.svg")
    };

    // Menu list based on role
    MenuItem[] menuToUse;
    switch (role) {
        case "Owner":
            menuToUse = adminMenu;
            break;
        case "Kasir":
            menuToUse = userMenu;
            break;
        default:
            menuToUse = new MenuItem[0];
            break;
    }
    
    simpleMenuOption.setMenuStyle(new MenuStyle() {

        @Override
        public void styleMenu(JComponent component) {
            component.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
        }

        @Override
        public void styleMenuItem(JButton menu, int[] index, boolean isMainItem) {
            menu.setForeground(Color.WHITE);
            menu.putClientProperty("Button.selectedBackground", Color.BLUE);
        }
    });
    
    simpleMenuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerStraightDotLineStyle());
    simpleMenuOption.setMenuItemAutoSelectionMode(MenuOption.MenuItemAutoSelectionMode.SELECT_SUB_MENU_LEVEL);
    simpleMenuOption.addMenuEvent(new MenuEvent() {
        @Override
    public void selected(MenuAction action, int[] index) {
    Class<?> itemClass = action.getItem().getItemClass();
    int i = index[0];
    
    if (role.equals("Owner")) {
        if (i == 12) { // Item Logout untuk Owner
            action.consume();
            showLogoutConfirmation(action);
            return;
        }
        handleFormAction(itemClass, action);
    } else if (role.equals("Kasir")) {
        if (i == 6) { // Item Logout untuk Kasir
            action.consume();
            showLogoutConfirmation(action);
            return;
        }
        handleFormAction(itemClass, action);
    }
}

// Tambahkan method baru untuk menampilkan konfirmasi logout
    private void showLogoutConfirmation(MenuAction action) {
    MessageModal modal = new MessageModal(
        MessageModal.Type.WARNING,
        "Apakah Anda yakin ingin keluar?",
        "Konfirmasi Logout",
        MessageModal.YES_NO_OPTION,
        new ModalCallback() {
            @Override
            public void action(ModalController controller, int action) {
                if (action == MessageModal.YES_OPTION) {
                    FormManager.logout();
                }
            }
        }
    );
    
    ModalDialog.showModal(
        FormManager.getFrame(), 
        modal,
        ModalDialog.createOption().setAnimationEnabled(false)
    );
}
        
        private void handleFormAction(Class<?> itemClass, MenuAction action) {
            if (itemClass == null || !Form.class.isAssignableFrom(itemClass)) {
                action.consume();
                return;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Form> formClass = (Class<? extends Form>) itemClass;
            FormManager.showForm(AllForms.getForm(formClass));
        }

    });

    simpleMenuOption.setMenus(menuToUse)
            .setBaseIconPath("com/radja/icon")
            .setIconScale(0.45f);
    
    return simpleMenuOption;
}

    
    @Override
    public int getDrawerWidth() {
        return 270 + SHADOW_SIZE;
    }

    @Override
    public int getDrawerCompactWidth() {
        return 80 + SHADOW_SIZE;
    }

    @Override
    public int getOpenDrawerAt() {
        return 1000;
    }

    @Override
    public Option getOption() {
        Option option = super.getOption();
        option.setOpacity(0.3f);
        option.getBorderOption()
                .setShadowSize(new Insets(0, 0, 0, SHADOW_SIZE));
        return option;
    }

    @Override
    public boolean openDrawerAtScale() {
        return false;
    }

    @Override
    public void build(DrawerPanel drawerPanel) {
        drawerPanel.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
    }
    
    private static String getDrawerBackgroundStyle() {
        return "" +
                //background sidebar
                "[light]background:rgb(49, 54, 73);";
    }
}