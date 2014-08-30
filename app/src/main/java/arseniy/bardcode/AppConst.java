package arseniy.bardcode;

/**
 * Created by arseniy on 03/08/14.
 */
public class AppConst {
    //переменные для меню
    public static String[] MainMenu = new String[]{"Сканировать", "Мои штрих коды"};
    public static Integer[] MainMenuIcon = new Integer[]{R.drawable.barcode_reader, R.drawable.star};

    //База данных
    public static String DbName = "listScanBase";
    public static int DbVersion = 2;
    public static String TableScanCodeName = "scanCode";
}
