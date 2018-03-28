package edu.singaporetech.shactivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class SHActivity extends AppCompatActivity {

    static {
        System.loadLibrary("shanative-lib");
    }

    public native String shaMe(String message, int count);

    /* UI Elements */
    Button buttonSHash;
    EditText textEditMessage;
    TextView textViewDigest;
	NumberPicker numPickerWorkFactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sh);

        buttonSHash = (Button) findViewById(R.id.buttonSHash);
        textEditMessage = (EditText) findViewById(R.id.textEditMessage);
        textViewDigest = (TextView) findViewById(R.id.textViewDigest);
		numPickerWorkFactor = (NumberPicker) findViewById(R.id.numPickerWorkFactor);
		
		numPickerWorkFactor.setMinValue(1);
        numPickerWorkFactor.setMaxValue(30);
		
        buttonSHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				String msg = textEditMessage.getText().toString();
				int numSelected = numPickerWorkFactor.getValue();
				new AsyncTask(textViewDigest, numSelected).execute(msg);
            }
        });
    }

    class AsyncTask extends AsyncTask<String,Void, String> {
        //Weak reference the textview that we need to change later
        WeakReference<TextView> weak_TextView;
		long timeStart = System.currentTimeMillis();
		long timeTaken;
		int power;

        //constructor
        public AsyncTask(TextView targetTextView,int power) {
            this.weak_TextView = new WeakReference<>(targetTextView);
            this.power = power;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... inputs) {
            return shaMe(inputs[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            TextView textViewDigest = weak_TextView.get();
            if(textViewDigest!=null){
                textViewDigest.setText(s);

                timeTaken = System.currentTimeMillis() - timeStart;
                Toast.makeText(thisActivity, "took " + timeTaken + "ms", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
