package com.application.pengingatsederhana.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pengingatsederhana.CustomOnItemClickListener;
import com.application.pengingatsederhana.Detail;
import com.application.pengingatsederhana.Entity.Reminder;
import com.application.pengingatsederhana.R;

import java.util.LinkedList;

public class ReminderAdaptor extends RecyclerView.Adapter<ReminderAdaptor.ReminderViewholder> {
    private LinkedList<Reminder> listReminder;
    private Activity activity;
    public ReminderAdaptor(Activity activity) {
        this.activity = activity;
    }

    public LinkedList<Reminder> getListReminder() {
        return listReminder;
    }

    public void setListReminder(LinkedList<Reminder> listReminder) {
        this.listReminder = listReminder;
    }
    @Override
    public ReminderViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ReminderViewholder(view);
    }
    @Override
    public void onBindViewHolder(ReminderViewholder holder, int position) {
        holder.tvTitle.setText(getListReminder().get(position).getTitle());
        holder.tvDesc.setText(getListReminder().get(position).getDesc());
        holder.tvClock.setText(getListReminder().get(position).getClock());
        holder.tvWaktu.setText(getListReminder().get(position).getWaktu());
        holder.cvReminder.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, Detail.class);
                intent.putExtra(Detail.EXTRA_POSITION, position);
                intent.putExtra(Detail.EXTRA_REMINDER, getListReminder().get(position));
                activity.startActivityForResult(intent, Detail.REQUEST_UPDATE);
            }
        }));
    }
    @Override
    public int getItemCount() {
        return getListReminder().size();
    }

    public class ReminderViewholder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDesc, tvClock, tvWaktu;
        CardView cvReminder;

        public ReminderViewholder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_item_title);
            tvDesc = (TextView)itemView.findViewById(R.id.tv_item_description);
            tvWaktu = (TextView)itemView.findViewById(R.id.tv_item_waktu);
            tvClock = (TextView)itemView.findViewById(R.id.tv_item_clock);
            cvReminder = (CardView) itemView.findViewById(R.id.cv_item_reminder);
        }
    }
}
