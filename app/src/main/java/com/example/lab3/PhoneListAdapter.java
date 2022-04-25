package com.example.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhoneListAdapter extends RecyclerView.Adapter<PhoneListAdapter.PhoneAdapterViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Phone> mPhoneList;

    public PhoneListAdapter(Context context) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mPhoneList = null;
    }

    @NonNull
    @Override
    public PhoneAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = mLayoutInflater.inflate(R.layout.phone_row, parent, false);

        return new PhoneAdapterViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneAdapterViewHolder holder, int position) {
        holder.mTextViewProducer.setText(mPhoneList.get(position).getProducer());
        holder.mTextViewModel.setText(mPhoneList.get(position).getModel());
    }

    @Override
    public int getItemCount() {
        return mPhoneList != null ? mPhoneList.size() : 0;
    }

    public void setPhoneList(List<Phone> phoneList) {
        this.mPhoneList = phoneList;
        notifyDataSetChanged();
    }

    public class PhoneAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewProducer;
        TextView mTextViewModel;

        public PhoneAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            connectLayoutElementsToFields(itemView);
            // TODO dodawanie nowego rekordu:
            // TODO mozemy ustawic onClickListener na calym wierszu:
            // TODO itemView.setOnClickListener(this)
            // TODO w adapterze mozemy utworzyc interfejs pozwalajacy na ustawienie listenera
        }

        private void connectLayoutElementsToFields(@NonNull View itemView) {
            mTextViewProducer = itemView.findViewById(R.id.textViewProducer);
            mTextViewModel = itemView.findViewById(R.id.textViewModel);
        }
    }
}
