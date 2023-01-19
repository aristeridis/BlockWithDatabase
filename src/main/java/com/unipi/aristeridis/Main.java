package com.unipi.aristeridis;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.swing.plaf.IconUIResource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class Main {
    public static List<Block> blockChain = new ArrayList<>();
    public static int prefix = 2;

    private Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");

        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());

        }
        String url = "jdbc:sqlite:databasedb.db";
        Connection conx = null;
        try {
            conx = DriverManager.getConnection(url);
            System.out.println("Connected with database");

        } catch (Exception e) {
            throw new RuntimeException(e);
        } return conx;
    }
    public void selectAll(){
        String sql = "SELECT * FROM products";

        try (Connection conx = this.connect();
             Statement stmt  = conx.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                System.out.println(rs.getInt("AA") +  "\t" +
                        rs.getString("productCode") + "\t" +
                        rs.getString("productTitle") + "\t" +
                        rs.getString("timestamp") + "\t" +
                        rs.getDouble("price") + "\t" +
                        rs.getString("productDetails") + "\t" +
                        rs.getString("productCategory")+ "\t" +
                        rs.getInt("productPreviousStoredAA"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public ArrayList<String> storeDB(){
       ArrayList<String> array=new ArrayList<>();
            String sql = "SELECT * FROM products";
            try (Connection conx = this.connect();
                 Statement stmt  = conx.createStatement();
                 ResultSet rs    = stmt.executeQuery(sql)){
                int i=0;
                while (rs.next()) {
                    array.add(rs.getInt("AA") + "\t" +
                            rs.getString("productCode") + "\t" +
                            rs.getString("productTitle") + "\t" +
                            rs.getString("timestamp") + "\t" +
                            rs.getDouble("price") + "\t" +
                            rs.getString("productDetails") + "\t" +
                            rs.getString("productCategory") + "\t" +
                            rs.getInt("productPreviousStoredAA"));
                   // System.out.println(array[i]);
                    i++;

                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return array;
        }

    public int getLastAA(){
        String sql = "SELECT * FROM products WHERE AA = (SELECT MAX(AA) FROM products)";
        int lastaa=0;
        try (Connection conx = this.connect();
             Statement stmt  = conx.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                System.out.println(rs.getInt("AA"));
                lastaa=rs.getInt("AA");

            }

        } catch (SQLException e) {
            System.out.println("aa dosent exist");

            System.out.println(e.getMessage());
        }
        return lastaa;
    }

    public void insert(int aa, String productCode,String productTitle,String timestamp,double price,
                       String productDetails,String productCategory,int productPreviousStoredAA) {
        String sql = "INSERT INTO products(aa, productCode,productTitle,timestamp,price" +
                ",productDetails,productCategory,productPreviousStoredAA)" + " VALUES(?,?,?,?,?,?,?,?)";

        try{
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, aa);
            pstmt.setString(2, productCode);
            pstmt.setString(3, productTitle);
            pstmt.setString(4, timestamp);
            pstmt.setDouble(5, price);
            pstmt.setString(6, productDetails);
            pstmt.setString(7, productCategory);
            pstmt.setInt(8, productPreviousStoredAA);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void getRecordFromAA(int aa){
        String sql = "SELECT aa, productTitle, price "
                + "FROM products WHERE aa = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1,aa);
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt("aa") +  "\t" +
                        rs.getString("productTitle") + "\t" +
                        rs.getDouble("price"));
            }
        } catch (SQLException e) {
            System.out.println("aa dosent exist");

            System.out.println(e.getMessage());
        }
    }
    public void getRecordFromProductCategory(String productCategory){
        String sql = "SELECT aa, productTitle, price "
                + "FROM products WHERE productCategory = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            pstmt.setString(1, productCategory);
            ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getInt("aa") + "\t" +
                            rs.getString("productTitle") + "\t" +
                            rs.getDouble("price"));
                }
        }catch (SQLException e) {
            System.out.println("product dosent exist");
            System.out.println(e.getMessage());
        }
    }
    public void getProductStats(String productTitle){
        String sql = "SELECT aa, productTitle, timestamp, price "
                + "FROM products WHERE productTitle = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,productTitle);
            ResultSet rs  = pstmt.executeQuery();
            int count=0;
            while (rs.next()) {

                System.out.println(rs.getInt("aa") +  "\t" +
                        rs.getString("productTitle") + "\t" +
                        rs.getString("timestamp") + "\t" +
                        rs.getDouble("price"));
                        count++;
            }  System.out.println("product total records: "+ count);}
        catch (SQLException e) {
            System.out.println("product dosent exist");
            System.out.println(e.getMessage());
        }

    }


    public static void main(String[] args) {
        /*Options options=new OptionsBuilder().include(Main.class.getSimpleName()).threads(1).forks(1).build();
        new Runner(options).run();*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);
        Main app = new Main();
        app.selectAll();
        Main app1=new Main();
        Main app2=new Main();
        app2.storeDB();
        ArrayList<String> array1=app2.storeDB();
//        app.getRecordFromAA(3);
       app.getRecordFromProductCategory("Waterg");
        app.getProductStats("Water A");
        System.out.println("Process started");
        //1st Block
        Block genesisBlock = new Block("0",array1.get(0),
                new Date().getTime());
        genesisBlock.mineBlock(prefix);
        blockChain.add(genesisBlock);
        System.out.println("Node "+(blockChain.size())+" created!");
        System.out.println("Is chain valid?:"+isChainValid());
        System.out.println("Hash:"+genesisBlock.getHash());
        for (int i=1;i<array1.size();i++) {
            Block block = new Block(blockChain.get(blockChain.size()-1).getHash(),array1.get(i),
                    new Date().getTime());
            block.mineBlock(prefix);
            blockChain.add(block);
            System.out.println("Node "+(blockChain.size())+" created!");
            System.out.println("Hash:"+block.getHash());

        }
        System.out.println("Is chain valid?:"+isChainValid());

        /*for (int i=0;i<array1.size()-6;i++) {
            System.out.println(array1.get(i));
        }*/
        try {
            Scanner scanner=new Scanner(System.in);
            System.out.println("Enter number of products to insert");
            int num = scanner.nextInt();
        if (num == (int)num){
            for (int i=0;i<num;i++) {
                int lastaa=app1.getLastAA();
                ts = sdf.format(timestamp);
                scanner=new Scanner(System.in);
                System.out.println("Enter AA of product to insert");
                int aa = scanner.nextInt();
                scanner=new Scanner(System.in);
                System.out.println("Enter productCode of product to insert");
                String productCode = scanner.nextLine();
                scanner=new Scanner(System.in);
                System.out.println("Enter productTitle of product to insert");
                String productTitle = scanner.nextLine();
                scanner=new Scanner(System.in);
                System.out.println("Enter price of product to insert");
                Double price = scanner.nextDouble();
                scanner=new Scanner(System.in);
                System.out.println("Enter productDetails of product to insert");
                String productDetails = scanner.nextLine();
                scanner=new Scanner(System.in);
                System.out.println("Enter productCategory of product to insert");
                String productCategory = scanner.nextLine();
                app.insert(aa,productCode,productTitle,ts,price,productDetails,
                            productCategory, lastaa);
            }}}
        catch(InputMismatchException | NumberFormatException ime){
            System.out.println("You dosent gave an integer");
        }

        //app.selectAll();

    }
    public static boolean isChainValid(){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[prefix]).replace('\0','0');
        for (int i=1;i<blockChain.size();i++){
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i-1);
            if (!currentBlock.getHash().equals(currentBlock.calculateBlockHash()))
                return false;
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash()))
                return false;
            if (!currentBlock.getHash().substring(0,prefix).equals(hashTarget))
                return false;
        }
        return true;
    }
            }