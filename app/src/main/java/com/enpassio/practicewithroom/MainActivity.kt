package com.enpassio.practicewithroom

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.LinearLayout.VERTICAL
import com.enpassio.practicewithroom.database.AppDatabase


class MainActivity : AppCompatActivity(), TaskAdapter.ItemClickListener {

    // Constant for logging
    private val TAG = MainActivity::class.java.simpleName
    // Member variables for the adapter and RecyclerView
    private lateinit var mRecyclerView: RecyclerView
    private var mAdapter: TaskAdapter? = null
    private var mDb: AppDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerViewTasks)

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = TaskAdapter(this, this)
        mRecyclerView.adapter = mAdapter

        val decoration = DividerItemDecoration(applicationContext, VERTICAL)
        mRecyclerView.addItemDecoration(decoration)
        mDb = AppDatabase.getInstance(applicationContext)

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            // Called when a user swipes left or right on a ViewHolder
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                // Here is where you'll implement swipe to delete
                val position = viewHolder.adapterPosition
                val task = mAdapter?.mTaskEntries?.get(position)
                val deleteTask = mDb?.taskDao()?.deleteTask(task!!)
                retrieveTasks()
            }
        }).attachToRecyclerView(mRecyclerView)


        val fabButton: FloatingActionButton = findViewById(R.id.fab)

        fabButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                // Create a new intent to start an AddTaskActivity
                val addTaskIntent = Intent(this@MainActivity, AddTaskActivity::class.java)
                startActivity(addTaskIntent)
            }
        })

    }


    override fun onItemClickListener(itemId: Int) {
        // Launch AddTaskActivity adding the itemId as an extra in the intent
    }

    override fun onResume() {
        super.onResume()
        retrieveTasks()
    }

    private fun retrieveTasks() {
        // Get the diskIO Executor from the instance of AppExecutors and
        // call the diskIO execute method with a new Runnable and implement its run method
        AppExecutors.instance.diskIO.execute(Runnable {
            // Move the logic into the run method and
            // Extract the list of tasks to a final variable
            val tasks = mDb?.taskDao()?.loadAllTasks()
            // Wrap the setTask call in a call to runOnUiThread
            // We will be able to simplify this once we learn more
            // about Android Architecture Components
            runOnUiThread { mAdapter?.setTasks(tasks!!) }
        })
    }
}
