package jp.co.iisnet.calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private EditText numperInput1;
    private EditText numperInput2;
    private Spinner operatorSelector;
    private TextView calcResult;

    private static final int REQUEST_CODE_ANOTHER_CALC_1 = 1;
    private static final int REQUEST_CODE_ANOTHER_CALC_2 = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numperInput1=(EditText)findViewById(R.id.numInput1) ;
        numperInput1.addTextChangedListener(this);

        numperInput2=(EditText)findViewById(R.id.numInput2) ;
        numperInput2.addTextChangedListener(this);

        operatorSelector = (Spinner)findViewById(R.id.opSel1);
        calcResult=(TextView)findViewById(R.id.calcResult);

        findViewById(R.id.calcBtn1).setOnClickListener(this);
        findViewById(R.id.calcBtn2).setOnClickListener(this);
        findViewById(R.id.calcBtn3).setOnClickListener(this);

    }

    // 入力チェック
    private boolean checkEditTextInput(){
        String input1 = numperInput1.getText().toString();
        String input2 = numperInput2.getText().toString();

        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2);
    }

    @Override
    public void beforeTextChanged(CharSequence s,int start, int count,int after) {

    }

    @Override
    public void onTextChanged(CharSequence s,int start, int count,int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        refreshResult();
    }

    // 計算結果の表示を更新する
    private void refreshResult(){
        if (checkEditTextInput()) {
            int result = calc();

            String resultText=getString(R.string.calc_result_text,result);
            calcResult.setText(resultText);

        } else {
            calcResult.setText(R.string.calc_result_default);
        }
    }

    // 計算する
    private int calc(){

        String input1 = numperInput1.getText().toString();
        String input2 = numperInput2.getText().toString();

        int number1 = Integer.parseInt(input1);
        int number2 = Integer.parseInt(input2);

        int operator = operatorSelector.getSelectedItemPosition();

        switch(operator){
            case 0:
                return number1 + number2;
            case 1:
                return number1 - number2;
            case 2:
                return number1 * number2;
            case 3:
                return number1 / number2;
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.calcBtn1:
                Intent intent1 = new Intent(this,AnotherCalcActivity.class);
                startActivityForResult(intent1,REQUEST_CODE_ANOTHER_CALC_1);
                break;

            case R.id.calcBtn2:
                Intent intent2 = new Intent(this,AnotherCalcActivity.class);
                startActivityForResult(intent2,REQUEST_CODE_ANOTHER_CALC_2);
                break;

            case R.id.calcBtn3:
                if (checkEditTextInput()){
                    int result = calc();
                    numperInput1.setText(String.valueOf(result));
                    refreshResult();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode != RESULT_OK) return;

        // 結果データセットを取り出す
        Bundle resultBundle = data.getExtras();

        // 所定のキーが含まれない場合はなにもしない
        if (!resultBundle.containsKey("result")) return;

        // 対応する値を取り出す
        int result = resultBundle.getInt("result");

        if (requestCode == REQUEST_CODE_ANOTHER_CALC_1) {
            numperInput1.setText(String.valueOf(result));
        } else {
            numperInput2.setText(String.valueOf(result));
        }
        refreshResult();
    }
}
