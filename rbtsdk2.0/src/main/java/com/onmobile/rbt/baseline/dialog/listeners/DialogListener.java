package com.onmobile.rbt.baseline.dialog.listeners;

import android.content.DialogInterface;

/**
 * Created by shahbaz on 10/12/2018.
 *
 * @author Shahbaz Akhtar
 */
public interface DialogListener {

    /**
     * Represents  a positive button
     *
     * @param dialog DialogInterface
     * @param id id
     */
    void PositiveButton(DialogInterface dialog, int id);

    /**
     * Represents  a negative button
     *
     * @param dialog DialogInterface
     * @param id id
     */
    void NegativeButton(DialogInterface dialog, int id);

    /**
     * Represents dismiss of dialog
     *
     * @param dialog DialogInterface
     */
    default void Dismiss(DialogInterface dialog){

    }
}