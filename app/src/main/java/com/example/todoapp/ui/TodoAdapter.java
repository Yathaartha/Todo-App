package com.example.todoapp.ui;

import static android.text.format.DateUtils.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.database.Todo;

import java.util.Date;
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
        private TextView createdDateTextView;
        private TextView priorityTextView;
        private TextView dueDateTextView;
        private ImageButton deleteButton;
        private ImageButton editButton;

        private int completeColor = Color.parseColor("#1ECD18");

        private int incompleteColor = Color.parseColor("#FF0000");



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            detailTextView = itemView.findViewById(R.id.detail);
            createdDateTextView = itemView.findViewById(R.id.created_at);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateText);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Todo todo) {
            titleTextView.setText(todo.getTitle());
            titleTextView.setTextColor(todo.getIsComplete() ? completeColor : incompleteColor);
            detailTextView.setText(todo.getDetail());
            createdDateTextView.setText(getRelativeTimeSpanString(todo.getCreatedAt().getTime()));
            priorityTextView.setText(todo.getPriority().toUpperCase());

            long timeDifference = todo.getDueDate().getTime() - new Date().getTime();
            CharSequence relativeTime;

            if (timeDifference > 0) {
                String formattedTime = formatElapsedTime(timeDifference / 1000);
                long relativeDays = timeDifference / (1000 * 60 * 60 * 24);
                relativeTime = "in " + relativeDays + " days";
            } else {
                relativeTime = "today";
            }

            dueDateTextView.setText("Due " + relativeTime);
            switch (todo.getPriority().toUpperCase()) {
                case "LOW":
                    priorityTextView.setBackgroundColor(Color.parseColor("#1ECD18"));
                    break;
                case "MEDIUM":
                    priorityTextView.setBackgroundColor(Color.parseColor("#FFA500"));
                    break;
                case "HIGH":
                    priorityTextView.setBackgroundColor(Color.parseColor("#FF0000"));
                    break;
                case "URGENT":
                    priorityTextView.setBackgroundColor(Color.parseColor("#8B0000"));
                    break;
                default:
                    priorityTextView.setBackgroundColor(Color.parseColor("#1ECD18"));
                    break;
            }
            if(titleTextView.getText().equals("No todos yet")){
                createdDateTextView.setVisibility(View.GONE);
                priorityTextView.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                dueDateTextView.setVisibility(View.GONE);
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
                    Toast.makeText(context, "Todo deleted", Toast.LENGTH_SHORT).show();
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
