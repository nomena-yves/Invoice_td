package hei.group.pushdowninvoice;

public class Main {
    public static void main(String[] args) {
        DabaseConnection connection = new DabaseConnection();
        InvoiceTotal invoiceTotal= new InvoiceTotal(1,"BOB",Status.PAID,100.0
        );
try{
    //System.out.println(invoiceTotal.findInvoiceTotal());
    //System.out.println(invoiceTotal.findInvoiceConfirmedAndPaidInvoiceTotal());
    //System.out.println(invoiceTotal.computeStatusTotals());
    //System.out.println(invoiceTotal.compteWeightedTurnorver());
    //System.out.println(invoiceTotal.findInvoiceTaxSummary());
    System.out.println(invoiceTotal.computeWeightedTurnorverTTC());
}catch(Exception e){
    System.out.println(e.getMessage());
}
    }
}
