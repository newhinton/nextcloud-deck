package it.niedermann.nextcloud.deck.ui.branding;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import static it.niedermann.nextcloud.deck.DeckApplication.readCurrentAccountColor;
import static it.niedermann.nextcloud.deck.ui.branding.BrandingUtil.getSecondaryForegroundColorDependingOnTheme;

public class BrandedPreferenceCategory extends PreferenceCategory {

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        final View v = holder.itemView.findViewById(android.R.id.title);
        @Nullable final Context context = getContext();
        if (context != null && v instanceof TextView) {
            @ColorInt final int mainColor = getSecondaryForegroundColorDependingOnTheme(context, readCurrentAccountColor(context));
            ((TextView) v).setTextColor(mainColor);
        }
    }

    public BrandedPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BrandedPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BrandedPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrandedPreferenceCategory(Context context) {
        super(context);
    }
}
