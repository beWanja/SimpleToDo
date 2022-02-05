package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.ButtonBarLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongCliskListener = object: TaskItemAdapter.onLongClickListener{
            override fun onItemLongClicked(position: Int) {
                //1. Remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter that data has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        //1. To detect when the user clicks on the Add button
  //      findViewById<Button>(R.id.button).setOnClickListener{
  //          //Code in here is executed when the user clicks on a button
  //          Log.i("Beauttah", "User clicked on a button")
  //      }
        loadItems()

        //Look up the recyclerView in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongCliskListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Set up the button and input field so that the user can enter and add the task
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Get a reference to the button and set a onClickListener
        findViewById<Button>(R.id.button).setOnClickListener{

            //1. Grab the text that the user has imported on @+id/addTaskField
            val userInputTask = inputTextField.text.toString()

            //2. Add the String to out list of tasks : listOfTasks
            listOfTasks.add(userInputTask)
            //Notify the adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3.Reset the text field
            inputTextField.setText("")

            saveItems()

        }

    }
    // Save the user inputted data by reading and writing from a file

    // Get the data file we need
    fun getDataFile(): File {
        // Every line will represent a specific task
        return File(filesDir, "data.text")
    }

    // Load the items by reading every line in our file
    fun loadItems(){
        try{
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch(ioException: IOException){
            ioException.printStackTrace()
        }
    }

    // Save the items by writing them in our file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),listOfTasks)
        }catch (ioException:IOException){
            ioException.printStackTrace()
        }

    }
}