package com.example.bimvendpro;

import android.app.Dialog;
import android.content.Context;

public class MachineAddDialogue extends Dialog {
    private Machine item;
    private MachineAdapter adapter;

    //TODO: machine add , edit dlg, full remains
    public MachineAddDialogue(Context context) {
        super(context);
    }

    public MachineAddDialogue(Context context, Machine item, MachineAdapter adapter) {
        super(context);
        this.item = item;
        this.adapter = adapter;
    }

    public MachineAddDialogue(Context context, MachineAdapter adapter) {
        super(context);
        this.adapter = adapter;
    }
}
