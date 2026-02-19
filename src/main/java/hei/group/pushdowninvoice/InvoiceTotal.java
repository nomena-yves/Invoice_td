package hei.group.pushdowninvoice;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvoiceTotal {
    private int id;
    private String nom_cliente;
    private Status status;
    private Double total;

    public InvoiceTotal(int id, String nom_cliente, Status status,Double total) {
        this.id = id;
        this.nom_cliente = nom_cliente;
        this.status = status;
        this.total = total;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_cliente() {
        return nom_cliente;
    }

    public void setNom_cliente(String nom_cliente) {
        this.nom_cliente = nom_cliente;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceTotal that = (InvoiceTotal) o;
        return id == that.id && Objects.equals(nom_cliente, that.nom_cliente) && status == that.status && Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom_cliente, status, total);
    }

    @Override
    public String toString() {
        return "InvoiceTotal{" +
                "id=" + id +
                ", nom_cliente='" + nom_cliente + '\'' +
                ", status=" + status +
                ", total=" + total +
                '}';
    }

    public List<InvoiceTotal> findInvoiceTotal()throws Exception {
        InvoiceTotal invoiceTotal = null;
        List<InvoiceTotal> invoiceTotalList = new ArrayList<>();
        String sql= """
                select i.id, i.customer_name,sum( il.quantity*il.unit_price) as total  from invoice  i inner join invoice_line  il on i.id=il.invoice_id group by i.id
                """;
        DabaseConnection db = new DabaseConnection();
        Connection conn = db.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while(rs.next()) {
            invoiceTotal=new InvoiceTotal(
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    null,
                    rs.getDouble("total")
            );

            invoiceTotalList.add(invoiceTotal);
        }
        conn.close();
        return invoiceTotalList;
    }

    public List<InvoiceTotal> findInvoiceConfirmedAndPaidInvoiceTotal()throws Exception {
    String sql= """
            select i.id, i.customer_name,sum( il.quantity*il.unit_price) as total,i.status  from invoice  i inner join invoice_line  il on i.id=il.invoice_id where i.status='CONFIRMED'or i.status='PAID' group by i.id, i.customer_name, i.status; 
            """;
        DabaseConnection db = new DabaseConnection();
        Connection conn = db.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        InvoiceTotal invoiceTotal =null;
        List<InvoiceTotal> invoiceTotalList = new ArrayList<>();
        while(rs.next()) {
            invoiceTotal=new InvoiceTotal(
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    Status.valueOf(rs.getString("status")),
                    rs.getDouble("total")
            );
            invoiceTotalList.add(invoiceTotal);
        }
        conn.close();
        return invoiceTotalList;
    }

   public List<InvoiceStatusTotal> computeStatusTotals()throws SQLException {
        DabaseConnection db = new DabaseConnection();
        Connection conn = db.getConnection();
    InvoiceStatusTotal invoiceStatusTotal = null;
    List<InvoiceStatusTotal> invoiceStatusTotalList = new ArrayList<>();
        String sql= """
                select i.status, sum(il.unit_price*il.quantity)
                 as total from invoice i inner join invoice_line il 
                 on i.id=il.invoice_id group by i.status""";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while(rs.next()) {
            invoiceStatusTotal=new InvoiceStatusTotal(
                    Status.valueOf(rs.getString("status")),
                    rs.getDouble("total")
            );
            invoiceStatusTotalList.add(invoiceStatusTotal);
        }
        conn.close();
        return invoiceStatusTotalList;
    }

    public Double compteWeightedTurnorver() throws SQLException {
        DabaseConnection db = new DabaseConnection();
        Double weightedTurnorver = 0.0;
        Connection conn = db.getConnection();
        String sql= """
                select sum(case when i.status='PAID' then il.quantity*il.unit_price
                when i.status='CONFIRMED' then il.quantity*il.unit_price*0.5
                ELSE 0
                END) as total_amount
                from invoice i
                inner join invoice_line il
                on i.id=il.invoice_id""";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        if(rs.next()) {
            weightedTurnorver+=rs.getDouble("total_amount");
        }
        conn.close();
        return weightedTurnorver;
    }

    public List<InvoiceTaxSummary> findInvoiceTaxSummary()throws Exception {
        DabaseConnection db = new DabaseConnection();
        Connection conn = db.getConnection();
        InvoiceTaxSummary invoiceTaxSummary = null;
        String sql= """
                SELECT
                    i.status,i.id,
                    SUM(il.unit_price * il.quantity) AS total,
                    SUM(il.unit_price * il.quantity) / 5 AS tva,
                    SUM(il.unit_price * il.quantity)
                        + (SUM(il.unit_price * il.quantity) / 5) AS TTC
                FROM invoice i
                INNER JOIN invoice_line il
                    ON i.id = il.invoice_id
                GROUP BY i.status,i.id; 
               """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        List<InvoiceTaxSummary> invoiceTaxSummaryList = new ArrayList<>();
        while(rs.next()) {
            invoiceTaxSummary=new InvoiceTaxSummary(
                    rs.getInt("id"),
                    rs.getDouble("total"),
                    rs.getDouble("TVA"),
                    rs.getDouble("TTC")
            );
            invoiceTaxSummaryList.add(invoiceTaxSummary);
        }
        conn.close();
        return invoiceTaxSummaryList;
    }
    public BigDecimal computeWeightedTurnorverTTC() throws SQLException {
        DabaseConnection db = new DabaseConnection();
        Connection conn = db.getConnection();
        BigDecimal weightedTurnorver = BigDecimal.ZERO;
        String sql= """
                select sum(
                    CASE
                        When i.status='PAID'
                        then ROUND(((il.quantity*il.unit_price) * (1+tc.rate/100))::numeric,2)
                        when i.status='CONFIRMED'
                        then round(((il.quantity*il.unit_price)*(1+tc.rate/100)*0.5)::numeric,2)
                        ELSE 0
                        END) as total_amount
                        from invoice i inner join invoice_line il on i.id=il.invoice_id cross join tax_config tc
                """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        if(rs.next()) {
            weightedTurnorver=BigDecimal.valueOf(rs.getDouble("total_amount"));
        }
        return weightedTurnorver;
    }

}
