package com.example.todoapp;

import static android.text.format.DateUtils.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.database.Todo;

import java.util.List;
import java.util.UUID;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<Todo> data;
    private Context context;
    private TodoModel todoModel;
    public TodoAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_todo_item, parent, false);
        context = parent.getContext();
        todoModel = new ViewModelProvider((ViewModelStoreOwner) context).get(TodoModel.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView detailTextView;
        private TextView dateTextView;
        private ImageButton deleteButton;
        private ImageButton editButton;

        private int completeColor = Color.parseColor("#1ECD18");

        private int incompleteColor = Color.parseColor("#FF0000");



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            detailTextView = itemView.findViewById(R.id.detail);
            dateTextView = itemView.findViewById(R.id.created_at);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Todo todo) {
            titleTextView.setText(todo.getTitle());
            titleTextView.setTextColor(todo.getIsComplete() ? completeColor : incompleteColor);
            detailTextView.setText(todo.getDetail());
            dateTextView.setText(getRelativeTimeSpanString(todo.getCreatedAt().getTime()));
            if(titleTextView.getText().equals("No todos yet")){
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            }
            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    UUID id = data.get(position).getId();
                    Log.d("positionId", ""+position);
                    Intent intent = TodoPagerActivity.makeIntent(context, id);
                    context.startActivity(intent);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getLayoutPosition();
                    openDialog(view, data.get(position).getId());
//                    UUID id = data.get(position).getId();
//
//                    todoModel.delete(id);
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    UUID id = data.get(position).getId();
                    Fragment fragment = EditTodoFragment.newInstance(id);
                    FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
                    fm.beginTransaction()
                            .add(R.id.container, fragment)
                            .commit();
                }
            });

        }
    }

    // function that opens alert dialog box
    public void openDialog(View view, UUID id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure you want to delete this todo?");
        alertDialogBuilder.setPositiveButton("Yes",
                (arg0, arg1) -> {
                    todoModel.delete(id);
                });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void submitData(List<Todo> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
