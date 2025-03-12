

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder> {
    private ArrayList<Laporan> listLaporan;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LaporanAdapter(ArrayList<Laporan> listLaporan) {
        this.listLaporan = listLaporan;
    }

    @NonNull
    @Override
    public LaporanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laporan, parent, false);
        return new LaporanViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanViewHolder holder, int position) {
        Laporan laporan = listLaporan.get(position);
        holder.tvNama.setText(laporan.getNama());
        holder.tvAduan.setText(laporan.getAduan());
        holder.tvLokasi.setText(laporan.getLokasi());
        holder.tvKeterangan.setText(laporan.getKeterangan());
        holder.tvBukti.setText(laporan.getBukti());
    }

    @Override
    public int getItemCount() {
        return listLaporan.size();
    }

    public static class LaporanViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama, tvAduan, tvLokasi, tvKeterangan, tvBukti;

        public LaporanViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvAduan = itemView.findViewById(R.id.tvAduan);
            tvLokasi = itemView.findViewById(R.id.tvLokasi);
            tvKeterangan = itemView.findViewById(R.id.tvKeterangan);
            tvBukti = itemView.findViewById(R.id.tvBukti);

            // Click untuk update
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // Long click untuk delete
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemLongClick(position);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
