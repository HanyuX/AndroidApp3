package com.example.xuehanyu.stressmeter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {

    private static final String TAG = "image_uri";

    private GridView gridView;
    private Button button;
    private List<Integer> mImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview);
        mImage = getImageList();
        gridView.setAdapter(new ImageAdapter(getActivity()));

        button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImage = getImageList();
                gridView.setAdapter(new ImageAdapter(getActivity()));
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(TAG, mImage.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return rootView;
    }




    public List<Integer> getImageList() {
        List<Integer> all = new ArrayList<Integer>();
        Field[] fields = R.drawable.class.getFields();
        try {
            for (Field field : fields) {
                if (field.getName().startsWith("psm"))
                    all.add(field.getInt(R.drawable.class));
            }
        }
        catch (IllegalAccessException e) {
            return null;
        }

        Log.d("a", all.size() + "");
        Random random = new Random();
        List<Integer> mImage = new ArrayList<Integer>();
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(all.size());
            if (set.contains(index)) {
                i--;
                continue;
            }
            else {
                set.add(index);
                mImage.add(all.get(index));
            }
        }
        return mImage;
    }

    class ImageAdapter extends BaseAdapter {
        Context context;
        public ImageAdapter(Context context) {
            this.context = context;
        }
        @Override
        public int getCount() {
            return mImage.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (null == convertView) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                imageView.setPadding(0, 0, 0, 0);
            }
            else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(mImage.get(position));
            return imageView;
        }
    }
}
