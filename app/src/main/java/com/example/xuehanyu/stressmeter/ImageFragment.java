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

    private static final String TAG = "image_uri";    //mark for the image
    private static final String TAG1 = "position";    //mark for the position

    private GridView gridView;               //Define the grid view as the container of the image
    private Button button;                   //The button for the image
    private List<Integer> mImage;            //list of the image showing in the app
    private int index = 0;                   //the index of the image series

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridview);
        mImage = getImageList(index);
        index++;
        gridView.setAdapter(new ImageAdapter(getActivity()));


        //change the grid view when the button is clicked
        button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImage = getImageList(index);
                index++;
                gridView.setAdapter(new ImageAdapter(getActivity()));
            }
        });

        //send the selected image to the next activity when more images button is clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowImageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(TAG, mImage.get(position));
                bundle.putInt(TAG1, position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return rootView;
    }
    /*
     * get the image list according to the index
     */
    public List<Integer> getImageList(int index) {
        List<Integer> mImage = new ArrayList<Integer>();
        int id = index % 3 + 1;

        //calling the getGridById to get the result
        int []res = PSM.getGridById(id);
        for (int i = 0; i < res.length; i++) {
            mImage.add(res[i]);
        }
        return mImage;
    }

    /*
     * define the adapter for the grid view
     */
    class ImageAdapter extends BaseAdapter {
        Context context;
        public ImageAdapter(Context context) {
            this.context = context;
        }

        /*
         * return the number of the images
         */
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

        /*
         * return the image view
         */
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
