package hei.group.pushdowninvoice;

import java.util.Objects;

public class InvoiceTaxSummary {
    private int id;
    private Double HT;
    private Double TVA;
    private Double TTC;

    public InvoiceTaxSummary(int id, Double HT, Double TVA, Double TTC) {
        this.id = id;
        this.HT = HT;
        this.TVA = TVA;
        this.TTC = TTC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getHT() {
        return HT;
    }

    public void setHT(Double HT) {
        this.HT = HT;
    }

    public Double getTVA() {
        return TVA;
    }

    public void setTVA(Double TVA) {
        this.TVA = TVA;
    }

    public Double getTTC() {
        return TTC;
    }

    public void setTTC(Double TTC) {
        this.TTC = TTC;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceTaxSummary that = (InvoiceTaxSummary) o;
        return id == that.id && Objects.equals(HT, that.HT) && Objects.equals(TVA, that.TVA) && Objects.equals(TTC, that.TTC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, HT, TVA, TTC);
    }

    @Override
    public String toString() {
        return "InvoiceTaxSumeery{" +
                "id=" + id +
                ", HT=" + HT +
                ", TVA=" + TVA +
                ", TTC=" + TTC +
                '}';
    }

}
