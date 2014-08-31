package arseniy.bardcode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import arseniy.bardcode.core.FlagCountryModel;
import arseniy.bardcode.core.ScanCodeModel;
import arseniy.bardcode.util.CFlag;

/**
 * Created by arseniy on 18/08/14.
 */
public class ResultScan extends Activity {
    private  String cod, format;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_res_scan);
        cod = getIntent().getStringExtra("cod");
        format = getIntent().getStringExtra("format");
        //заносим код в базу данных
        index = ScanCodeModel.addCode(cod, format, this);

        TextView result_code = (TextView) findViewById(R.id.result_code);
        result_code.setText(cod);
        //определение страны
        FlagCountryModel flag = new FlagCountryModel();
        cod = cod.substring(0,3);
        CFlag cflag = flag.getCountry(Integer.valueOf(cod));

        TextView country = (TextView) findViewById(R.id.country_code);
        country.setText(cflag.country);

        ImageView imFlag = (ImageView) findViewById(R.id.flagImg);
        imFlag.setImageResource(cflag.prefix);

        ImageView imAdd = (ImageView) findViewById(R.id.add_button);
        imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    public void update(){
        EditText nameField = (EditText) findViewById(R.id.code_name);
        ScanCodeModel.updateCode(nameField.getText().toString(), index, this );
    }
}
