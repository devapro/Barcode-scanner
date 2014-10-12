package arseniy.bardcode;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ErrorReporter;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(
        formKey = "", // This is required for backward compatibility but not used
        formUri = "http://testzp.hol.es/android/r.php" // url for report about crash application
)
/**
 * Created by arseniy on 16/08/14.
 */
public class Bardcode extends Application {

    @Override
    public void onCreate() {
        ACRA.init(this);
        ErrorReporter.getInstance().checkReportsOnApplicationStart();
        super.onCreate();
    }
}
