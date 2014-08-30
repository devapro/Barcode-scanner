package arseniy.bardcode.core;

import java.util.HashMap;
import java.util.Map;

import arseniy.bardcode.R;
import arseniy.bardcode.util.CFlag;

/**
 * Created by arseniy on 30/08/14.
 */
public class FlagCountryModel {
    public Map<Integer, CFlag> hashmap;

    public FlagCountryModel(){
        hashmap = new HashMap<Integer, CFlag>();
        hashmap.put(00,new CFlag("America", R.drawable.us));
        hashmap.put(59,new CFlag("America", R.drawable.us));
        hashmap.put(48,new CFlag("Ukrainian", R.drawable.ua));
        hashmap.put(46,new CFlag("Russia", R.drawable.ru));
    }

    public CFlag getCountry(Integer cod){
        CFlag result = hashmap.get(cod);
        if(result == null){
            result = hashmap.get(Math.floor(cod/10));
        }
        if (result != null)
            return result;
        return new CFlag("default", R.drawable.ru);
    }
}
