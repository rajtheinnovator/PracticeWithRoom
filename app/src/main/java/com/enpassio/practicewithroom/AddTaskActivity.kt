package com.enpassio.practicewithroom

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import com.enpassio.practicewithroom.database.AppDatabase
import com.enpassio.practicewithroom.database.TaskEntry
import java.util.*


class AddTaskActivity : AppCompatActivity() {

    companion object {
        // Extra for the task ID to be received in the intent
        val EXTRA_TASK_ID = "extraTaskId"
        // Extra for the task ID to be received after rotation
        val INSTANCE_TASK_ID = "instanceTaskId"
    }

    // Constants for priority
    val PRIORITY_HIGH = 1
    val PRIORITY_MEDIUM = 2
    val PRIORITY_LOW = 3
    // Constant for default task id to be used when not in update mode
    private val DEFAULT_TASK_ID = -1
    // Constant for logging
    private val TAG = AddTaskActivity::class.java.simpleName
    // Fields for views
    lateinit var mEditText: EditText
    lateinit var mRadioGroup: RadioGroup
    lateinit var mButton: Button

    private var mTaskId = DEFAULT_TASK_ID

    // Create AppDatabase member variable for the Database
    // Member variable for the Database
    private var mDb: AppDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        initViews()

        // Initialize member variable for the data base
        mDb = AppDatabase.getInstance(applicationContext)

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID)
        }

        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button)
            if (mTaskId === DEFAULT_TASK_ID) {
                // populate the UI
                // Assign the value of EXTRA_TASK_ID in the intent to mTaskId
                // Use DEFAULT_TASK_ID as the default
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID)
                // Get the diskIO Executor from the instance of AppExecutors and
                // call the diskIO execute method with a new Runnable and implement its run method
                AppExecutors.instance.diskIO.execute(Runnable {
                    // Use the loadTaskById method to retrieve the task with id mTaskId and
                    // assign its value to a final TaskEntry variable
                    val task = mDb?.taskDao()?.loadTaskById(mTaskId)
                    // Call the populateUI method with the retrieve tasks
                    // Remember to wrap it in a call to runOnUiThread
                    // We will be able to simplify this once we learn more
                    // about Android Architecture Components
                    runOnUiThread { populateUI(task!!) }
                })
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId)
        super.onSaveInstanceState(outState)
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private fun initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription)
        mRadioGroup = findViewById(R.id.radioGroup)

        mButton = findViewById(R.id.saveButton)
        mButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                onSaveButtonClicked()
            }
        })
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private fun populateUI(task: TaskEntry) {
        // return if the task is null
        if (task == null) {
            return;
        }

        // Use the variable task to populate the UI
        mEditText.setText(task.description);
        setPriorityInViews(task.priority);
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    fun onSaveButtonClicked() {
        // Create a description variable and assign to it the value in the edit text
        val description = mEditText.getText().toString()
        // Create a priority variable and assign the value returned by getPriorityFromViews()
        val priority = getPriorityFromViews()
        // Create a date variable and assign to it the current Date
        val date = Date()


        val task = TaskEntry(description, priority, date)
        // Get the diskIO Executor from the instance of AppExecutors and
        // call the diskIO execute method with a new Runnable and implement its run method
        AppExecutors.instance.diskIO.execute(Runnable {
            // insert the task only if mTaskId matches DEFAULT_TASK_ID
            // Otherwise update it
            // call finish in any case
            if (mTaskId == DEFAULT_TASK_ID) {
                // insert new task
                mDb?.taskDao()?.insertTask(task);
            } else {
                //update task
                task.id = mTaskId;
                mDb?.taskDao()?.updateTask(task);
            }
        })
        finish()
    }

    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    fun getPriorityFromViews(): Int {
        var priority = 1
        val checkedId = (findViewById(R.id.radioGroup) as RadioGroup).checkedRadioButtonId
        when (checkedId) {
            R.id.radButton1 -> priority = PRIORITY_HIGH
            R.id.radButton2 -> priority = PRIORITY_MEDIUM
            R.id.radButton3 -> priority = PRIORITY_LOW
        }
        return priority
    }

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    fun setPriorityInViews(priority: Int) {
        when (priority) {
            PRIORITY_HIGH -> (findViewById(R.id.radioGroup) as RadioGroup).check(R.id.radButton1)
            PRIORITY_MEDIUM -> (findViewById(R.id.radioGroup) as RadioGroup).check(R.id.radButton2)
            PRIORITY_LOW -> (findViewById(R.id.radioGroup) as RadioGroup).check(R.id.radButton3)
        }
    }

}
