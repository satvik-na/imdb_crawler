import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.apache.poi.hssf.usermodel.*;
import java.io.*;
import java.util.List;

public class IMDB_crawler extends Film_info
{
    public static void main(String[] args) throws IOException //this is required
    {
        String filename = "imdb_results.xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("FirstSheet");
        HSSFRow rowhead = sheet.createRow(0);
        rowhead.createCell(0).setCellValue("Serial No.");
        rowhead.createCell(1).setCellValue("Title");
        rowhead.createCell(2).setCellValue("Year");
        rowhead.createCell(3).setCellValue("IMDB Rating");
        rowhead.createCell(4).setCellValue("No. of ratings");
        rowhead.createCell(5).setCellValue("Censor board rating");
        rowhead.createCell(6).setCellValue("Genre 1");
        rowhead.createCell(7).setCellValue("Genre 2");
        rowhead.createCell(8).setCellValue("Genre 3");
        rowhead.createCell(9).setCellValue("Length");
        rowhead.createCell(10).setCellValue("Release date");
        rowhead.createCell(11).setCellValue("Director");
        rowhead.createCell(12).setCellValue("Writer 1");
        rowhead.createCell(13).setCellValue("Writer 2");
        rowhead.createCell(14).setCellValue("Star 1");
        rowhead.createCell(15).setCellValue("Star 2");
        rowhead.createCell(16).setCellValue("Star 3");
        //setting values for the table headers

        Document webpage = Jsoup.connect("https://www.imdb.com/chart/top").get();
        //gets the top 250 movies webpage
        Elements movies = webpage.getElementsByClass("titleColumn");
        //all movies are in "td", with class "title column"
        Elements ratings = webpage.getElementsByClass("ratingColumn imdbRating");
        //this is for the ratings only
        for (int i = 0; i < 250; i++)
        {
            Element movie = movies.get(i);
            Element rating = ratings.get(i);
            //get the 'i'th movie and its rating, out of the 250 in the list
            String movie_rating = rating.text();
            String movie_rank = movie.ownText();
            //getting the rank of the movie

            //dealing with links
            Element link = movie.child(0); //the first element under the movie, which is the <a href...>
            StringBuilder s = new StringBuilder("https://www.imdb.com");
            s.append(link.attr("href"));
            String movie_link = s.toString();
            //this is the link of the movie
            String movie_title = link.ownText();// gets the title of the movie in the <a>

            link = movie.child(1);//this gets the 2nd element in the under movie element
            String movie_year = link.ownText();// the year of release of movie

            Document movie_page = Jsoup.connect(movie_link).get();
            //all the variables for storing the data from the webpage of each movie
            String ratings_no = get_ratings(movie_page);
            String censor_text = get_censor(movie_page);
            List<String> genres_text = get_genres(movie_page);
            while (genres_text.size() < 3)
            {
                String temp = "-";
                genres_text.add(temp);
            }//adds '-' for other genres if there are none
            String length_text = get_length(movie_page);
            String releasedate_text = get_release(movie_page);
            String director_text = get_director(movie_page);
            List<String> writers_text= get_writers(movie_page);
            while (writers_text.size() < 2)
            {
                String temp = "-";
                writers_text.add(temp);
            }
            List<String> stars_text = get_stars(movie_page);
            //each movie has 3 stars so making some fields '-' is not required

            //writing data in sheet
            HSSFRow row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(movie_rank);
            row.createCell(1).setCellValue(movie_title);
            row.createCell(2).setCellValue(movie_year);
            row.createCell(3).setCellValue(movie_rating);
            row.createCell(4).setCellValue(ratings_no);
            row.createCell(5).setCellValue(censor_text);
            row.createCell(6).setCellValue(genres_text.get(0));
            row.createCell(7).setCellValue(genres_text.get(1));
            row.createCell(8).setCellValue(genres_text.get(2));
            row.createCell(9).setCellValue(length_text);
            row.createCell(10).setCellValue(releasedate_text);
            row.createCell(11).setCellValue(director_text);
            row.createCell(12).setCellValue(writers_text.get(0));
            row.createCell(13).setCellValue(writers_text.get(1));
            row.createCell(14).setCellValue(stars_text.get(0));
            row.createCell(15).setCellValue(stars_text.get(1));
            row.createCell(16).setCellValue(stars_text.get(2));

        }
        FileOutputStream fileOut = new FileOutputStream(filename);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        System.out.println("Excel sheet created");
    }
}

