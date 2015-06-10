package hy.tmc.cli.backend;

import hy.tmc.cli.domain.Review;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class MailboxTest {

    private Mailbox mail;

    @Before
    public void setup() {
        Mailbox.create();
        mail = Mailbox.getMailbox();

    }

    @Test
    public void whenFilledThereAreNewReviews() {
        assertFalse(mail.reviewsWaiting());
        mail.fill(reviews("nice job", "lol", "hello world"));
        assertTrue(mail.reviewsWaiting());
    }

    @Test
    public void filledReviewsAreWhatTheyShouldBe() {
        mail.fill(reviews("nice job", "lol", "hello world"));
        boolean helloFound = false;
        boolean lelFound = false;
        for (Review r : mail.getUnreadReviews()) {
            if (r.getReviewBody().equals("hello world")) {
                helloFound = true;
            }
            if (r.getReviewBody().equals("lel")) {
                lelFound = true;
            }
        }
        assertTrue(helloFound);
        assertFalse(lelFound);
    }

    @Test
    public void afterReadingReviewsTheMailboxIsEmptyOfReviews() {
        mail.fill(reviews("nice job", "lol", "hello world"));
        assertTrue(mail.reviewsWaiting());
        mail.getUnreadReviews();
        assertFalse(mail.reviewsWaiting());
    }

    @Test
    public void afterFillingTwiceGetReturnsAll() {
        mail.fill(reviews("nice job", "lol", "hello world"));
        mail.fill(reviews("asd"));

        assertEquals(4, mail.getUnreadReviews().size());
    }

    private List<Review> reviews(String... reviewMsgs) {
        List<Review> reviews = new ArrayList<>();
        for (String review : reviewMsgs) {
            Review r = new Review();
            r.setReviewBody(review);
            reviews.add(r);
        }
        return reviews;
    }

}
