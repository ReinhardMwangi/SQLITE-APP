package com.example.midmorningsqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var edtEmail: EditText
    lateinit var edtIdNumber:EditText
    lateinit var btnSave:Button
    lateinit var btnView:Button
    lateinit var btnDelete:Button
    lateinit var db:SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.mEdtName)
        edtEmail = findViewById(R.id.mEdtEmail)
        edtIdNumber = findViewById(R.id.mEdtId)
        btnSave = findViewById(R.id.mbtnSave)
        btnView = findViewById(R.id.mbtnView)
        btnDelete = findViewById(R.id.mbtnDelete)
        // Create a database called eMobilisDB
        db = openOrCreateDatabase("eMobilisDB", Context.MODE_PRIVATE, null)
        // Create a table called users in the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR) ")
        //Set onClick listeners to buttons
        btnSave.setOnClickListener {
            // Receive data from the user
            var name = edtName.text.toString().trim()
            var email = edtEmail.text.toString().trim()
            var idNumber = edtIdNumber.text.toString().trim()
            // Check if the user is submitting empty fields
            if (name.isEmpty()|| email.isEmpty() || idNumber.isEmpty()){
                //Display an error message using the defined message function
                message("EMPTY FIELDS!!!", "Please fill all inputs")
            }else{
                // Proceed to save
                db.execSQL("INSERT INTO users VALUES('"+name+"', '"+email+"', '"+idNumber+"')")
                clear()
                message("SUCESS!!!","User saved successfully")
            }

        }
        btnView.setOnClickListener {
            //Use cursor to select all the records
            var cursor = db.rawQuery("SELECT * FROM users", null)
            // Check if there is any record found
            if (cursor.count == 0){
                message("NO RECORDS!!", "Sorry, no users found!!")
            }else{
                //Use string buffer to append al records to be displayed usong a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n")
                }
                message("USERS", buffer.toString())
            }

        }
        btnDelete.setOnClickListener {
            var idNumber = edtIdNumber.text.toString().trim()
            if (idNumber.isEmpty()){
                message("EMPTY FIELD!!", "Please fill in ID field")
            }else{
                var cursor  = db.rawQuery("SELECT * FROM users WHERE kitambulisho='"+idNumber+"'", null)
                if (cursor.count == 0){
                    message("NO RECORD FOUND!!", "Sorry, there's no user with provided ID")
                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+idNumber+"'")
                        clear()
                        message("SUCCESS!!","User deleted successfully")
                }
            }

        }

    }

    fun message(title:String, message:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Cancel", null)
        alertDialog.create().show()
    }

    fun clear(){
        edtName.setText("")
        edtEmail.setText("")
        edtIdNumber.setText("")
    }
}