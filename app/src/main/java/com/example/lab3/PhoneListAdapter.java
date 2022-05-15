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
    private RecyclerViewOnClickListener mOnClickListener;

    public PhoneListAdapter(Context context, RecyclerViewOnClickListener listener) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mOnClickListener = listener;
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

    public List<Phone> getPhoneList() {
        return this.mPhoneList;
    }

    public class PhoneAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextViewProducer;
        TextView mTextViewModel;

        public PhoneAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            connectLayoutElementsToFields(itemView);
        }

        private void connectLayoutElementsToFields(@NonNull View itemView) {
            mTextViewProducer = itemView.findViewById(R.id.textViewProducer);
            mTextViewModel = itemView.findViewById(R.id.textViewModel);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onCLick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewOnClickListener {
        void onCLick(View view, int position);
    }
}
