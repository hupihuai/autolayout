package com.weihu.autolayout.noadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weihu.autolayout.R;


public class NoAdapterActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_adapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new MyAdapter(LayoutInflater.from(this)));
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        private LayoutInflater mInflater;

        public MyAdapter(LayoutInflater inflater) {
            mInflater = inflater;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(mInflater.inflate(R.layout.item_no_adapter, parent, false));
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.setTitle("位置：" + position);
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        class MyHolder extends RecyclerView.ViewHolder {
            private TextView titleView;

            public MyHolder(View itemView) {
                super(itemView);
                titleView = (TextView) itemView.findViewById(R.id.name);
            }

            public void setTitle(String title) {
                titleView.setText(title);

            }
        }
    }
}
