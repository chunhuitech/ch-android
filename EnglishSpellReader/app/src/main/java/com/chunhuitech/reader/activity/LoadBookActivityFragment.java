package com.chunhuitech.reader.activity;

import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunhuitech.reader.App;
import com.chunhuitech.reader.R;
import com.chunhuitech.reader.callback.ILoadDataCallback;
import com.chunhuitech.reader.component.ArrowTextView;
import com.chunhuitech.reader.entity.BaseResult;
import com.chunhuitech.reader.listener.AnimListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoadBookActivityFragment extends Fragment {

    ImageView imageView;
    TextView textView;
    ArrowTextView toolTipText;
    List<Map<String, Object>> listReadData;
    List<Rect> listRects;
    String half;
    float originWidth;
    float originHeight;

    public LoadBookActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        half = getArguments().getString("half");
        //int page = Integer.valueOf(getArguments().getString("page")).intValue();
        //half = page % 2 == 0 ? "left" : "right";
        View view = inflater.inflate(R.layout.fragment_load_book, container, false);
        imageView = view.findViewById(R.id.page_image);
        toolTipText = view.findViewById(R.id.text_tooltip);
        imageView.setOnTouchListener(new ImageOnTouchListener());
        textView = view.findViewById(R.id.temptext);

        loadImage(getArguments().getString("bookId"),
                getArguments().getString("page"),
                getArguments().getString("imageUrl"),
                half);
        App.instanceApp().getDataService().getPageReadInfo(getArguments().getString("id"), new ILoadDataCallback() {
            @Override
            public void loadFinish(BaseResult data) {
                if (data != null) {
                    listReadData = (List<Map<String, Object>>) data.getData().get("dataList");
                    if (listReadData != null && listReadData.size() > 0) {
                        double scaleW = imageView.getWidth() / originWidth;
                        double scaleH = imageView.getHeight() / originHeight;
                        listRects = new ArrayList<>();
                        boolean isLeft = "left".equals(half);
                        for(int i=0; i<listReadData.size(); i++) {
                            int left = Integer.parseInt(listReadData.get(i).get("leftPosition").toString());
                            int top = Integer.parseInt(listReadData.get(i).get("topPosition").toString());
                            int width = Integer.parseInt(listReadData.get(i).get("width").toString());
                            int height = Integer.parseInt(listReadData.get(i).get("height").toString());
                            if (isLeft) {
                                left = (int)(left * scaleW);
                            } else {
                                left = (int)((left - originWidth) * scaleW);
                            }
                            top = (int)(top * scaleH);
                            width = (int)(width * scaleW);
                            height = (int)(height * scaleH);
                            listRects.add(new Rect(left, top, left + width, top + height));
                        }
                    }
                }
            }
        });
        return view;
    }

    private void loadImage(String bookId, String page, String urlImage, String half) {
        Bitmap pngBM = App.instanceApp().getStoreCache().getPageImage(bookId, page, urlImage);
        if (pngBM != null) {
            originWidth = pngBM.getWidth() / 2.0f;
            originHeight = pngBM.getHeight();
            if ("left".equals(half)) {
                Bitmap smallBitmap = Bitmap.createBitmap(pngBM,0,0,pngBM.getWidth()/2,pngBM.getHeight());
                imageView.setImageBitmap(smallBitmap);
            }
            if ("right".equals(half)) {
                Bitmap smallBitmap = Bitmap.createBitmap(pngBM, pngBM.getWidth()/2,0, pngBM.getWidth()/2, pngBM.getHeight());
                imageView.setImageBitmap(smallBitmap);
            }
        }
    }

    private class ImageOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Matrix m = imageView.getImageMatrix();
                float[] values = new float[10];
                m.getValues(values);
                int offsetY = (int) values[5];
                int x = (int)event.getX();
                int y = (int)event.getY() - offsetY;

                int selectIndex = -1;
                if (listRects != null) {
                    for(int i=0;i<listRects.size();i++) {
                        if (listRects.get(i).contains(x, y)) {
                            selectIndex = i;
                        }
                    }
                }

                if (selectIndex > -1) {
                    playSelectedAnim(selectIndex, offsetY);
                }
            }
            return true;
        }

        private void playSelectedAnim(int selectIndex, int offsetY) {
            final Rect selectRect = listRects.get(selectIndex);
            Rect startRect;
            if(textView.getVisibility() == View.INVISIBLE) {
                textView.setWidth(imageView.getWidth());
                textView.setHeight(imageView.getHeight());
                textView.setVisibility(View.VISIBLE);

                startRect = new Rect(0,0,imageView.getWidth(), imageView.getHeight());
            } else {
                startRect = new Rect(textView.getLeft(),textView.getTop(),textView.getWidth(), textView.getHeight());
            }

            final Map<String, Object> selectedObject = listReadData.get(selectIndex);
            Rect endRect = new Rect(selectRect.left,selectRect.top + offsetY,selectRect.right - selectRect.left, selectRect.bottom - selectRect.top);
            ValueAnimator anim = ValueAnimator.ofObject(new RectEvaluator(), startRect, endRect);
            anim.setDuration(500);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Rect rect = (Rect) animation.getAnimatedValue();
                    AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams)textView.getLayoutParams();
                    lp.x = rect.left;
                    lp.y= rect.top;
                    lp.height = rect.bottom;
                    lp.width = rect.right;
                    textView.requestLayout();
                }
            });
            AnimListener animListener = new AnimListener(selectedObject);
            anim.addListener(animListener);
            anim.start();

            showToolTipText(selectedObject, selectRect);
        }

        private void showToolTipText(Map<String, Object> selectedObject, Rect selectRect) {

            Object objText = selectedObject.get("text");
            if (objText != null) {
                // 给侧边留边50
                int padding = 50;
                int maxWidth = imageView.getWidth() - padding;
                String tipText = objText.toString();
                TextPaint textPaint = toolTipText.getPaint();
                int newWidth = (int)textPaint.measureText(tipText);
                toolTipText.setText(tipText);
                toolTipText.setVisibility(View.VISIBLE);

                int left = (selectRect.left + selectRect.right) / 2 - newWidth / 2 - 50;
                if ((left + newWidth) > maxWidth) {
                    left = maxWidth - newWidth;
                }
                if (left < padding) {
                    left = padding;
                }

                AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams)toolTipText.getLayoutParams();
                lp.x = left;
                lp.y= selectRect.top - toolTipText.getHeight();
                textView.requestLayout();
            }


//            ValueAnimator anim = ValueAnimator.ofObject(new RectEvaluator(), startRect, endRect);
//            anim.setDuration(500);
//            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    Rect rect = (Rect) animation.getAnimatedValue();
//                    AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams)textView.getLayoutParams();
//                    lp.x = rect.left;
//                    lp.y= rect.top;
//                    lp.height = rect.bottom;
//                    lp.width = rect.right;
//                    textView.requestLayout();
//                }
//            });
//            AnimListener animListener = new AnimListener(selectedObject);
//            anim.addListener(animListener);
//            anim.start();
        }

    }

}
