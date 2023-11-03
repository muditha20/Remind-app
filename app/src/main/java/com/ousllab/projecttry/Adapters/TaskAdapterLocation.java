package com.ousllab.projecttry.Adapters;


import static com.ousllab.projecttry.Services.LocationTrackerService.locationModelArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ousllab.projecttry.Activities.DatabaseHelper;
import com.ousllab.projecttry.Model.TaskModel;
import com.ousllab.projecttry.R;

import java.util.ArrayList;

public class TaskAdapterLocation extends RecyclerView.Adapter<TaskAdapterLocation.ViewHolder> {


    private LayoutInflater mInflater;
    Activity activity;
    ArrayList<TaskModel> taskModelArraylist;
    DatabaseHelper databaseHelper;

    public TaskAdapterLocation(Activity activity, DatabaseHelper databaseHelper) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity=activity;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.taskView.setText(taskModelArraylist.get(position).getTaskName());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLocationInMap(taskModelArraylist.get(position).getLatitude(),
                        taskModelArraylist.get(position).getLongitude(),
                        taskModelArraylist.get(position).getTaskName());
            }
        });

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
                input.setText(taskModelArraylist.get(position).getTaskName());

                // OK button
                builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String taskName = input.getText().toString().trim();
                        if (!taskName.isEmpty()) {

                            TaskModel taskModel=
                                    new TaskModel(taskModelArraylist.get(position).getId(),taskName, taskModelArraylist.get(position).getLatitude(), taskModelArraylist.get(position).getLongitude());

                            databaseHelper.updateTask(taskModel);
                            taskModelArraylist.set(position,taskModel);
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

                // Alert Dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        holder.tvDirections.setVisibility(View.VISIBLE);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete task
                databaseHelper.deleteTaskById(taskModelArraylist.get(position).getId());
                taskModelArraylist.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationModelArrayList.size();
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
        TextView taskView,tvDirections;
        CardView cardview;
        ImageView imgEdit, imgDelete;
        ViewHolder(View itemView,int i) {
            super(itemView);
            taskView = itemView.findViewById(R.id.taskViewTitle);
            cardview = itemView.findViewById(R.id.cardview);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            tvDirections = itemView.findViewById(R.id.tvDirections);
            taskModelArraylist =locationModelArrayList;

        }
    }

    private void openLocationInMap(double latitude, double longitude,String s) {
        String strUri =
                "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + "(" + s + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));
        activity.startActivity(intent);
    }
}
