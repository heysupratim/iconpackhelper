package iamsupratim.com.ontime.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by borax12 on 29/12/15.
 */
public class IconPackManager {

    //@Inject
    //private android.app.Application mContext;

    private Context mContext;

    public void setContext (Context c) {
        mContext = c;
    }


    private ArrayList<IconPack> iconPacks = null;

    public List<IconPack> getAvailableIconPacks(boolean forceReload)
    {
        if (iconPacks == null || forceReload)
        {
            iconPacks = new ArrayList<IconPack>();

            HashSet<String> packageNameList = new HashSet<>();

            //adding a first empty object to make the spinner in icon pack selection work easier
            IconPack forcedEntry = new IconPack();
            forcedEntry.packageName="None";
            forcedEntry.name="None";
            iconPacks.add(forcedEntry);

            // find apps with intent-filter "com.gau.go.launcherex.theme" and return build the HashMap
            PackageManager pm = mContext.getPackageManager();

            List<ResolveInfo> adwlauncherthemes = pm.queryIntentActivities(new Intent("org.adw.launcher.THEMES"), PackageManager.GET_META_DATA);
            List<ResolveInfo> golauncherthemes = pm.queryIntentActivities(new Intent("com.gau.go.launcherex.theme"), PackageManager.GET_META_DATA);

            // merge those lists
            List<ResolveInfo> rinfo = new ArrayList<ResolveInfo>(adwlauncherthemes);
            rinfo.addAll(golauncherthemes);

            for(ResolveInfo ri  : rinfo)
            {
                if(!packageNameList.contains(ri.activityInfo.packageName)){
                    IconPack ip = new IconPack();
                    ip.packageName = ri.activityInfo.packageName;

                    ApplicationInfo ai = null;
                    try
                    {
                        ai = pm.getApplicationInfo(ip.packageName, PackageManager.GET_META_DATA);
                        ip.name  = mContext.getPackageManager().getApplicationLabel(ai).toString();
                    }
                    catch (PackageManager.NameNotFoundException e)
                    {
                        // shouldn't happen
                        e.printStackTrace();
                    }

                    iconPacks.add(ip);
                    packageNameList.add(ip.packageName);
                }
            }
        }
        return iconPacks;
    }
}
