package com.example.noli.sphinx;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by User on 24-Apr-17.
 */

public class CustomSpinner extends android.support.v7.widget.AppCompatSpinner {

    public CustomSpinner(Context contextArg){
        super(contextArg);
    }

    public CustomSpinner(Context contextArg, AttributeSet attributeSetArg){
        super(contextArg, attributeSetArg);
    }

    public CustomSpinner(Context contextArg, AttributeSet attributeSetArg, int styleArg){
        super(contextArg, attributeSetArg, styleArg);
    }

    @Override
    public void setSelection(int positionArg){
        boolean samePosition = positionArg == getSelectedItemPosition();
        super.setSelection(positionArg, false);
        // here we modifiy the Spinner's default behavior
        if(samePosition){
            // we dispatch the event, even if the position is the same
            OnItemSelectedListener onItemSelectedListener = getOnItemSelectedListener();
            if(onItemSelectedListener != null){
                onItemSelectedListener.onItemSelected(this,
                        getSelectedView(),
                        positionArg,
                        getSelectedItemId());
            }
        }
    }

    @Override
    public void setSelection(int positionArg, boolean animateArg){
        boolean samePosition = positionArg == getSelectedItemPosition();
        super.setSelection(positionArg, animateArg);
        // here we modifiy the Spinner's default behavior
        if(samePosition){
            // we dispatch the event, even if the position is the same
            OnItemSelectedListener onItemSelectedListener = getOnItemSelectedListener();
            if(onItemSelectedListener != null){
                onItemSelectedListener.onItemSelected(this,
                        getSelectedView(),
                        positionArg,
                        getSelectedItemId());
            }
        }
    }
}
