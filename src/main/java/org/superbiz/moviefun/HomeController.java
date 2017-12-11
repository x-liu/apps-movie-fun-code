package org.superbiz.moviefun;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.db.DbConfig;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    private final PlatformTransactionManager albumsTM;
    private final PlatformTransactionManager moviesTM;




    public HomeController(MoviesBean moviesBean, AlbumsBean albumsBean,
                          MovieFixtures movieFixtures, AlbumFixtures albumFixtures,
                          @Qualifier("albumsTM") PlatformTransactionManager albumTM,
                          @Qualifier("moviesTM") PlatformTransactionManager moviesTM){
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
        this.albumsTM = albumTM;
        this.moviesTM = moviesTM;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        createMovies();
        createAlbums();
        model.put("movies", moviesBean.getMovies());
        model.put("albums", albumsBean.getAlbums());
        return "setup";
    }

    public void createAlbums() {
        TransactionStatus at = albumsTM.getTransaction(null);
        for (Album album : albumFixtures.load()) {
            albumsBean.addAlbum(album);
        }
        albumsTM.commit(at);
    }

    public void createMovies() {
        System.out.println("Movie Title: ->>>>>>>>>>>>>>>>>>>in setup");
        TransactionStatus mt = moviesTM.getTransaction(null);
        System.out.println("Movie Title: ->>>>>>>>>>>>>>>>>>>before for loop");
        for (Movie movie : movieFixtures.load()) {
            System.out.println("Movie Title: ->>>>>>>>>>>>>>>>>>>>" + movie.getTitle());
            moviesBean.addMovie(movie);
            System.out.println("Movie Title: ->>>>>>>>>>>>>>>>>>>>after adding movie");
        }
        moviesTM.commit(mt);
    }
}
