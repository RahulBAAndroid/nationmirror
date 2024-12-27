package com.netlink.newsapplication.adapters;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.netlink.newsapplication.FullRecentNewsActivity;
import com.netlink.newsapplication.R;
import com.netlink.newsapplication.models.NewsModel;
import com.netlink.newsapplication.utiles.NetworkUtils;

import org.jsoup.Jsoup;

import java.util.List;

public class NewRecommendedNewsAdapter extends RecyclerView.Adapter<NewRecommendedNewsAdapter.RecomendedNewsViewHolder> {
    private List<NewsModel> newsList;
    private Context context;
    private int categoryNumb;
    public NewRecommendedNewsAdapter(List<NewsModel> newsList)
    {
       this.newsList = newsList;

    }
    public NewRecommendedNewsAdapter(List<NewsModel> newsList,int categoryNumb)
    {
        this.newsList = newsList;
        this.categoryNumb = categoryNumb;
    }

    public NewRecommendedNewsAdapter() {
    }

    @NonNull
    @Override
    public RecomendedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_recomended_card, parent, false);
        return new NewRecommendedNewsAdapter.RecomendedNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecomendedNewsViewHolder holder, int position) {
        NewsModel newsModel = newsList.get(position);
//        setNewsColor(holder,newsModel,categoryNumb);
            //        holder.tvRecommended.setText(cleanHtmlContent(newsModel.getExcerpt().getRendered()));
           String cleanTitle = cleanTitleText(newsModel.getTitle().getRendered());
            holder.tvTitle.setText(getStyledTitle(cleanTitle,categoryNumb,newsModel));
            String imageUrl = newsModel.getYoastHeadJson().getOgImage().get(0).getUrl();
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imgRecomended);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Toast.makeText(view.getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(view.getContext(), FullRecentNewsActivity.class);
                    i.putExtra("title", holder.tvTitle.getText().toString());
                    i.putExtra("content", newsModel.getContent().getRendered());
                    i.putExtra("image", imageUrl);
                    view.getContext().startActivity(i);
                }
            });



    }

    private void setNewsColor(RecomendedNewsViewHolder holder, NewsModel newsModel,int categoryNumb) {
        if (categoryNumb==8823) {
            holder.tvTitle.setTextColor(Color.parseColor("#FF6F00"));// oRANGE color
        }
        else if (categoryNumb==8827) {
            holder.tvTitle.setTextColor(Color.parseColor("#FF00FF")); // oRANGE color
        }
        else if (categoryNumb==1) {
            holder.tvTitle.setTextColor(Color.parseColor("#2E7D32")); // oRANGE color
        }
        else if (categoryNumb==40) {
            holder.tvTitle.setTextColor(Color.parseColor("#D32F2F")); // oRANGE color
        }
        else if (categoryNumb==4) {
            holder.tvTitle.setTextColor(Color.parseColor("#F57C00")); // oRANGE color
        }
        else if (categoryNumb==13528) {
            holder.tvTitle.setTextColor(Color.parseColor("#9575CD")); // oRANGE color
        }
        else if (categoryNumb==9746) {
            holder.tvTitle.setTextColor(Color.parseColor("#424242")); // oRANGE color
        }
        else if (categoryNumb==7) {
            holder.tvTitle.setTextColor(Color.parseColor("#B71C1C")); // oRANGE color
        }
        else if (categoryNumb==10) {
            holder.tvTitle.setTextColor(Color.parseColor("#880E4F")); // oRANGE color
        }
        else if (categoryNumb==8) {
            holder.tvTitle.setTextColor(Color.parseColor("#FF1744")); // oRANGE color
        }
        else if (categoryNumb==0) {
            if (newsModel.getCategories().contains(8823)) {
                holder.tvTitle.setTextColor(Color.parseColor("#FF6F00")); // oRANGE color
            }
            else if (newsModel.getCategories().contains(8827)) {
                holder.tvTitle.setTextColor(Color.parseColor("#FF00FF")); // oRANGE color
            }
            else if (newsModel.getCategories().contains(1)) {
                holder.tvTitle.setTextColor(Color.parseColor("#2E7D32")); // oRANGE color
            }
            else if (newsModel.getCategories().contains(40)) {
                holder.tvTitle.setTextColor(Color.parseColor("#D32F2F")); // oRANGE color
            }
            else if (newsModel.getCategories().contains(4)) {
                holder.tvTitle.setTextColor(Color.parseColor("#F57C00")); // oRANGE color
            }
            else if (newsModel.getCategories().contains(13528)) {
                holder.tvTitle.setTextColor(Color.parseColor("#9575CD")); // oRANGE color
            }
            else if (newsModel.getCategories().contains(9746)) {
                holder.tvTitle.setTextColor(Color.parseColor("#424242")); // oRANGE color
            }
            else if (newsModel.getCategories().contains(7)) {
                holder.tvTitle.setTextColor(Color.parseColor("#B71C1C")); // oRANGE color
            }
            else if (newsModel.getCategories().contains(10)) {
                holder.tvTitle.setTextColor(Color.parseColor("#880E4F")); // oRANGE color
            }
            else if (newsModel.getCategories().contains(8)) {
                holder.tvTitle.setTextColor(Color.parseColor("#FF1744")); // oRANGE color
            }
        }
    }
    // Helper method to clean the title
    private String cleanTitleText(String title) {
        // Remove unwanted characters like "&amp;", "#8817", etc.
        return title.replaceAll("amp?;", "") // Remove &...; patterns
                .replaceAll("#\\d+", "") // Remove # followed by numbers
                .trim(); // Remove leading/trailing spaces
    }

    private CharSequence getStyledTitle(String title, int categoryNumb,NewsModel newsModel) {
        int index = title.indexOf(":");
        SpannableString spannableTitle = new SpannableString(title);
        // Determine color based on categoryNumb
        int color = getColorForCategory(categoryNumb,newsModel);
        if (index != -1) {
            // Set the color till the ':' character
            spannableTitle.setSpan(
                    new ForegroundColorSpan(color),
                    0,
                    index + 1, // Include the ':' character
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

        } else {
            // Set the color for the entire text if ':' is not present
            spannableTitle.setSpan(
                    new ForegroundColorSpan(color),
                    0,
                    title.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        return spannableTitle;
    }

    // Helper method to map categoryNumb to colors
    private int getColorForCategory(int categoryNumb,NewsModel newsModel) {
        if (categoryNumb == 0 && newsModel != null) {
            if (newsModel.getCategories().contains(8823)) {
                return Color.parseColor("#FF6F00"); // Orange
            } else if (newsModel.getCategories().contains(8827)) {
                return Color.parseColor("#FF00FF"); // Pink
            } else if (newsModel.getCategories().contains(1)) {
                return Color.parseColor("#2E7D32"); // Green
            } else if (newsModel.getCategories().contains(40)) {
                return Color.parseColor("#D32F2F"); // Red
            } else if (newsModel.getCategories().contains(4)) {
                return Color.parseColor("#F57C00"); // Amber
            } else if (newsModel.getCategories().contains(13528)) {
                return Color.parseColor("#9575CD"); // Purple
            } else if (newsModel.getCategories().contains(9746)) {
                return Color.parseColor("#424242"); // Gray
            } else if (newsModel.getCategories().contains(7)) {
                return Color.parseColor("#B71C1C"); // Dark Red
            } else if (newsModel.getCategories().contains(10)) {
                return Color.parseColor("#880E4F"); // Deep Purple
            } else if (newsModel.getCategories().contains(8)) {
                return Color.parseColor("#FF1744"); // Bright Red
            }
            // Default color if no category matches
            return Color.BLACK;
        } else {
            // Switch for other category numbers
            switch (categoryNumb) {
                case 8823:
                    return Color.parseColor("#FF6F00"); // Orange
                case 8827:
                    return Color.parseColor("#FF00FF"); // Pink
                case 1:
                    return Color.parseColor("#2E7D32"); // Green
                case 40:
                    return Color.parseColor("#D32F2F"); // Red
                case 4:
                    return Color.parseColor("#F57C00"); // Amber
                case 13528:
                    return Color.parseColor("#9575CD"); // Purple
                case 9746:
                    return Color.parseColor("#424242"); // Gray
                case 7:
                    return Color.parseColor("#B71C1C"); // Dark Red
                case 10:
                    return Color.parseColor("#880E4F"); // Deep Purple
                case 8:
                    return Color.parseColor("#FF1744"); // Bright Red
                default:
                    return Color.BLACK; // Default color
            }
        }
    }
    @Override
    public int getItemCount() {
        if(categoryNumb==0){return 10;}
        else return 50;
    }
    private void showConnectToInternetDialog() {
        new AlertDialog.Builder(context.getApplicationContext())
                .setTitle("No Internet Connection")
                .setMessage("Please connect to the internet to get the latest news.")
                .setPositiveButton("Turn On Internet", (dialog, which) -> openInternetSettings())
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Open internet settings
    private void openInternetSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        context.startActivity(intent);
    }
    public String cleanHtmlContent(String html) {
        return Jsoup.parse(html).text(); // This will extract the plain text
    }
    class RecomendedNewsViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvRecommended,tvTitle;
        ImageView imgRecomended;
        public RecomendedNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecommended = itemView.findViewById(R.id.tvRecomendedNews);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgRecomended = itemView.findViewById(R.id.imgRecomended);
        }
    }
}
