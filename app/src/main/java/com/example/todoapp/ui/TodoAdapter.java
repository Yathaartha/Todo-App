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

/**
 * TodoAdapter is the adapter for the RecyclerView in TodoListFragment.
 */
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

    /**
     * Sets the data for the adapter.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo item = data.get(position);
        holder.bind(item);
    }

    /**
     * Sets the data for the adapter.
     * @return The number of items in the data set held by the adapter.
     */
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
            // TODO: Bind the data to the ViewHolder
            titleTextView.setText(todo.getTitle());
            titleTextView.setTextColor(todo.getIsComplete() ? completeColor : incompleteColor);
            detailTextView.setText(todo.getDetail());
            createdDateTextView.setText(getRelativeTimeSpanString(todo.getCreatedAt().getTime()));
            priorityTextView.setText(todo.getPriority().toUpperCase());

            long timeDifference = todo.getDueDate().getTime() - new Date().getTime();
            CharSequence relativeTime;

            // get the relative time for dueDate
            if (timeDifference > 0) {
                String formattedTime = formatElapsedTime(timeDifference / 1000);
                long relativeDays = timeDifference / (1000 * 60 * 60 * 24);
                relativeTime = "in " + relativeDays + " days";
            } else {
                relativeTime = "today";
            }

            dueDateTextView.setText("Due " + relativeTime);// set due date text field

            // set priority text field color
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
            // if there are no todos yet, hide the edit and delete buttons and other fields
            if(titleTextView.getText().equals("No todos yet")){
                createdDateTextView.setVisibility(View.GONE);
                priorityTextView.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                dueDateTextView.setVisibility(View.GONE);
            }
            // open todo detail when pressed on the title
            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    UUID id = data.get(position).getId();
                    Intent intent = TodoPagerActivity.makeIntent(context, id);// create intent to open TodoPagerActivity and pass the id of the todo
                    context.startActivity(intent);
                }
            });

            // delete the todo when pressed on the delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getLayoutPosition();
                    openDialog(view, data.get(position).getId());// open alert dialog box for confirmation
                }
            });

            // open edit todo fragment when pressed on the edit button
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    UUID id = data.get(position).getId();
                    Fragment fragment = EditTodoFragment.newInstance(id);// create fragment to open EditTodoFragment and pass the id of the todo
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
                    todoModel.delete(id);// delete the todo
                    Toast.makeText(context, "Todo deleted", Toast.LENGTH_SHORT).show();
                });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // submitData function to set the data for the adapter
    public void submitData(List<Todo> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
