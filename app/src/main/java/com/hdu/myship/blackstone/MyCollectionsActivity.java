package com.hdu.myship.blackstone;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

public class MyCollectionsActivity extends AppCompatActivity {
    private SwipeMenuRecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_collections);
        recyclerView= (SwipeMenuRecyclerView) findViewById(R.id.my_collection_recyclerview);
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        recyclerView.setAdapter(new MenuAdapter());
        recyclerView.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {

            }
        });

    }

    private SwipeMenuCreator swipeMenuCreator=new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext()).setBackgroundColor(Color.YELLOW)
                    .setImage(R.mipmap.action_cancel)
                    .setText("删除")
                    .setTextColor(Color.BLUE)
                    .setTextSize(16)
                    .setWidth(200)
                    .setHeight(40);
            swipeRightMenu.addMenuItem(deleteItem);
        }
    };

    public class MenuAdapter extends SwipeMenuAdapter<MenuAdapter.DefaultViewHolder>
    {

        @Override
        public View onCreateContentView(ViewGroup parent, int viewType) {
            View v=View.inflate(parent.getContext(),R.layout.species_content_recycler_view_item,null);

            return v;
        }

        @Override
        public DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {

            return new DefaultViewHolder(realContentView);
        }

        @Override
        public void onBindViewHolder(DefaultViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class DefaultViewHolder extends RecyclerView.ViewHolder {
            public DefaultViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
