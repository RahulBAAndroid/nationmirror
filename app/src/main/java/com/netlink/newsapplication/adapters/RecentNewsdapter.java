package com.netlink.newsapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.netlink.newsapplication.FullRecentNewsActivity;
import com.netlink.newsapplication.MainActivity;
import com.netlink.newsapplication.R;
import com.netlink.newsapplication.models.NewsModel;
import com.netlink.newsapplication.models.NewsModelOld;

import org.jsoup.Jsoup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentNewsdapter extends RecyclerView.Adapter<RecentNewsdapter.CurrentNewsViewHolder> {
    private List<NewsModel> newsList;
    private Context context;
    private int categorynumb;

    public RecentNewsdapter(List<NewsModel>newsList)
    {
     this.newsList = newsList;
    }
    public RecentNewsdapter(List<NewsModel>newsList,int categoryNumb)
    {
     this.newsList = newsList;
     this.categorynumb = categoryNumb;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentNewsViewHolder holder, int position) {
        NewsModel news = newsList.get(position);
//        setNewsColor(holder,news,categorynumb);
//        if (news.getCategories().contains(8827)) {
//            holder.tvTitle.setTextColor(Color.parseColor("#FFC0CB")); // Pink color
//        }
        String cleanTitle = cleanTitleText(news.getTitle().getRendered());
        holder.tvTitle.setText(getStyledTitle(cleanTitle,categorynumb,news));
        holder.tvAuthor.setText(news.getYoastHeadJson().getAuthor());
        holder.tvDate.setText(formatDate(news.getDate()));
        holder.tvDesc.setText(cleanHtmlContent(news.getExcerpt().getRendered()));
        // Load image if available
            String imageUrl = news.getYoastHeadJson().getOgImage().get(0).getUrl();
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imgNews);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(view.getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(view.getContext(), FullRecentNewsActivity.class);
                i.putExtra("title", holder.tvTitle.getText().toString());
                i.putExtra("content", news.getContent().getRendered());
                i.putExtra("image", imageUrl);
                i.putExtra("link",news.getLink());
                i.putExtra("author",holder.tvAuthor.getText());
                i.putExtra("date",holder.tvDate.getText());
                i.putExtra("categoryNumb",categorynumb);
                view.getContext().startActivity(i);
            }
        });
        // WhatsApp Share Button

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
    private void setNewsColor(CurrentNewsViewHolder holder, NewsModel newsModel, int categoryNumb) {
        if (categoryNumb==8823) {
            holder.tvTitle.setTextColor(Color.parseColor("#FF6F00"));
        }
        else if (categoryNumb==8827) {
            holder.tvTitle.setTextColor(Color.parseColor("#FF00FF"));
        }
        else if (categoryNumb==1) {
            holder.tvTitle.setTextColor(Color.parseColor("#2E7D32"));
        }
        else if (categoryNumb==40) {
            holder.tvTitle.setTextColor(Color.parseColor("#D32F2F"));
        }
        else if (categoryNumb==4) {
            holder.tvTitle.setTextColor(Color.parseColor("#F57C00"));
        }
        else if (categoryNumb==13528) {
            holder.tvTitle.setTextColor(Color.parseColor("#9575CD"));
        }
        else if (categoryNumb==9746) {
            holder.tvTitle.setTextColor(Color.parseColor("#424242"));
        }
        else if (categoryNumb==7) {
            holder.tvTitle.setTextColor(Color.parseColor("#B71C1C"));
        }
        else if (categoryNumb==10) {
            holder.tvTitle.setTextColor(Color.parseColor("#880E4F"));
        }
        else if (categoryNumb==8) {
            holder.tvTitle.setTextColor(Color.parseColor("#FF1744"));
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
    @Override
    public int getItemCount() {
        return newsList.size();
    }
    @NonNull
    @Override
    public CurrentNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_card_recentnews, parent, false);

        return new CurrentNewsViewHolder(view);
    }
    public String cleanHtmlContent(String html) {
        return Jsoup.parse(html).text(); // This will extract the plain text
    }

    public class CurrentNewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvAuthor, tvDate;
        ImageView imgNews;
        public CurrentNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            imgNews = itemView.findViewById(R.id.imgRecentNews);

        }
    }
    // Helper method to format the date
    private String formatDate(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }}
}
