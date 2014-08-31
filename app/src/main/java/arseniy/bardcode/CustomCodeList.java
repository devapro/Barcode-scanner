package arseniy.bardcode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import arseniy.bardcode.core.ScanCodeModel;

/**
 * Класс для списка ранее отсканированных кодов
 */
public class CustomCodeList  extends ArrayAdapter<String> {
    private MyListScan context;
    private String[] nameItem;
    private String[] codeId;

    public CustomCodeList(MyListScan context, String[] nameItem, String[] codeId) {
        super(context, R.layout.custom_list, nameItem);
        this.context = context;
        this.nameItem = nameItem;
        this.codeId = codeId;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_code_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.code);
        txtTitle.setText(this.codeId[position]);
        //кнопка удаления зписи
        ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
               //удаляем элемент
                if(ScanCodeModel.deleteItemByCode(codeId[position], context)){
                    context.beforeDeleteItem();
                }
            }
        });
        return rowView;
    }
}
