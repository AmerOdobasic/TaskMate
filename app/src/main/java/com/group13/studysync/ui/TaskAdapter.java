package com.group13.studysync.ui;

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

    private List<String> taskTitles = new ArrayList<>();

    public void setTasks(List<String> tasks) {
        // We wrap the incoming data in a new ArrayList so we are allowed to delete items from it later
        this.taskTitles = new ArrayList<>(tasks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String title = taskTitles.get(position);
        holder.tvTitle.setText(title);
        holder.tvDate.setText("Today, 13:00");

        // RESET the view. Because RecyclerView recycles old cards as you scroll,
        // we have to make sure new cards don't accidentally load in already shrunk or invisible!
        holder.itemView.setAlpha(1f);
        holder.itemView.setScaleX(1f);
        holder.itemView.setScaleY(1f);
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(false);

        // Listening for the checkbox click
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Get the exact position of the item clicked
                int currentPosition = holder.getAdapterPosition();

                // Make sure the item actually still exists before doing anything
                if (currentPosition != RecyclerView.NO_POSITION) {

                    // Animation (Shrink down to 80% and fade out)
                    holder.itemView.animate()
                            .alpha(0f)
                            .scaleX(0.8f)
                            .scaleY(0.8f)
                            .setDuration(300)
                            .withEndAction(() -> {
                                // Delete it from the actual list
                                taskTitles.remove(currentPosition);
                                notifyItemRemoved(currentPosition);

                                // The Popup (Toast)
                                Toast.makeText(holder.itemView.getContext(), "Task Completed!", Toast.LENGTH_SHORT).show();
                            })
                            .start();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskTitles.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvTitle;
        TextView tvDate;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_task);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvDate = itemView.findViewById(R.id.tv_task_date);
        }
    }
}