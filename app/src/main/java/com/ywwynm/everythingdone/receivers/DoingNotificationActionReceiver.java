package com.ywwynm.everythingdone.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;

import com.ywwynm.everythingdone.App;
import com.ywwynm.everythingdone.Def;
import com.ywwynm.everythingdone.helpers.RemoteActionHelper;
import com.ywwynm.everythingdone.model.Thing;
import com.ywwynm.everythingdone.services.DoingService;

/**
 * Created by qiizhang on 2016/11/3.
 * receiver for doing notification actions
 */
public class DoingNotificationActionReceiver extends BroadcastReceiver {

    public static final String TAG = "DoingNotificationActionReceiver";

    public static final String ACTION_FINISH       = TAG + ".finish";
    public static final String ACTION_CANCEL       = TAG + ".cancel";
    public static final String ACTION_STOP_SERVICE = TAG + ".stop_service";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!ACTION_FINISH.equals(action)
                && !ACTION_CANCEL.equals(action)
                && !ACTION_STOP_SERVICE.equals(action)) {
            return;
        }

        if (ACTION_FINISH.equals(action)) {
            long thingId = intent.getLongExtra(Def.Communication.KEY_ID, -1L);
            Pair<Thing, Integer> pair = App.getThingAndPosition(context, thingId, -1);
            Thing thing = pair.first;
            if (thing != null) {
                @Thing.Type int thingType = thing.getType();
                if (thingType == Thing.HABIT) {
                    RemoteActionHelper.finishHabitOnce(
                            context, thing, pair.second, System.currentTimeMillis());
                } else {
                    RemoteActionHelper.finishReminder(context, thing, pair.second);
                }
            }
        }

        context.sendBroadcast(new Intent(action));
        context.stopService(new Intent(context, DoingService.class));
    }
}