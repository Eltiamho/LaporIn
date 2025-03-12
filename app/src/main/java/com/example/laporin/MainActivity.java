package com.example.laporin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.net.Uri;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Deklarasi EditText untuk form input:
    private EditText etNama, etAduan, etLokasi, etKeterangan;
    // Tombol untuk mengirim laporan dan upload foto
    private Button btnLapor, btnUploadFoto;
    // ImageView untuk preview foto bukti
    private ImageView ivBukti;
    // RecyclerView untuk menampilkan laporan
    private RecyclerView rvLaporan;
    // List dan adapter laporan
    private ArrayList<Laporan> listLaporan;
    private LaporanAdapter adapter;

    // Variabel untuk menyimpan URI foto yang dipilih
    private Uri imageUri = null;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi komponen berdasarkan id
        etNama = findViewById(R.id.etNama);
        etAduan = findViewById(R.id.etAduan);
        etLokasi = findViewById(R.id.etLokasi);
        etKeterangan = findViewById(R.id.etKeterangan);
        btnUploadFoto = findViewById(R.id.btnUploadFoto);
        ivBukti = findViewById(R.id.ivBukti);
        btnLapor = findViewById(R.id.btnLapor);
        rvLaporan = findViewById(R.id.rvLaporan);

        listLaporan = new ArrayList<>();
        adapter = new LaporanAdapter(listLaporan);
        rvLaporan.setLayoutManager(new LinearLayoutManager(this));
        rvLaporan.setAdapter(adapter);

        // Tombol Upload Foto untuk membuka galeri
        btnUploadFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuka intent untuk memilih gambar
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        // Tombol "Lapor" untuk menambahkan laporan baru
        btnLapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = etNama.getText().toString().trim();
                String aduan = etAduan.getText().toString().trim();
                String lokasi = etLokasi.getText().toString().trim();
                String keterangan = etKeterangan.getText().toString().trim();
                // Ambil URI foto sebagai string, jika ada
                String bukti = (imageUri != null) ? imageUri.toString() : "";

                // Validasi field wajib
                if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(aduan) ||
                        TextUtils.isEmpty(lokasi) || TextUtils.isEmpty(keterangan)) {
                    Toast.makeText(MainActivity.this, "Nama, Aduan, Lokasi, dan Keterangan wajib diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // CREATE: Membuat objek laporan baru dan menambahkannya ke list
                Laporan laporan = new Laporan(nama, aduan, lokasi, keterangan, bukti);
                listLaporan.add(laporan);
                adapter.notifyItemInserted(listLaporan.size() - 1);
                clearForm();
            }
        });

        // Implementasi Update dan Delete pada item RecyclerView
        adapter.setOnItemClickListener(new LaporanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                final Laporan laporan = listLaporan.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Update Laporan");

                // Inflate layout update (pastikan dialog_update.xml sudah diperbarui untuk foto)
                View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_update, null);
                final EditText inputNama = viewInflated.findViewById(R.id.etUpdateNama);
                final EditText inputAduan = viewInflated.findViewById(R.id.etUpdateAduan);
                final EditText inputLokasi = viewInflated.findViewById(R.id.etUpdateLokasi);
                final EditText inputKeterangan = viewInflated.findViewById(R.id.etUpdateKeterangan);
                // ImageView untuk preview foto pada dialog update
                final ImageView ivUpdateFoto = viewInflated.findViewById(R.id.ivUpdateFoto);
                // Tombol untuk mengganti foto pada dialog update
                Button btnUpdateFoto = viewInflated.findViewById(R.id.btnUpdateFoto);

                // Set nilai awal
                inputNama.setText(laporan.getNama());
                inputAduan.setText(laporan.getAduan());
                inputLokasi.setText(laporan.getLokasi());
                inputKeterangan.setText(laporan.getKeterangan());
                if (!laporan.getBukti().isEmpty()) {
                    ivUpdateFoto.setImageURI(Uri.parse(laporan.getBukti()));
                }

                // Tombol untuk mengganti foto pada dialog update
                btnUpdateFoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Sama seperti upload foto di MainActivity
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        // Menggunakan startActivityForResult dengan request code berbeda, misal 2
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                        // Untuk kesederhanaan, Anda bisa menyimpan URI baru ke laporan setelah update selesai,
                        // atau menggunakan pendekatan ActivityResultLauncher agar tidak bentrok.
                    }
                });

                builder.setView(viewInflated);
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        laporan.setNama(inputNama.getText().toString().trim());
                        laporan.setAduan(inputAduan.getText().toString().trim());
                        laporan.setLokasi(inputLokasi.getText().toString().trim());
                        laporan.setKeterangan(inputKeterangan.getText().toString().trim());
                        // Jika gambar diupdate melalui dialog, Anda perlu menambahkan logika
                        // untuk mendapatkan URI foto baru. Contoh di sini tidak meng-handle update foto.
                        adapter.notifyItemChanged(position);
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }

            @Override
            public void onItemLongClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Hapus Laporan");
                builder.setMessage("Apakah anda yakin ingin menghapus laporan ini?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listLaporan.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("Tidak", null);
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Untuk pemilihan foto di MainActivity
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivBukti.setImageURI(imageUri);
        }
        // Jika Anda mengimplementasikan update foto di dialog update dengan request code 2,
        // Anda bisa menambahkan penanganan untuk requestCode == 2 di sini.
    }

    private void clearForm() {
        etNama.setText("");
        etAduan.setText("");
        etLokasi.setText("");
        etKeterangan.setText("");
        ivBukti.setImageResource(0); // Menghapus preview gambar
        imageUri = null;
    }
}