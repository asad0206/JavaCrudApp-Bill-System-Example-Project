import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.*;

public class JavaCrud {
    private JPanel Main;

    private JTextField txtName;
    private JTextField txtPrice;
    private JButton saveButton;
    private JButton deleteButton;
    private JTextField txtQty;

    private JButton updateButton;
    private JTextField txtfid;
    private JButton searchButton;
    private JButton billButton;
    private JButton displayButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("RestaurantApp");
        frame.setContentPane(new JavaCrud().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    Connection con;
    PreparedStatement pst;

    public JavaCrud() {
        Connect();

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name,price,qty;

                name = txtName.getText();
                price = txtPrice.getText();
                qty = txtQty.getText();

                try {
                    pst = con.prepareStatement("insert into products(fname,price,qty)values(?,?,?)");
                    pst.setString(1, name);
                    pst.setString(2, price);
                    pst.setString(3, qty);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Record Added!!!!");

                    txtName.setText("");
                    txtPrice.setText("");
                    txtQty.setText("");
                    txtName.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String fid = txtfid.getText();
                    pst = con.prepareStatement("select fname,price,qty from products where fid = ?");
                    pst.setString(1, fid);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()==true)
                    {
                        String name = rs.getString(1);
                        String price = rs.getString(2);
                        String qty = rs.getString(3);

                        txtName.setText(name);
                        txtPrice.setText(price);
                        txtQty.setText(qty);
                    }
                    else
                    {
                        txtName.setText("");
                        txtPrice.setText("");
                        txtQty.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid Product ID");

                    }
                }

                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fid,name,price,qty;

                name = txtName.getText();
                price = txtPrice.getText();
                qty = txtQty.getText();
                fid = txtfid.getText();

                try {
                    pst = con.prepareStatement("update products set fname = ?,price = ?,qty = ? where fid = ?");
                    pst.setString(1, name);
                    pst.setString(2, price);
                    pst.setString(3, qty);
                    pst.setString(4, fid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Updated!");

                    txtName.setText("");
                    txtPrice.setText("");
                    txtQty.setText("");
                    txtName.requestFocus();
                    txtfid.setText("");
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bid;

                bid = txtfid.getText();


                try {
                    pst = con.prepareStatement("delete from products  where fid = ?");
                    pst.setString(1, bid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleted!");

                    txtName.setText("");
                    txtPrice.setText("");
                    txtQty.setText("");
                    txtName.requestFocus();
                    txtfid.setText("");
                }

                catch (SQLException e1)
                {

                    e1.printStackTrace();
                }
            }
        });


        billButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name,price,qty;

                int bprice, bqty;

                name = txtName.getText();
                price = txtPrice.getText();
                qty = txtQty.getText();

                try {
                    bprice = Integer.parseInt(price);
                    bqty = Integer.parseInt(qty);

                    double total = bprice * bqty;

                    double tax = 0.02 * total;

                    double bill = total + tax;

                    JOptionPane.showMessageDialog(null, "Tax: "+tax+"\n Total Bill: "+bill);
                }
                catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }

            }
        });


        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog displayDatabase = new JDialog();
                displayDatabase.setSize(480,480);
                displayDatabase.setVisible(true);
                JPanel disp = new JPanel();
                disp.setLayout(null);

                JLabel dispfid = new JLabel("FID");
                JLabel dispfname = new JLabel("FNAME");
                JLabel dispprice = new JLabel("PRICE");
                JLabel dispqty = new JLabel("QTY");

                JTextArea data = new JTextArea();
                disp.add(data);
                data.setBounds(0,50,300,400);

                disp.add(dispfid);
                disp.add(dispfname);
                disp.add(dispprice);
                disp.add(dispqty);

                dispfid.setBounds(0,0, 50,50);
                dispfname.setBounds(50,0, 50,50);
                dispprice.setBounds(150,0, 50,50);
                dispqty.setBounds(250,0, 50,50);

                displayDatabase.add(disp);

                try{
                    Statement var = con.createStatement();
                    ResultSet rs = var.executeQuery("select * from products");


                    while(rs.next()){
                        String fid = rs.getString("fid");
                        String fname = rs.getString("fname");
                        String price = rs.getString("price");
                        String qty = rs.getString("qty");
                        data.append(fid+"\t"+fname+"\t"+price+"\t"+qty+"\t\n\n");
                    }
                }
                catch (SQLException e1){
                        e1.printStackTrace();
                }
            }
        });
    }

    public void Connect() {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/gbproducts", "root","");
            System.out.println("Success");
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

}
