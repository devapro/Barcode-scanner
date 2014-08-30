package arseniy.bardcode;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by arseniy on 03/08/14.
 * обрабртка меню
 */
public class MenuWorker implements AdapterView.OnItemClickListener, View.OnClickListener  {
    private Activity Activ;

    public MenuWorker(Activity Activ){
        super();
        this.Activ = Activ;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        Log.d("Menu position",  String.valueOf(position));
        Log.d("Menu id", String.valueOf(id));
        switch (position){
            case 0: intent = new Intent(this.Activ, ZbarScan.class);
                break;
            case 1: intent = new Intent(this.Activ, MyListScan.class);
                break;
            default: intent = new Intent(this.Activ, ZbarScan.class);
        }

       // intent.putExtra(MainActivity.typeId, java.lang.String.valueOf(position));
        this.Activ.startActivity(intent);
    }
}
