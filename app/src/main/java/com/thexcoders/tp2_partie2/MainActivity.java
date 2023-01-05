package com.thexcoders.tp2_partie2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    int PICK_CONTACT_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button detailsContactsBtn = findViewById(R.id.details_con_btn);
        Button contactsBtn = findViewById(R.id.contact_id);
        Button callBtn = findViewById(R.id.call_btn);

        callBtn.setEnabled(false);
        detailsContactsBtn.setEnabled(false);

        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);

                startActivityForResult(intent, PICK_CONTACT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            // Get the URI of the selected contact
            Uri contactUri = data.getData();
            grantUriPermission("com.example.otherapp", contactUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            System.out.println(contactUri.toString());

            // Define a projection (the columns you want to retrieve)
            String[] contactProjection = {ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};

            String contactSelection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = ?";
            String[] contactSelectionArgs = {"1"};

            // Query the content provider to get a cursor with the results
            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                    contactProjection,
                    contactSelection,
                    contactSelectionArgs,
                    null);

            // Use the cursor to retrieve the contact's information
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                // Do something with the contact's information
                Log.d("Contact Information", "ID: " + id + ", Name: " + name + "Phone number: " + phoneNumber);
            }
            cursor.close();
        }
    }
}