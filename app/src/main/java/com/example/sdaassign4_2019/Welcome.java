package com.example.sdaassign4_2019;



import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass. This is the welcome/landing page
 * for The lLearning Tree Book Club
 *
 * @author Colin Fleck - colin.fleck3@mail.dcu.ie
 * @version 1
 */
public class Welcome extends Fragment {

    /**
     * empty constructor initializes view
     */
    public Welcome() {
        // Required empty public constructor
    }

    /**
     * onCreateView inflates the fragment view for this welcome tab page
     * @param inflater inflates the layout
     * @param container creates the container that holds the fragment
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_welcome, container, false);
        root.getRootView().setBackgroundColor(getResources().getColor(R.color.myColor));

        //Grabs imageView for the background pic
        ImageView bgImage = root.findViewById(R.id.welcomeImage);

        //String holds the link for the background image
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/assign4-colin-fleck.appspot.com/o/reading_tree.png?alt=media&token=8a43b541-e5ab-40fe-8c5e-d30820d9a3c6";

        //glide loads the image and pops it into the imageView
        //ref: https://github.com/bumptech/glide/issues/1529
        Glide.with(root.getRootView())
                 .load(imageUrl)
                 .into(bgImage);

        return root;
    }

}
