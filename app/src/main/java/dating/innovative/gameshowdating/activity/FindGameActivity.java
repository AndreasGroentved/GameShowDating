package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import dating.innovative.gameshowdating.R;

public class FindGameActivity extends Activity {

    Button findGameButton;
    CheckBox lookingForMalesCheckBox;
    CheckBox lookingForFemalesCheckBox;
    RadioButton judgeRadioButton;
    RadioButton beJudgedRadioButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_game);

        findGameButton = (Button) findViewById(R.id.findGameSearchButton);
        lookingForMalesCheckBox = (CheckBox) findViewById(R.id.findGame_male_checkbox);
        lookingForFemalesCheckBox = (CheckBox) findViewById(R.id.findGame_checkbox_female);
        judgeRadioButton = (RadioButton) findViewById(R.id.findGame_radio_judge);
        beJudgedRadioButton = (RadioButton) findViewById(R.id.findGame_radio_be_judged);

        findGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((lookingForMalesCheckBox.isChecked() || lookingForFemalesCheckBox.isChecked()) &&  (beJudgedRadioButton.isChecked() || judgeRadioButton.isChecked())){
                    Intent findGameIntent = new Intent(getApplicationContext(), InQueueActivity.class);
                    findGameIntent.putExtra("maleCheckBox", lookingForMalesCheckBox.isChecked());
                    findGameIntent.putExtra("femaleCheckBox", lookingForFemalesCheckBox.isChecked());
                    findGameIntent.putExtra("beJudged", beJudgedRadioButton.isChecked());
                    findGameIntent.putExtra("judge", judgeRadioButton.isChecked());
                    startActivity(findGameIntent);
                }

            }
        });
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.findGame_radio_judge:
                if(checked){
                    judgeRadioButton.setChecked(true);
                    beJudgedRadioButton.setChecked(false);
                }
                break;
            case R.id.findGame_radio_be_judged:
                if(checked){
                    beJudgedRadioButton.setChecked(true);
                    judgeRadioButton.setChecked(false);
                }
        }
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.findGame_checkbox_female:
                if (checked)
                lookingForFemalesCheckBox.setChecked(true);
            else
                lookingForFemalesCheckBox.setChecked(false);
                break;
            case R.id.findGame_male_checkbox:
                if (checked)
                lookingForMalesCheckBox.setChecked(true);
            else
                lookingForMalesCheckBox.setChecked(false);
                break;
        }
    }
}
