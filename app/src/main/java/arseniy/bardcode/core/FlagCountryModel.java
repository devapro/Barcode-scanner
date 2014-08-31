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
        hashmap.put(00,new CFlag(R.string.c_us, R.drawable.us));
        hashmap.put(202,new CFlag(R.string.c_us, R.drawable.us));
        hashmap.put(48,new CFlag(R.string.c_ua, R.drawable.ua));
        hashmap.put(59,new CFlag(R.string.c_ru, R.drawable.ru));
    }

    public CFlag getCountry(Integer cod){
        CFlag result = hashmap.get(cod);
        if(result == null){
            Double c = Math.floor(cod/10);
            result = hashmap.get(c.intValue());
        }
        if (result != null)
            return result;
        return new CFlag(R.string.c_default, R.drawable.ru);
    }
}
