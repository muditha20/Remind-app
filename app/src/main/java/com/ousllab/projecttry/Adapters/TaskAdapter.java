package com.ousllab.projecttry.Adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ousllab.projecttry.Activities.DatabaseHelper;
import com.ousllab.projecttry.R;
import com.ousllab.projecttry.Model.TaskModel;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    Activity activity;
    List<TaskModel> taskModelArrayList;
    DatabaseHelper databaseHelper;

    public TaskAdapter(Activity activity, List<TaskModel> taskModelArrayList, DatabaseHelper databaseHelper) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity=activity;
        this.taskModelArrayList = taskModelArrayList;
        this.databaseHelper=databaseHelper;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mInflater.inflate(R.layout.layout_recyclerview, parent, false);
       ViewHolder viewHolder=new ViewHolder(view,i);
       return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.taskViewTitle.setText(taskModelArrayList.get(position).getTaskName());
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               //edit task
                AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.AlertDialogCustom);
                builder.setTitle("Edit Task");

                // Edittext
                final EditText input = new EditText(activity);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                input.setText(taskModelArrayList.get(position).getTaskName());

                // OK button
                builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String taskName = input.getText().toString().trim();
                        if (!taskName.isEmpty()) {

                            TaskModel taskModel=
                                    new TaskModel(taskModelArrayList.get(position).getId(),taskName,
                                            taskModelArrayList.get(position).getLatitude(), taskModelArrayList.get(position).getLongitude());
                            databaseHelper.updateTask(taskModel);
                            taskModelArrayList.set(position,taskModel);
                            notifyDataSetChanged();
                        }
                    }
                });

                // Cancel button
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        //delete button action
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete task
                databaseHelper.deleteTaskById(taskModelArrayList.get(position).getId());
                taskModelArrayList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskViewTitle;
        ImageView imgDelete,imgEdit;
        ViewHolder(View itemView,int i) {
            super(itemView);
            taskViewTitle = itemView.findViewById(R.id.taskViewTitle);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);

        }
    }


}
