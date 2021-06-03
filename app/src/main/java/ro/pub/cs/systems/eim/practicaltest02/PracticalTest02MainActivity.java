package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText portEditText;
    private EditText hourEditText;
    private EditText minuteEditText;
    private Button setButton;
    private Button resetButton;
    private Button pollButton;
    private TextView dataTextView;

    private class ButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String port = portEditText.getText().toString();
            if (port == null || port.isEmpty()) {
                Toast.makeText(getApplication(), "Port field cannot be empty!", Toast.LENGTH_LONG).show();
                return;
            }
            String hour = hourEditText.getText().toString();
            if (hour == null || hour.isEmpty()) {
                Toast.makeText(getApplication(), "Hour field cannot be empty!", Toast.LENGTH_LONG).show();
                return;
            }
            String minute = minuteEditText.getText().toString();
            if (minute == null || hour.isEmpty()) {
                Toast.makeText(getApplication(), "Minute field cannot be empty!", Toast.LENGTH_LONG).show();
                return;
            }
            switch(view.getId()) {
                case R.id.set_button:
                    new RetrieveTask(setButton, dataTextView).execute(port, hour, minute);
                    break;
                case R.id.reset_button:
                    new RetrieveTask(resetButton, dataTextView).execute(port, hour, minute);
                    break;
                case R.id.poll_button:
                    new RetrieveTask(pollButton, dataTextView).execute(port, hour, minute);
                    break;
            }
        }
    }
    private final ButtonClickListener buttonClickListener = new ButtonClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        portEditText = (EditText)findViewById(R.id.port_edit_text);
        hourEditText = (EditText)findViewById(R.id.hour_edit_text);
        minuteEditText = (EditText)findViewById(R.id.minute_edit_text);

        setButton = (Button)findViewById(R.id.set_button);
        setButton.setOnClickListener(buttonClickListener);
        resetButton = (Button)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(buttonClickListener);
        pollButton = (Button)findViewById(R.id.poll_button);
        pollButton.setOnClickListener(buttonClickListener);

        dataTextView = (TextView)findViewById(R.id.data_text_view);
    }
}