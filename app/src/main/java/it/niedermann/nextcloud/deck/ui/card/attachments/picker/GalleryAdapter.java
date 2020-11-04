package it.niedermann.nextcloud.deck.ui.card.attachments.picker;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import it.niedermann.nextcloud.deck.databinding.ItemAttachmentImageBinding;
import it.niedermann.nextcloud.deck.databinding.ItemPhotoPreviewBinding;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.Q;
import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class GalleryAdapter extends AbstractPickerAdapter<RecyclerView.ViewHolder> {

    @NonNull
    private final ExecutorService bitmapExecutor = Executors.newCachedThreadPool();
    @NonNull
    private final LifecycleOwner lifecycleOwner;

    @SuppressLint("InlinedApi")
    private static final String sortOrder = (SDK_INT >= Q)
            ? MediaStore.Images.Media.DATE_TAKEN
            : MediaStore.Images.Media.DATE_ADDED;

    public GalleryAdapter(@NonNull Context context, @NonNull Consumer<Uri> onSelect, @NonNull Runnable onSelectPicker, @NonNull LifecycleOwner lifecycleOwner) {
        super(context, onSelect, onSelectPicker, EXTERNAL_CONTENT_URI, _ID, sortOrder + " DESC");
        this.lifecycleOwner = lifecycleOwner;
        notifyItemRangeInserted(0, getItemCount());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM_PICKER:
                return new GalleryPickerItemViewHolder(ItemPhotoPreviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            case VIEW_TYPE_ITEM:
                return new GalleryItemViewHolder(ItemAttachmentImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            default:
                throw new IllegalStateException("Unknown viewType " + viewType);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM_PICKER: {
                ((GalleryPickerItemViewHolder) holder).bind(onSelectPicker, lifecycleOwner);
                break;
            }
            case VIEW_TYPE_ITEM: {
                bitmapExecutor.execute(() -> {
                    final long id = getItemId(position);
                    try {
                        final Bitmap thumbnail;
                        if (SDK_INT >= Q) {
                            thumbnail = contentResolver.loadThumbnail(ContentUris.withAppendedId(
                                    EXTERNAL_CONTENT_URI, id), new Size(512, 384), null);
                        } else {
                            thumbnail = MediaStore.Images.Thumbnails.getThumbnail(
                                    contentResolver, id,
                                    MediaStore.Images.Thumbnails.MINI_KIND, null);
                        }
                        new Handler(Looper.getMainLooper()).post(() -> ((GalleryItemViewHolder) holder).bind(ContentUris.withAppendedId(
                                EXTERNAL_CONTENT_URI, id), thumbnail, onSelect));
                    } catch (IOException ignored) {
                        new Handler(Looper.getMainLooper()).post(((GalleryItemViewHolder) holder)::bindError);
                    }
                });
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder instanceof GalleryPickerItemViewHolder) {
            ((GalleryPickerItemViewHolder) holder).unbind();
        }
    }
}
