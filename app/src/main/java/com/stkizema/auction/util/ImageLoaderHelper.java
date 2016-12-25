package com.stkizema.auction.util;

import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;
import com.stkizema.auction.R;

import java.io.IOException;
import java.io.InputStream;


public class ImageLoaderHelper {

    public static final String FILE_SYSTEM_PREF = "file:///";
    public static final String DRAWABLE_PREF = "drawable://";

    private static Context context;

    public static void init(Context c) {
        context = c;

        /**
         *  Get max available VM memory, exceeding this amount will throw an OutOfMemory exception.
         */
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());

        /**
         *  Use 1/8th of the available memory for this memory cache.
         */
        final int memoryCache = maxMemory / 8;
        Log.d("UILHelper", "memoryCache size : " + (float) (memoryCache) / (float) (1024 * 1024) + "MB");

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(100 * 1024 * 1024) // 100 MiB
                .memoryCacheSizePercentage(30)
                .memoryCache(new UsingFreqLimitedMemoryCache(memoryCache))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDecoder(new NutraBaseImageDecoder(true))
                .defaultDisplayImageOptions(
                        new DisplayImageOptions.Builder()
                                .showImageForEmptyUri(R.drawable.auction_hammer)
                                .showImageOnFail(R.drawable.auction_hammer)
                                .build())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple());

        ImageLoader.getInstance().init(config.build());
    }

    private static class NutraBaseImageDecoder extends BaseImageDecoder {

        public NutraBaseImageDecoder(boolean loggingEnabled) {
            super(loggingEnabled);
        }

        @Override
        protected InputStream getImageStream(ImageDecodingInfo decodingInfo) throws IOException {
            InputStream stream = decodingInfo.getDownloader()
                    .getStream(decodingInfo.getImageUri(), decodingInfo.getExtraForDownloader());
            return stream == null ? null : new JpegClosedInputStream(stream);
        }

        private class JpegClosedInputStream extends InputStream {

            private static final int JPEG_EOI_1 = 0xFF;
            private static final int JPEG_EOI_2 = 0xD9;
            private final InputStream inputStream;
            private int bytesPastEnd;

            private JpegClosedInputStream(final InputStream iInputStream) {
                inputStream = iInputStream;
                bytesPastEnd = 0;
            }

            @Override
            public int read() throws IOException {
                int buffer = inputStream.read();
                if (buffer == -1) {
                    if (bytesPastEnd > 0) {
                        buffer = JPEG_EOI_2;
                    } else {
                        ++bytesPastEnd;
                        buffer = JPEG_EOI_1;
                    }
                }

                return buffer;
            }
        }
    }


}

