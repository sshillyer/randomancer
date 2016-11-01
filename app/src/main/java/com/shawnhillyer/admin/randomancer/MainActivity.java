package com.shawnhillyer.admin.randomancer;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int MENU_ITEM_LOGOUT = 1001;
    private CoordinatorLayout coordinatorLayout;

    private static String webUrl = "http://www.facebook.com";
    private static String email = "shawn.hillyer@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        // Getting text from a text field and using it
//                EditText et = (EditText) findViewById(R.id.nameText);
//                String entry = et.getText().toString();
//                Snackbar.make(view, "You entered " + entry, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                // Sending an email -- not supported?
//                String[] addresses = {email};
//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setData(Uri.parse("mailto:"));
//                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Information Request");
//                intent.putExtra(Intent.EXTRA_TEXT, "Please send some information!");
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                }

            }
        });


        // Lorem ipsum string building stuff
//        StringBuilder builder = new StringBuilder();
//
//        for (int i = 0; i < 10; i++) {
//            builder.append(getString(R.string.lorem_ipsum) + "\n\n");
//        }
//
//        TextView tv = (TextView) findViewById(R.id.longText);
//
//        tv.setText(builder.toString());

        Button button = (Button) findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.nameText);
                String name = et.getText().toString();


                Snackbar.make(v,
                        "Your name is " + name, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // Setting an iamge
        String imageName = "sunset";
        ImageView iv = (ImageView) findViewById(R.id.photo);

        try {
            InputStream stream = getAssets().open(imageName + ".jpg");
            Drawable d = Drawable.createFromStream(stream, null);
            iv.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        int res = getResources().getIdentifier(imageName, "drawable", getPackageName());
//        iv.setImageResource(R.drawable.sunset);
//        iv.setImageResource(res);



// Systematically creating few buttons using code
//        RelativeLayout layout = (RelativeLayout) findViewById(R.id.content_layout);

//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        for (int i = 0; i < 3; i++) {
//            Button button = new Button(this);
//            button.setText("Click me");
//            button.setLayoutParams(params);
//            layout.addView(button);
//        }

        String[] items = getResources().getStringArray(R.array.races);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        items);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(MainActivity.this, "Your orientation is portrait", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Your orientation is landscape", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add(0, MENU_ITEM_LOGOUT, 102, R.string.logout);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("MainActivity", "onStart");
    }

    protected void onResume() {
        super.onResume();

        Log.d("MainActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("MainActivity", "onStop");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement

        switch(id) {
            case R.id.action_settings:
                Snackbar.make(coordinatorLayout,
                        "You selected settings", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            case R.id.action_about:
//                Snackbar.make(coordinatorLayout,
//                        "You selected about", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case MENU_ITEM_LOGOUT:
                Snackbar.make(coordinatorLayout,
                        "You selected logout", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            case R.id.action_cart:
//                Snackbar.make(coordinatorLayout,
//                        "You selected shopping cart", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void buttonClickHandler(View view) {
//        EditText et = (EditText) findViewById(R.id.nameText);
//        String name = et.getText().toString();
//
//
//        Snackbar.make(coordinatorLayout,
//                "Your name is " + name, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//    }
}
