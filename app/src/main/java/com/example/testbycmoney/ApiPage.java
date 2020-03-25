package com.example.testbycmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testbycmoney.task.CommonTask;
import com.example.testbycmoney.task.ImageTask;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ApiPage extends AppCompatActivity {


    private final String URL = "https://jsonplaceholder.typicode.com/photos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_page);
        setRecyclerView(getText());
    }

    //連接RecyclerView和Adapter
    private void setRecyclerView(List<ApiJsonVO> list) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(new ApiAdapter(list));
    }

    //發出請求獲得Json資料，回傳一個ArrayList<ApiJsonVO>。
    private List<ApiJsonVO> getText() {
        List<ApiJsonVO> apiJsonList = new ArrayList<ApiJsonVO>();
        String jsonInt = null;
        try {
            CommonTask getApiJson = new CommonTask(URL);
            jsonInt = getApiJson.execute(URL).get();
        } catch (Exception e) {
            Log.e("ApiPage.getApiJson:", e.toString());
        }
        try {
            JSONArray jobjarr = new JSONArray(jsonInt);
            for (int i = 0; i < jobjarr.length(); i++) {
                ApiJsonVO ajv = new ApiJsonVO();
                ajv.setId(jobjarr.getJSONObject(i).getString("id"));
                ajv.setTitle(jobjarr.getJSONObject(i).getString("title"));
                ajv.setThumbnailUrl(jobjarr.getJSONObject(i).getString("thumbnailUrl"));
                apiJsonList.add(ajv);
            }
        } catch (JSONException e) {
            Log.e("JSONObject", e.toString());
        }
        return apiJsonList;
    }


    public class ApiAdapter extends RecyclerView.Adapter<ApiAdapter.ViewHolder> {
        @Override
        public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            Logger.e(holder+"already attached");


        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            Logger.e(recyclerView+"already Detached");


        }

        private List<ApiJsonVO> apiJsonList;

        private ApiAdapter(List<ApiJsonVO> apiJsonList) {
            this.apiJsonList = apiJsonList;
        }

        @NonNull
        @Override
        public ApiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_api, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final ApiJsonVO apiJsonVO = apiJsonList.get(position);
            holder.tvApiId.setText(apiJsonVO.getId());
            holder.tvApiTitle.setText(apiJsonVO.getTitle());
//                  ImageTask getImage = new ImageTask(apiJsonVO.getThumbnailUrl(), holder.imageView);  //原本預定要使用每個Json物件中的URL的寫法
            String iurl = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQHSu5jbau6EZiPwxboctWe4IBZ4dzxThkglfSWwsZYcl8PLDsJ";
            ImageTask getImage = new ImageTask(iurl,  holder.imageView);  //因請求圖片網址資料回傳410，才改成其他圖片網址。
            getImage.execute();
        }

        @Override
        public int getItemCount() {
            return apiJsonList.size();
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            super.onViewRecycled(holder);
            Logger.w(holder+"already Recycled");

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvApiTitle, tvApiId;
            private ImageView imageView;
            private int imageSize = getResources().getDisplayMetrics().widthPixels / 4;

            private ViewHolder(View view) {
                super(view);
                tvApiTitle = view.findViewById(R.id.tvApiTitle);
                tvApiId = view.findViewById(R.id.tvApiId);
                imageView = view.findViewById(R.id.imageView);
            }
        }
    }


}
