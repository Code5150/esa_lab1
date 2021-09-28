import com.example.esa_lab1.dao.AuthorDAO;
import com.example.esa_lab1.dao.BookDAO;
import com.example.esa_lab1.dao.GenreDAO;
import com.example.esa_lab1.dto.Author;
import com.example.esa_lab1.dto.Book;
import com.example.esa_lab1.dto.Genre;
import com.example.esa_lab1.utils.HibernateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.UUID;

public class AppTest {
    private EntityManager em;

    @Before
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "bookShop" );
        em = emf.createEntityManager();
        HibernateUtils.setDaoEntityManager(em);
        //em.getTransaction().begin();
    }

    @Test
    public void insertionTest() {
        var author = new Author();
        author.setName("Александр Пушкин ");
        author.setBio("Великий российский поэт ");
        AuthorDAO.insert(author);
        var author2 = new Author();
        author2.setName("Александр Блок");
        author2.setBio("Тоже великий российский поэт");
        AuthorDAO.insert(author2);
        var author3 = new Author();
        author3.setName("Иван Тургенев");
        author3.setBio("Великий российский писатель");
        AuthorDAO.insert(author3);
        assert(AuthorDAO.selectAll().size() == 3);

        var genre = new Genre();
        genre.setName("Поэма");
        GenreDAO.insert(genre);
        assert(GenreDAO.selectAll().size() == 1);

        var onegin = new Book();
        onegin.setName("Евгений Онегин");
        onegin.setEditionYear(new Date());
        onegin.setDescription("Шедевр русской литературы начала 19 в.");
        onegin.setPrice(200);
        onegin.getAuthors().add(author);
        onegin.getGenres().add(genre);
        BookDAO.insert(onegin);
        assert(BookDAO.selectAll().size() == 1);
        var selected = BookDAO.select(onegin.getId());
        assert(selected.getAuthors() != null);
        assert(selected.getAuthors().size() > 0);
        assert(selected.getGenres() != null);
        assert(selected.getGenres().size() > 0);
    }

    @After
    public void close() {
        /*if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }*/
        if (em.getEntityManagerFactory().isOpen()) {
            em.getEntityManagerFactory().close();
        }
        em.close();
    }
}