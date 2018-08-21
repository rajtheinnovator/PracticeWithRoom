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


class MainActivity : AppCompatActivity(), TaskAdapter.ItemClickListener {

    // Constant for logging
    private val TAG = MainActivity::class.java.simpleName
    // Member variables for the adapter and RecyclerView
    private lateinit var mRecyclerView: RecyclerView
    private var mAdapter: TaskAdapter? = null

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
}
