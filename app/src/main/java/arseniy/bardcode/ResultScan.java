package arseniy.bardcode;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_res_scan);
        cod = getIntent().getStringExtra("cod");
        format = getIntent().getStringExtra("format");
        //заносим код в базу данных
        ScanCodeModel.addCode(cod, format, this);

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
    }
}
