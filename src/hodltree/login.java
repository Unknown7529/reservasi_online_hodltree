package hodltree;
import java.sql.*;
import javax.swing.*;
import java.awt.*;

public class login extends javax.swing.JFrame {
int xMouse,yMouse;
int rMouse,sMouse;
    public login() {
        initComponents();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jColorChooser1 = new javax.swing.JColorChooser();
        componentMoverUtil1 = new com.k33ptoo.utils.ComponentMoverUtil();
        app1 = new com.k33ptoo.App();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCalendarTheme1 = new com.toedter.plaf.JCalendarTheme();
        kGradientPanel2 = new com.k33ptoo.components.KGradientPanel();
        txtusername = new javax.swing.JTextField();
        txtpassword = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btn_login = new com.k33ptoo.components.KButton();
        kButton2 = new com.k33ptoo.components.KButton();
        kButton3 = new com.k33ptoo.components.KButton();
        Pascekbok = new javax.swing.JCheckBox();
        kGradientPanel1 = new com.k33ptoo.components.KGradientPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("APBIKAN 1.0");
        setBackground(new java.awt.Color(128, 199, 192));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Franklin Gothic Book", 3, 10)); // NOI18N
        setForeground(new java.awt.Color(255, 102, 102));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kGradientPanel2.setkEndColor(new java.awt.Color(0, 0, 51));
        kGradientPanel2.setkStartColor(new java.awt.Color(0, 204, 0));
        kGradientPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtusername.setBackground(new Color(0,0,0,0));
        txtusername.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        txtusername.setForeground(new java.awt.Color(255, 255, 255));
        txtusername.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txtusername.setCaretColor(new java.awt.Color(255, 255, 255));
        txtusername.setDoubleBuffered(true);
        txtusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtusernameActionPerformed(evt);
            }
        });
        kGradientPanel2.add(txtusername, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 270, 20));

        txtpassword.setBackground(new Color(0,0,0,0));
        txtpassword.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        txtpassword.setForeground(new java.awt.Color(255, 255, 255));
        txtpassword.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txtpassword.setDoubleBuffered(true);
        txtpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpasswordActionPerformed(evt);
            }
        });
        kGradientPanel2.add(txtpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 310, 270, 20));

        jLabel2.setFont(new java.awt.Font("Rockwell Condensed", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("(APLIKASI RESERVASI CAFE)");
        kGradientPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 230, -1));

        jLabel3.setFont(new java.awt.Font("Sitka Display", 3, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Username :");
        kGradientPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 150, -1));

        jLabel4.setFont(new java.awt.Font("Sitka Display", 3, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Password :");
        kGradientPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 130, -1));

        jLabel5.setFont(new java.awt.Font("Rockwell Condensed", 3, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("HODL TREE APP");
        kGradientPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 170, -1));

        btn_login.setText("LOGIN");
        btn_login.setkBackGroundColor(new java.awt.Color(153, 153, 153));
        btn_login.setkBorderRadius(70);
        btn_login.setkEndColor(new java.awt.Color(229, 229, 203));
        btn_login.setkHoverEndColor(new java.awt.Color(204, 0, 204));
        btn_login.setkHoverForeGround(new java.awt.Color(0, 0, 0));
        btn_login.setkHoverStartColor(new java.awt.Color(0, 204, 204));
        btn_login.setkStartColor(new java.awt.Color(76, 161, 175));
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });
        kGradientPanel2.add(btn_login, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 390, 130, 40));

        kButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        kButton2.setkBorderRadius(90);
        kButton2.setkEndColor(new java.awt.Color(255, 0, 0));
        kButton2.setkStartColor(new java.awt.Color(255, 0, 0));
        kButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton2ActionPerformed(evt);
            }
        });
        kGradientPanel2.add(kButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 10, 10));

        kButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        kButton3.setkBorderRadius(90);
        kButton3.setkEndColor(new java.awt.Color(255, 255, 0));
        kButton3.setkStartColor(new java.awt.Color(255, 255, 0));
        kButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton3ActionPerformed(evt);
            }
        });
        kGradientPanel2.add(kButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 10, 10, 10));

        Pascekbok.setFont(new java.awt.Font("Sitka Display", 3, 14)); // NOI18N
        Pascekbok.setForeground(new java.awt.Color(255, 255, 255));
        Pascekbok.setText("Show Password");
        Pascekbok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PascekbokActionPerformed(evt);
            }
        });
        kGradientPanel2.add(Pascekbok, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 310, -1, -1));

        getContentPane().add(kGradientPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 570, 470));

        kGradientPanel1.setkEndColor(new java.awt.Color(238, 238, 238));
        kGradientPanel1.setkStartColor(new java.awt.Color(238, 238, 238));
        kGradientPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                kGradientPanel1MouseDragged(evt);
            }
        });
        kGradientPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                kGradientPanel1MousePressed(evt);
            }
        });
        kGradientPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo2.png"))); // NOI18N
        kGradientPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 400, 370));

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Demi", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(34, 40, 49));
        jLabel7.setText("LOGIN ADMIN");
        kGradientPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 240, 40));

        getContentPane().add(kGradientPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 470));

        getAccessibleContext().setAccessibleParent(kGradientPanel2);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpasswordActionPerformed

    private void txtusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtusernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtusernameActionPerformed

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
            // TODO add your handling code here:
       String username=txtusername.getText();
        String password=String.valueOf(txtpassword.getPassword());

        //kondisi jika username kosong
        if (username.isEmpty() ) {
            JOptionPane.showMessageDialog(null,"Username tidak boleh kosong");
            txtusername.requestFocus();
        }
        //kondisi jika password kosong
        else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(null,"Password tidak boleh kosong");
        }
        try{
            //memanggil klass koneksi
            Connection conn=(Connection)koneksi.koneksiDB();
            Statement stt=conn.createStatement();

            //query ketable admin
            String sql= "SELECT * FROM admin  WHERE username='"+username+"' AND password='"+password+"'";
            ResultSet rs = stt.executeQuery(sql);

            //kondisi jika data ada
            if(rs.next()){
                this.dispose();
                new HOME().setVisible(true);

            }else{
                JOptionPane.showMessageDialog(null, "Username dan password yang anda masukkan salah!","Error",JOptionPane.ERROR_MESSAGE);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Login gagal\n"+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
// TODO add your handling code here:
    }//GEN-LAST:event_btn_loginActionPerformed

    private void kButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton2ActionPerformed
        int result = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin Keluar?", "Logout", JOptionPane.YES_NO_OPTION);
if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
    // tindakan jika pengguna memilih "Yes"
} else {
    // tindakan jika pengguna memilih "No"
}
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton2ActionPerformed

    private void kButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton3ActionPerformed
        this.setExtendedState(Frame.ICONIFIED);
        // TODO add your handling code here:
    }//GEN-LAST:event_kButton3ActionPerformed

    private void kGradientPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kGradientPanel1MousePressed
        rMouse = evt.getX();
        sMouse = evt.getY();
        // TODO add your handling code here:
    }//GEN-LAST:event_kGradientPanel1MousePressed

    private void kGradientPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kGradientPanel1MouseDragged
        int r = evt.getXOnScreen();
        int s = evt.getYOnScreen();
        this.setLocation(r -  rMouse, s - sMouse);
        // TODO add your handling code here:
    }//GEN-LAST:event_kGradientPanel1MouseDragged

    private void PascekbokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PascekbokActionPerformed
        if(Pascekbok.isSelected()){
            txtpassword.setEchoChar((char)0);
        }else{
            txtpassword.setEchoChar('*');
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_PascekbokActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Pascekbok;
    private com.k33ptoo.App app1;
    private com.k33ptoo.components.KButton btn_login;
    private com.k33ptoo.utils.ComponentMoverUtil componentMoverUtil1;
    private com.toedter.plaf.JCalendarTheme jCalendarTheme1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JColorChooser jColorChooser1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private com.k33ptoo.components.KButton kButton2;
    private com.k33ptoo.components.KButton kButton3;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel2;
    private javax.swing.JPasswordField txtpassword;
    private javax.swing.JTextField txtusername;
    // End of variables declaration//GEN-END:variables
    
}
