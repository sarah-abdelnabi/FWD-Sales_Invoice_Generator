package Controller;

import Model.InvoiceHeader;
import Model.InvoiceLine;
import Model.InvoicesTable;
import Model.LinesTable;
import View.NewInvoicesDialog;
import View.MainFrame;
import View.NewLinesDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Controller implements ActionListener , ListSelectionListener{
    
    private MainFrame frame;
    private NewInvoicesDialog invoiceDialog;
    private NewLinesDialog lineDialog;
    
    public Controller(MainFrame frame){
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Action "+actionCommand);
        switch (actionCommand){
            case "Load File":
                loadFile();
                break;
            case "Save File":
                saveFile();
                break;
            case "Create New Invoice":
                createNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Create New Item":
                createNewItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
            case "createInvoiceCancel":
                createInvoiceCancel();
                break;
            case "createInvoiceOK":
                createInvoiceOK();
                break;
            case "createLineOK":
                createLineOK();
                break;
            case "createLineCancel":
                createLineCancel();
                break;
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
        if(selectedIndex != -1){
        System.out.println("you have selected row: " +selectedIndex );
        InvoiceHeader CurrentInvoice = frame.getInvoices().get(selectedIndex);
        frame.getInvoiceNumberlbl().setText(""+CurrentInvoice.getNum());
        frame.getInvoiceDatelbl().setText(CurrentInvoice.getDate());
        frame.getCustomerNamelbl().setText(CurrentInvoice.getName());
        frame.getInvoiceTotallbl().setText(""+CurrentInvoice.getTotal());
        LinesTable lineTable = new LinesTable(CurrentInvoice.getLines());
        frame.getLineTable().setModel(lineTable);
        lineTable.fireTableDataChanged();
        }
    }

    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        try{
        int result = fc.showOpenDialog(frame);
        if(result == JFileChooser.APPROVE_OPTION){
            File headerFile = fc.getSelectedFile();
            Path headerPath = Paths.get(headerFile.getAbsolutePath());
            List<String> headerLines = Files.readAllLines(headerPath);
            System.out.println("Invoices have been read ");
            ArrayList<InvoiceHeader> invoicesArray = new ArrayList<>();
            for (String headerLine : headerLines){
                String[] headerParts = headerLine.split(",");
                int invoiceNum = Integer.parseInt(headerParts[0]);
                String invoiceDate = headerParts[1];
                String customerName = headerParts[2];
                InvoiceHeader invoice = new InvoiceHeader(invoiceNum,invoiceDate,customerName);
                invoicesArray.add(invoice);
            }
            System.out.println("Ceck point");
            result = fc.showOpenDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION){
                File lineFile = fc.getSelectedFile();
                Path linePath = Paths.get(lineFile.getAbsolutePath());
                List<String> lineLines = Files.readAllLines(linePath);
                System.out.println("Lines have been read ");
                for (String lineLine : lineLines){
                    String[] lineParts = lineLine.split(",");
                    int invoiceNum = Integer.parseInt(lineParts[0]);
                    String itemName = lineParts[1];
                    double itemPrice = Double.parseDouble(lineParts[2]);
                    int count = Integer.parseInt(lineParts[3]);
                    InvoiceHeader inv = null;
                    for (InvoiceHeader invoice :invoicesArray){
                        if(invoice.getNum() == invoiceNum){
                            inv = invoice;
                            break;
                        }
                    }
                   InvoiceLine line = new InvoiceLine(itemName,itemPrice,count,inv);
                    inv.getLines().add(line);
                }
                System.out.println("Ceck point");
            }
            frame.setInvoices(invoicesArray);
            InvoicesTable invoicesTable = new InvoicesTable(invoicesArray);
            frame.setInvoicesTable(invoicesTable);
            frame.getInvoiceTable().setModel(invoicesTable);
            frame.getInvoicesTable().fireTableDataChanged();
        }
        }catch (IOException ex){
                ex.printStackTrace();
                }
    }

    private void saveFile() {
        ArrayList<InvoiceHeader> invoices = frame.getInvoices();
        String headers = "";
        String lines = "";
        for (InvoiceHeader invoice : invoices){
            String invCSV= invoice.getAsCSV();
            headers += invCSV;
            headers += "\n";
            for (InvoiceLine line : invoice.getLines()){
                String lineCSV = line.getAsCSV();
                lines += lineCSV;
                lines += "\n";
            }
        }
        System.out.println("check point");
        try{ 
        JFileChooser fc = new JFileChooser();
        int result = fc.showSaveDialog(frame);
        if(result == JFileChooser.APPROVE_OPTION){
            File headerFile =  fc.getSelectedFile();
            FileWriter hfw = new FileWriter(headerFile);
            hfw.write(headers);
            hfw.flush();
            hfw.close();
            result = fc.showSaveDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION){
                File lineFile = fc.getSelectedFile();
                FileWriter lfw = new FileWriter(lineFile);
                lfw.write(lines);
                lfw.flush();
                lfw.close();
            }
        }
        }catch (Exception ex){
            
        }
    }

    private void createNewInvoice() {
        invoiceDialog = new NewInvoicesDialog(frame);
        invoiceDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedRow = frame.getInvoiceTable().getSelectedRow();
        if(selectedRow != -1){
            frame.getInvoices().remove(selectedRow);
            frame.getInvoicesTable().fireTableDataChanged();
        }
    }

    private void createNewItem() {
        lineDialog = new NewLinesDialog(frame);
        lineDialog.setVisible(true);
    }

    private void deleteItem() {
        int selectedRow = frame.getLineTable().getSelectedRow();
        if(selectedRow != -1){
            LinesTable linesTableModel = (LinesTable) frame.getLineTable().getModel();
            linesTableModel.getLines().remove(selectedRow);
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTable().fireTableDataChanged();
        }
    }

    private void createInvoiceCancel() {
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

    private void createInvoiceOK() {
        String date = invoiceDialog.getInvDateField().getText();
        String customer = invoiceDialog.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();
        InvoiceHeader invoiceHeader = new InvoiceHeader(num,date,customer);
        frame.getInvoices().add(invoiceHeader);
        frame.getInvoicesTable().fireTableDataChanged();
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

    private void createLineOK() {
        String item = lineDialog.getItemNameField().getText();
        String countSTR = lineDialog.getItemCountField().getText();
        String priceSTR = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countSTR);
        double price = Double.parseDouble(priceSTR);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        if(selectedInvoice != -1){
            InvoiceHeader invoice = frame.getInvoices().get(selectedInvoice);
            InvoiceLine line = new InvoiceLine(item,price,count,invoice);
            invoice.getLines().add(line);
            LinesTable linesTable = (LinesTable) frame.getLineTable().getModel();
            linesTable.fireTableDataChanged();
            frame.getInvoicesTable().fireTableDataChanged();
        }
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void createLineCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }
   
}
