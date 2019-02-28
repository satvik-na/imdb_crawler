import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.util.ArrayList;
import java.util.List;
public class Film_info
{
    public static String get_ratings (Document film_page)
    {
        String rating_no = film_page.getElementsByAttributeValue("itemprop", "ratingCount").get(0).text();
        return rating_no;
        //this gets no of ratings
    }
    public static String get_director (Document film_page)
    {
        Element director_ele = film_page.getElementsByClass("credit_summary_item").get(0);
        director_ele = director_ele.child(1);
        //this gets director, since the actual name of the director is 2nd child of element
        String director_text = director_ele.text();
        return director_text;
    }
    public static String get_length (Document film_page)
    {
        Element length_ele = film_page.getElementsByAttribute("datetime").get(0);
        //gets the length of the movie via "datetime" attribute
        String length_text = length_ele.text();
        return length_text;
    }
    public static String get_censor (Document film_page)
    {
        Element subtext = film_page.getElementsByClass("subtext").get(0);
        //the 1st item in subtext is always censor board rating
        String censor_text = subtext.ownText();
        censor_text = censor_text.replace(",", "");
        //replace unnecessary commas
        return censor_text;
    }
    public static String get_release (Document film_page)
    {
        Element release_ele = film_page.getElementsByAttributeValue("title","See more release dates").get(0);
        //the release date element has the attribute "title" and the value is given above
        String release_text = release_ele.text();
        return release_text;
    }
    public static List<String> get_genres (Document film_page)
    {
        Element subtext = film_page.getElementsByClass("subtext").get(0);
        List<String> a = new ArrayList<>();
        Elements genres_ele = subtext.getElementsByTag("a");
        //this is the 1st genre
        for (int i = 0; i < genres_ele.size()-1; i++) {
            Element genre = genres_ele.get(i);
            String genre_text = genre.text();
            a.add(genre_text);
        }
        return a;
    }
    public static List<String> get_writers (Document film_page)
    {
        List<String> a = new ArrayList<>();
        Element writers_ele = film_page.getElementsByClass("credit_summary_item").get(1);
        //the 2nd credit summary item has writers
        Elements writers = writers_ele.getElementsByTag("a");
        for (Element writer:writers)
        {
            String writer_text = writer.text();
            a.add(writer_text);
        }
        return a;
    }
    public static List<String> get_stars (Document film_page)
    {
        List<String> a = new ArrayList<>();
        Element stars_element = film_page.getElementsByClass("credit_summary_item").get(2);
        //the 3rd credit summary item has stars
        Elements stars = stars_element.getElementsByTag("a");
        for (int i = 0; i < stars.size()-1; i++) {
            //loop has one index less because the last thing is 'see full cast'
            String star_text = stars.get(i).text();
            a.add(star_text);
        }
        return a;
    }

}
