package arseniy.bardcode;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import arseniy.bardcode.core.ScanCodeModel;

public class MyListScan extends Activity{
    public CustomCodeList adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_scan);
        this.updateList();
    }

    public void beforeDeleteItem(){
        this.updateList();
        Toast toast = Toast.makeText(this, R.string.success_delete, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void updateList(){
        ScanCodeModel sc = new ScanCodeModel(this);
        //проверяем аличие записей
        if(!sc.isRecord()){
            TextView tv = (TextView) this.findViewById(R.id.emptyList);
            tv.setText(R.string.empty_list);
        }
        ListView lvMain = (ListView) this.findViewById(R.id.lvScan);
        adapter = new CustomCodeList(this, sc.names, sc.codes);
        lvMain.setAdapter(adapter);
    }
}
