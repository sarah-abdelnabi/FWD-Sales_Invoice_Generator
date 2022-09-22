    
package Model;


public class InvoiceLine {
    private String name;
    private int count;
    private double price;
    private InvoiceHeader inv;

    public InvoiceLine(String name, double price, int count, InvoiceHeader inv) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.inv = inv;
    }
    
    public double getTotal(){
        return count * price;
    }

    public InvoiceHeader getInv() {
        return inv;
    }

    public void setInv(InvoiceHeader inv) {
        this.inv = inv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "InvoiceLine{" + "num=" + inv.getNum() + "name=" + name + ", price=" + price + ", count=" + count + '}';
    }

   public String getAsCSV(){
        return inv.getNum() + "," + name + "," + price + "," + count;
    }
}
