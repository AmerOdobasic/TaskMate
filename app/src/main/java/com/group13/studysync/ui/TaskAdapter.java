package com.group13.studysync.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group13.studysync.R;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskItem> taskList = new ArrayList<>();
    private OnTaskCompleteListener listener; // The messenger variable

    // The messenger blueprint
    public interface OnTaskCompleteListener {
        void onTaskComplete(int position);
    }

    // Method to connect the messenger
    public void setOnTaskCompleteListener(OnTaskCompleteListener listener) {
        this.listener = listener;
    }

    public void setTasks(List<TaskItem> tasks) {
        this.taskList = new ArrayList<>(tasks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskItem task = taskList.get(position);

        holder.tvTitle.setText(task.title);
        holder.tvDesc.setText(task.description);
        holder.tvDate.setText(task.date);
        holder.priorityMarker.setBackgroundColor(task.color);

        holder.itemView.setAlpha(1f);
        holder.itemView.setScaleX(1f);
        holder.itemView.setScaleY(1f);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(false);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                int currentPos = holder.getAdapterPosition();
                if (currentPos != RecyclerView.NO_POSITION) {
                    holder.itemView.animate()
                            .alpha(0f).scaleX(0.8f).scaleY(0.8f)
                            .setDuration(300)
                            .withEndAction(() -> {

                                // Tells the fragment which task was clicked
                                if (listener != null) {
                                    listener.onTaskComplete(currentPos);
                                }

                                taskList.remove(currentPos);
                                notifyItemRemoved(currentPos);
                                Toast.makeText(holder.itemView.getContext(), "Mission Accomplished!", Toast.LENGTH_SHORT).show();
                            }).start();
                }
            }
        });

        // Share button — fires an implicit intent so the user can share the task title + due date
        // to any app that accepts text (Messages, Gmail, WhatsApp, etc.)
        if (holder.btnShare != null) {
            holder.btnShare.setOnClickListener(v -> {
                String shareText = "Task: " + task.title + "\nDue: " + task.date;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                v.getContext().startActivity(Intent.createChooser(shareIntent, "Share Task via"));
            });
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvTitle, tvDesc, tvDate;
        View priorityMarker;
        ImageButton btnShare; // Share button — add id btn_share_task to item_task.xml

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_task);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvDesc = itemView.findViewById(R.id.tv_task_desc);
            tvDate = itemView.findViewById(R.id.tv_task_date);
            priorityMarker = itemView.findViewById(R.id.priority_marker);
            btnShare = itemView.findViewById(R.id.btn_share_task); // add this to item_task.xml
        }
    }
}