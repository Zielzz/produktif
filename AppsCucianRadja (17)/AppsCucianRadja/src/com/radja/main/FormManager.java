package com.radja.main;

import com.radja.auth.FormLogin;
import com.radja.form.FormDashboard;
import com.radja.model.User;
import javax.swing.JFrame;
import raven.modal.Drawer;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.utils.UndoRedo;

public class FormManager {

    public static final UndoRedo<Form> FORMS = new UndoRedo<>();
    private static JFrame frame;
    private static MainForm mainForm;
    private static FormLogin formLogin;
    private static User loggedInUser;

    public static void install(JFrame f) {
        if (f == null) {
            throw new IllegalArgumentException("JFrame cannot be null");
        }
        frame = f;
        logout();
    }

    public static void showForm(Form form) {
        if (frame == null) {
            throw new IllegalStateException("FormManager not initialized. Call install() first.");
        }
        if (form != FORMS.getCurrent()) {
            FORMS.add(form);
            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
        }
    }

    public static void undo() {
        if (frame == null) {
            throw new IllegalStateException("FormManager not initialized. Call install() first.");
        }
        if (FORMS.isUndoAble()) {
            Form form = FORMS.undo();
            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
            Drawer.setSelectedItemClass(form.getClass());
        }
    }

    public static void redo() {
        if (frame == null) {
            throw new IllegalStateException("FormManager not initialized. Call install() first.");
        }
        if (FORMS.isRedoAble()) {
            Form form = FORMS.redo();
            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
            Drawer.setSelectedItemClass(form.getClass());
        }
    }

    public static void login(User user) {
        if (frame == null) {
            throw new IllegalStateException("FormManager not initialized. Call install() first.");
        }
        loggedInUser = user;

        Drawer.installDrawer(frame, new com.radja.main.Menu());
        Drawer.setVisible(true);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(getMainForm());

        Drawer.setSelectedItemClass(FormDashboard.class);
        frame.repaint();
        frame.revalidate();
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void logout() {
        if (frame == null) {
            throw new IllegalStateException("FormManager not initialized. Call install() first.");
        }
        if (loggedInUser == null) {
            Drawer.installDrawer(frame, new com.radja.main.Menu());
        }
        Drawer.setVisible(false);
        frame.getContentPane().removeAll();
        FormLogin login = getLogin();
        login.formCheck();
        frame.getContentPane().add(login);
        FORMS.clear();
        frame.repaint();
        frame.revalidate();
    }

    public static JFrame getFrame() {
        return frame;
    }

    private static MainForm getMainForm() {
        if (mainForm == null) {
            mainForm = new MainForm();
        }
        return mainForm;
    }

    private static FormLogin getLogin() {
        if (formLogin == null) {
            formLogin = new FormLogin(frame); // Teruskan frame ke FormLogin
        }
        return formLogin;
    }

    public static void showAbout() {
        if (frame == null) {
            throw new IllegalStateException("FormManager not initialized. Call install() first.");
        }
        ModalDialog.showModal(frame, new SimpleModalBorder(new com.radja.util.About(), "About"),
                ModalDialog.createOption().setAnimationEnabled(false)
        );
    }
}