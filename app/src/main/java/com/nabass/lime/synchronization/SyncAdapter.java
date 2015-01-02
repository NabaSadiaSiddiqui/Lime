/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nabass.lime.synchronization;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.nabass.lime.db.DBConstants;
import com.nabass.lime.db.TBLMsgs;

/**
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String TAG = "SyncAdapter";

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.e(TAG, "Beginning network synchronization");
        Log.e(TAG, "Account name that started this action is " + account.name);
        Log.e(TAG, "Sync action triggered by authority: " + authority);
        Log.e(TAG, "Get all unsynced messages");
        Cursor c=null;
        String selection = DBConstants.TBL_MSGS_COLS.COL_SYNCED + "=0";
        try {
            c = provider.query(DBConstants.DB_MSGS, null, selection, null, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(c!=null) {
            while(c.moveToNext()) {
                Log.e(TAG, "From: " + c.getString(c.getColumnIndex(DBConstants.TBL_MSGS_COLS.COL_FROM)));
                Log.e(TAG, "Msg: " + new String(c.getBlob(c.getColumnIndex(DBConstants.TBL_MSGS_COLS.COL_MSG))));
                Log.e(TAG, "Msg type: " + c.getString(c.getColumnIndex(DBConstants.TBL_MSGS_COLS.COL_MSG_TYPE)));
                Log.e(TAG, "Timestamp: " + c.getString(c.getColumnIndex(DBConstants.TBL_MSGS_COLS.COL_TIME)));
            }
        }
        c.close();
        Log.e(TAG, "Update msg statuses to synched");
        ContentValues values = new ContentValues(1);
        values.put(DBConstants.TBL_MSGS_COLS.COL_SYNCED, "1");
        try {
            provider.update(DBConstants.DB_MSGS, values, null, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Network synchronization complete");
    }
}