
package Model;

import java.util.ArrayList;

public class InvoiceHeader {
    private int num;
    private String date;
    private String name;
    private ArrayList<InvoiceLine> Lines;
  

    public InvoiceHeader(int num, String date, String name) {
        this.num = num;
        this.date = date;
        this.name = name;
    }
    public double getTotal(){
        double total =0.0;
        for(InvoiceLine Line : getLines()){
            total += Line.getTotal();
        }
        return total;
    }

    public String getName() {  
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "InvoceHeader{" + "num=" + num + ", date=" + date + ", name=" + name + '}';
    }

    public ArrayList<InvoiceLine> getLines() {
        if (Lines == null){
            Lines = new ArrayList<>();
        }
        return Lines;
    }
    public String getAsCSV(){
        return num + "," + date + "," + name;
    }
}
