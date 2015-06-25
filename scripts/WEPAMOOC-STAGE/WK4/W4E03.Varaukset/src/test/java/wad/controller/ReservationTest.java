package wad.controller;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.hibernate.validator.internal.util.Contracts;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wad.Application;
import wad.domain.Reservation;
import wad.ext.apartments.Apartment;
import wad.ext.apartments.ApartmentService;
import wad.repository.ReservationRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ReservationTest {

    private static final String RESERVATIONS_URI = "/reservations";

    private static final String RESERVATIONS_API = "/api/reservations";

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ApartmentService apartmentService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        setApartments();
    }

    @Test
    @Points("W4E03.1")
    public void canGetReservations() throws Exception {
        mockMvc.perform(get(RESERVATIONS_API))
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Points("W4E03.1")
    public void cannotCreateReservation() throws Exception {
        String name = "Apartment name: " + UUID.randomUUID().toString().substring(0, 6);

        mockMvc.perform(post(RESERVATIONS_API).content("{\"apartment\":\"" + name + "\"}").contentType(MediaTypes.HAL_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Points("W4E03.1")
    public void cantDeleteReservation() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date startDate = cal.getTime();

        cal.set(Calendar.DAY_OF_YEAR, 2);
        Date endDate = cal.getTime();

        Reservation r = new Reservation();
        r.setApartmentId(new Long(1));
        r.setReservationStart(startDate);
        r.setReservationEnd(endDate);

        r = reservationRepository.save(r);

        mockMvc.perform(MockMvcRequestBuilders.delete(RESERVATIONS_API + "/" + r.getId()))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Points("W4E03.2")
    public void reservationsAndApartmentsGivenToReservationsJsp() throws Exception {
        mockMvc.perform(get(RESERVATIONS_URI))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("reservations"))
                .andExpect(model().attributeExists("apartments"))
                .andExpect(view().name("/WEB-INF/views/reservations.jsp"));
    }

    @Test
    @Points("W4E03.2")
    public void reservationsHasAllReservations() throws Exception {
        reservationRepository.deleteAll();

        int count = 1 + new Random().nextInt(7);
        Set<Long> apartmentIds = new HashSet<>();

        for (int i = 0; i < count; i++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_YEAR, i + 1);
            Date startDate = cal.getTime();

            cal.set(Calendar.DAY_OF_YEAR, i + 2);
            Date endDate = cal.getTime();

            Reservation rsrv = new Reservation();

            Long l = Math.abs(new Random().nextLong()) % 60;
            apartmentIds.add(l);

            rsrv.setApartmentId(l);
            rsrv.setReservationStart(startDate);
            rsrv.setReservationEnd(endDate);

            reservationRepository.save(rsrv);
        }

        List<Reservation> reservations = reservationRepository.findAll();

        MvcResult res = mockMvc.perform(get(RESERVATIONS_URI))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("reservations"))
                .andExpect(model().attributeExists("apartments"))
                .andExpect(view().name("/WEB-INF/views/reservations.jsp")).andReturn();

        assertNotNull(res.getModelAndView().getModel().get("reservations"));

        Collection<Reservation> resFromModel = (Collection) res.getModelAndView().getModel().get("reservations");
        assertEquals(reservations.size(), resFromModel.size());

        for (Reservation r : resFromModel) {
            if (r.getApartmentId() == null) {
                continue;
            }

            apartmentIds.remove(r.getApartmentId());
        }

        assertTrue("The response from a GET to " + RESERVATIONS_URI + " should contain the reservations that have been stored to the database.", apartmentIds.isEmpty());
    }

    @Test
    @Points("W4E03.2")
    public void reservationStoredToDatabase() throws Exception {
        reservationRepository.deleteAll();

        setApartments();

        String start = "2012-01-01 15:00";
        String end = "2012-01-02 12:00";

        mockMvc.perform(post(RESERVATIONS_URI).param("reservationStart", start).param("reservationEnd", end).param("apartmentId", "1"));

        List<Reservation> reservations = reservationRepository.findAll();

        assertEquals("When a single reservation has been posted to " + RESERVATIONS_URI + ", it should be added to the database.", 1, reservations.size());

        boolean found = false;
        for (Reservation r : reservations) {
            if (r.getApartmentId() == null) {
                continue;
            }

            if (!r.getApartmentId().equals(new Long(1))) {
                continue;
            }

            if (!r.getApartment().equals("Rick")) {
                continue;
            }

            found = true;
        }

        assertTrue("When posting a reservation with apartmentId \"1\", the reservation should retrieve the apartment name from the ApartmentService using it's get-method.", found);
    }

    @Test
    @Points("W4E03.2")
    public void otherReservationStoredToDatabase() throws Exception {
        reservationRepository.deleteAll();

        setApartments();

        String start = "2012-01-01 15:00";
        String end = "2012-01-02 12:00";

        mockMvc.perform(post(RESERVATIONS_URI).param("reservationStart", start).param("reservationEnd", end).param("apartmentId", "2"));

        List<Reservation> reservations = reservationRepository.findAll();

        assertEquals("When a single reservation has been posted to " + RESERVATIONS_URI + ", it should be added to the database.", 1, reservations.size());

        boolean found = false;
        for (Reservation r : reservations) {
            if (r.getApartmentId() == null) {
                continue;
            }

            if (!r.getApartmentId().equals(new Long(2))) {
                continue;
            }

            if (!r.getApartment().equals("Roll")) {
                continue;
            }

            found = true;
        }

        assertTrue("When posting a reservation with apartmentId \"2\", the reservation should retrieve the apartment name from the ApartmentService using it's get-method.", found);
    }

    @Test
    @Points("W4E03.2")
    public void canAddTwoReservations() throws Exception {
        reservationRepository.deleteAll();

        setApartments();

        String start = "2012-01-01 15:00";
        String end = "2012-01-02 12:00";

        mockMvc.perform(post(RESERVATIONS_URI).param("reservationStart", start).param("reservationEnd", end).param("apartmentId", "2"));

        mockMvc.perform(post(RESERVATIONS_URI).param("reservationStart", "2012-01-02 15:00").param("reservationEnd", "2012-01-03 12:00").param("apartmentId", "2"));

        List<Reservation> reservations = reservationRepository.findAll();

        assertEquals("When two reservations have been posted to " + RESERVATIONS_URI + ", they both should be added to the database.", 2, reservations.size());
    }

    @Test
    @Points("W4E03.3")
    public void overlappingReservationsNotAllowedForSameApartment() throws Exception {
        reservationRepository.deleteAll();

        setApartments();

        String start = "2012-01-01 15:00";
        String end = "2012-01-02 12:00";

        mockMvc.perform(post(RESERVATIONS_URI).param("reservationStart", start).param("reservationEnd", end).param("apartmentId", "2"));

        mockMvc.perform(post(RESERVATIONS_URI).param("reservationStart", start).param("reservationEnd", end).param("apartmentId", "2"));

        List<Reservation> reservations = reservationRepository.findAll();

        assertEquals("When two overlapping reservations have been posted to " + RESERVATIONS_URI + ", only one should be added to the database.", 1, reservations.size());
    }

    @Test
    @Points("W4E03.3")
    public void overlappingReservationsAllowedForDifferentApartments() throws Exception {
        reservationRepository.deleteAll();

        setApartments();

        String start = "2012-01-01 15:00";
        String end = "2012-01-02 12:00";

        mockMvc.perform(post(RESERVATIONS_URI).param("reservationStart", start).param("reservationEnd", end).param("apartmentId", "1"));

        mockMvc.perform(post(RESERVATIONS_URI).param("reservationStart", start).param("reservationEnd", end).param("apartmentId", "2"));

        List<Reservation> reservations = reservationRepository.findAll();

        assertEquals("When two reservations have been posted to " + RESERVATIONS_URI + ", they both should be added to the database.", 2, reservations.size());
    }

    @Test
    @Points("W4E03.4")
    public void ableToSetAsPaid() throws Exception {
        reservationRepository.deleteAll();

        setApartments();

        String start = "2012-01-01 15:00";
        String end = "2012-01-02 12:00";

        mockMvc.perform(post(RESERVATIONS_URI).param("reservationStart", start).param("reservationEnd", end).param("apartmentId", "1"));
        mockMvc.perform(post(RESERVATIONS_URI).param("reservationStart", start).param("reservationEnd", end).param("apartmentId", "2"));

        List<Reservation> reservations = reservationRepository.findAll();
        assertEquals("When two reservations have been posted to " + RESERVATIONS_URI + ", they both should be added to the database.", 2, reservations.size());

        for (Reservation reservation : reservations) {
            assertNotNull("When a reservation has been created, it's id should not be null.", reservation.getId());
            assertEquals("When reservations are created, they should be unpaid", "UNPAID", reservation.getPaymentStatus());
        }

        Long reservationId = reservations.get(0).getId();

        mockMvc.perform(post(RESERVATIONS_URI + "/" + reservationId + "/payment"))
                .andExpect(status().is3xxRedirection());

        Reservation updated = reservationRepository.findOne(reservationId);
        assertEquals("Once a reservation has been paid through the " + RESERVATIONS_URI + "/{id}/payment -interface, it's payment status should be set to \"PAID\".", "PAID", updated.getPaymentStatus());
        assertEquals("Even if the payment status of one reservation is updated, it should not affect other reservations.", "UNPAID", reservationRepository.findOne(reservations.get(1).getId()).getPaymentStatus());

    }

    private void setApartments() {
        Map<Long, Apartment> apas = new HashMap<>();
        Apartment ap = new Apartment();
        ap.setName("Rick");
        ap.setId(1L);
        apas.put(ap.getId(), ap);

        ap = new Apartment();
        ap.setName("Roll");
        ap.setId(2L);
        apas.put(ap.getId(), ap);

        ap = new Apartment();
        ap.setName("Trollhouse");
        ap.setId(3L);
        apas.put(ap.getId(), ap);

        this.apartmentService.setApartments(apas);
    }
}
