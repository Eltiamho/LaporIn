public class Laporan {
    private String nama;
    private String aduan;
    private String lokasi;
    private String keterangan;
    private String bukti;

    public Laporan(String nama, String aduan, String lokasi, String keterangan, String bukti) {
        this.nama = nama;
        this.aduan = aduan;
        this.lokasi = lokasi;
        this.keterangan = keterangan;
        this.bukti = bukti;
}

    // Getter dan Setter
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getAduan() { return aduan; }
    public void setAduan(String aduan) { this.aduan = aduan; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public String getBukti() { return bukti; }
    public void setBukti(String bukti) { this.bukti = bukti; }
}

