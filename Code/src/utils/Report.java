package utils;


import javafx.fxml.FXMLLoader;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.*;
import java.sql.Connection;
import java.time.LocalTime;
import java.util.HashMap;

public class Report {

    private static String sql;
    private static FileInputStream inputStream;
    private static JasperDesign jasperDesign;
    private static HashMap para;
    static String jasperReport;
    static String path = "D:\\Codes\\Development\\BITS\\DBS-Project\\Code\\src\\sample\\";



    public static void printReport( String report) throws JRException {


        String sourceFileName = path + "ReportDesign\\" + report + ".jrxml";
        System.out.println("Compiling Report Design ...");
        try {
            //Compiling the jrxml and creating a Jasper type object
            jasperReport =  JasperCompileManager.compileReportToFile(sourceFileName);
        } catch (JRException e) {
            e.printStackTrace();
        }
        System.out.println("Compilation Done!!! ...");


        try {
             inputStream = new FileInputStream( new File( path + "ReportDesign\\" + report + ".jrxml"));
            jasperDesign = JRXmlLoader.load(inputStream);

            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sql);
            jasperDesign.setQuery(newQuery);
            JasperReport jr = JasperCompileManager.compileReport(jasperDesign);
            para = new HashMap();
            JasperPrint j = JasperFillManager.fillReport(jr, para , ConnectionUtil.conDB());
            JasperViewer.viewReport(j, false);
            //String currtime = LocalTime.now().toString();
            //OutputStream os = new FileOutputStream(new File(path + "ReportPdf\\" + report + "_" + currtime + ".pdf"));
            //JasperExportManager.exportReportToPdfStream(j, os);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static void printEmployee(){
        sql = " select * from employee";
        try {
            printReport("employee");
        } catch (JRException e) {
            e.printStackTrace();
        }

    }
    public static void printEmployee( String startDate, String endDate){
        sql = " select * from employee where joining between" + startDate + " and " + endDate;
        try {
            printReport("employee");
        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    public static void printparking(){
        sql = " select * from parking_slot";
        try {
            printReport("parking");
        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    public static void printVisitor(){
        sql = " select * from visitor";
        try {
            printReport("visitors");
        } catch (JRException e) {
            e.printStackTrace();
        }

    }
    public static void printattendance(){
        sql = " select * from visitor";
        try {
            printReport("visitors");
        } catch (JRException e) {
            e.printStackTrace();
        }

    }
}