package arseniy.bardcode;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by arseniy on 17/07/14.
 */
public class CustomList extends ArrayAdapter<String> {

    private Activity context;
    private final String[] textItem;
    private final Integer[] imageId;
    private ImageView imageView;

    public CustomList(Activity context, String[] textItem, Integer[] imageId) {
        super(context, R.layout.custom_list, textItem);
        this.context = context;
        this.textItem = textItem;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvName);
        this.imageView = (ImageView) rowView.findViewById(R.id.catIcon);
        txtTitle.setText(textItem[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }


}
