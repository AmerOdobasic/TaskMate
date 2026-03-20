package com.group13.studysync.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group13.studysync.R;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskItem> taskList = new ArrayList<>();

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
        holder.tvDate.setText(task.date); // Now it knows what this is!
        holder.priorityMarker.setBackgroundColor(task.color);

        // Reset animation state for recycled views
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
                                taskList.remove(currentPos);
                                notifyItemRemoved(currentPos);
                                Toast.makeText(holder.itemView.getContext(), "Mission Accomplished!", Toast.LENGTH_SHORT).show();
                            }).start();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvTitle, tvDesc, tvDate; // Added tvDate here
        View priorityMarker;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_task);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvDesc = itemView.findViewById(R.id.tv_task_desc);
            tvDate = itemView.findViewById(R.id.tv_task_date); // Hooked it up to the XML here
            priorityMarker = itemView.findViewById(R.id.priority_marker);
        }
    }
}