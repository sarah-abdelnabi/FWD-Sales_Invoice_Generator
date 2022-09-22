
package Model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


public class LinesTable extends AbstractTableModel {

    private ArrayList<InvoiceLine> lines;
    private String[] columns = {"Nmber" ,"Item Name","Item Price","Count","Total"};

    public LinesTable(ArrayList<InvoiceLine> lines) {
        this.lines = lines;
    }
    
    public ArrayList<InvoiceLine> getLines() {
        return lines;
    }
    
    @Override
    public int getRowCount() {
        return lines.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceLine line = lines.get(rowIndex);
        switch (columnIndex){
            case 0: return line.getInv().getNum();
            case 1: return line.getName();
            case 2: return line.getPrice();
            case 3: return line.getCount();
            case 4: return line.getTotal();
            default: return "";
        }
    }


}
