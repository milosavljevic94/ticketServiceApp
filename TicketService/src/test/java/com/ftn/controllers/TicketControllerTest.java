package com.ftn.controllers;

import com.ftn.constants.*;
import com.ftn.dtos.*;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class TicketControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    ReservationRepository reservationRepository;

    private String accessToken;

    @Before
    public void login() {
        ResponseEntity<LoggedInUserDTO> login = restTemplate.postForEntity("/login", new LoginDTO("testAdmin", "admin"),
                LoggedInUserDTO.class);

        accessToken = login.getBody().getToken();
    }

    private HttpEntity<Object> createRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Object> createRequestEntityNotLoggedIn() {
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(headers);
    }

    @Test
    public void testGetAllTicket_thenReturnTicketList(){
        ResponseEntity<TicketDto[]> response = restTemplate.exchange("/api/ticket/allTicket", HttpMethod.GET,
                createRequestEntity(), TicketDto[].class);
        TicketDto[] result = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertNotEquals(0, result.length);
        assertEquals(5, result.length);
    }

    @Test
    public void testGetAllTicketOfUser_thenReturnTicketList(){
        ResponseEntity<TicketDto[]> response = restTemplate.exchange("/api/ticket/allUserTicket", HttpMethod.GET,
                createRequestEntity(), TicketDto[].class);
        TicketDto[] result = response.getBody();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertNotEquals(0, result.length);
        assertEquals(1, result.length);
    }

    @Test
    public void testGetAllTicketOfUserNotLoggedIn_thenBadRequest(){
        ResponseEntity<TicketDto[]> response = restTemplate.exchange("/api/ticket/allUserTicket", HttpMethod.GET,
                createRequestEntityNotLoggedIn(), (Class<TicketDto[]>) null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetTicket_thenReturnTicket(){
        ResponseEntity<TicketDto> response = restTemplate.exchange("/api/ticket/" + TicketConst.OK_TICKET_ID, HttpMethod.GET,
                createRequestEntity(), TicketDto.class);
        TicketDto result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertEquals(TicketConst.DB_ROW, result.getRowNum());
        assertEquals(TicketConst.DB_COLUMN, result.getSeatNum());
        assertTrue(result.getPurchaseConfirmed());
        assertEquals(TicketConst.DB_PURCHASE_TIME, result.getPurchaseTime());
    }

    @Test
    public void testGetTicketNotValidId_thenReturnNotFound(){
        ResponseEntity<TicketDto> response = restTemplate.exchange("/api/ticket/" + TicketConst.WRONG_TICKET_ID, HttpMethod.GET,
                createRequestEntity(), TicketDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void testBuyTicketMakeNewTicket_thenReturnBoughtTicket() {

        int sizeBeforeBuy = ticketRepository.findAll().size();

        BuyTicketDto buyTicketDto = TicketConst.validTicketForBuyTest();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(buyTicketDto, headers);

        ResponseEntity<TicketDto> response = restTemplate.exchange("/api/ticket/buyTicket", HttpMethod.POST,
                requestEntity, TicketDto.class);

        TicketDto result = response.getBody();

        int sizeAfterBuy = ticketRepository.findAll().size();
        assertEquals(sizeBeforeBuy + 1, sizeAfterBuy);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertEquals(buyTicketDto.getWantedSeat().getRow(), result.getRowNum().intValue());
        assertEquals(buyTicketDto.getWantedSeat().getSeatNumber(), result.getSeatNum().intValue());
        assertTrue(result.getPurchaseConfirmed());
        assertNull(result.getReservation());

        //after buying/adding ticket, delete ticket.
        ticketRepository.deleteById(Long.valueOf(sizeAfterBuy));
    }

    @Test
    public void testBuyTicketMakeNewTicketNotLoggedIn_thenReturnBadRequest() {
        BuyTicketDto buyTicketDto = TicketConst.validTicketForBuyTest();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(buyTicketDto, headers);

        ResponseEntity<TicketDto> response = restTemplate.exchange("/api/ticket/buyTicket", HttpMethod.POST,
                requestEntity, TicketDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testBuyTicketMakeNewTicketWrongManDay_thenReturnNotFound(){
        BuyTicketDto buyTicketDto = TicketConst.validTicketForBuyTest();
        buyTicketDto.setDayId(ManDaysConst.NOT_VALID_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(buyTicketDto, headers);

        ResponseEntity<TicketDto> response = restTemplate.exchange("/api/ticket/buyTicket", HttpMethod.POST,
                requestEntity, TicketDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testBuyTicketMakeNewTicketWrongRowAndColumn_thenReturnBadRequest(){
        BuyTicketDto buyTicketDto = TicketConst.ticketWrongRowAndColumn();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(buyTicketDto, headers);

        ResponseEntity<TicketDto> response = restTemplate.exchange("/api/ticket/buyTicket", HttpMethod.POST,
                requestEntity, TicketDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testBuyTicketMakeNewTicketForTakenSeat_thenReturnBadRequest(){
        BuyTicketDto takenSeat = TicketConst.ticketForBuyTakenSeat();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(takenSeat, headers);

        ResponseEntity<TicketDto> response = restTemplate.exchange("/api/ticket/buyTicket", HttpMethod.POST,
                requestEntity, TicketDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /*
    Here we set a number of seats in sector to simulate full sector.
     */
    @Test
    @Sql(statements = "UPDATE sector SET seats_number = 1 WHERE id = 1")
    public void testBuyTicketMakeNewTicketSectorIsFull_thenReturnBadRequest(){
        BuyTicketDto takenSeat = TicketConst.validTicketForBuyTest();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(takenSeat, headers);

        ResponseEntity<TicketDto> response = restTemplate.exchange("/api/ticket/buyTicket", HttpMethod.POST,
                requestEntity, TicketDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /*
    Dirties context after method to refresh db and remove added items.
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testReserveTicketMakeNewTicket_thenReturnReservation(){

        int ticketSizeBefore = ticketRepository.findAll().size();
        int reservationsSizeBefore = reservationRepository.findAll().size();

        BuyTicketDto reserveTicket = TicketConst.integrationTest_validToReserve();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(reserveTicket, headers);

        ResponseEntity<ReservationDto> response = restTemplate.exchange("/api/ticket/reserveTicket", HttpMethod.PUT,
                requestEntity, ReservationDto.class);

        ReservationDto result = response.getBody();

        int ticketSizeAfter = ticketRepository.findAll().size();
        int reservationSizeAfter = reservationRepository.findAll().size();

        assertEquals(ticketSizeBefore + 1, ticketSizeAfter);
        assertEquals(reservationsSizeBefore + 1, reservationSizeAfter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);

        assertTrue(result.getActive());
        assertEquals(reserveTicket.getWantedSeat().getSeatNumber(), result.getTicket().getWantedSeat().getSeatNumber());
        assertEquals(reserveTicket.getWantedSeat().getRow(), result.getTicket().getWantedSeat().getRow());
        assertEquals(reserveTicket.getDayId(), result.getTicket().getDayId());
        assertEquals(reserveTicket.getWantedSeat().getManSectorId(), result.getTicket().getWantedSeat().getManSectorId());
    }

    @Test
    public void testReserveTicketMakeNewTicketNotLoggedIn_thenReturnBadRequest(){
        BuyTicketDto reserveTicket = TicketConst.integrationTest_validToReserve();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(reserveTicket, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/reserveTicket", HttpMethod.PUT,
                createRequestEntityNotLoggedIn(), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testReserveTicketMakeNewTicketWrongManDay_thenReturnNotFound(){
        BuyTicketDto reserveTicket = TicketConst.validTicketForBuyTest();
        reserveTicket.setDayId(ManDaysConst.NOT_VALID_ID);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(reserveTicket, headers);

        ResponseEntity<ReservationDto> response = restTemplate.exchange("/api/ticket/reserveTicket", HttpMethod.PUT,
                requestEntity, (Class<ReservationDto>) null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testReserveTicketMakeNewTicketAfterRestriction_thenReturnBadRequest() {
        BuyTicketDto reserveTicket = TicketConst.validTicketForBuyTest();
        reserveTicket.setDayId(4L);  /*Set ticket to reserve for day with id 4, because start date is in restriction.*/

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(reserveTicket, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/reserveTicket", HttpMethod.PUT,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testReserveTicketMakeNewTicketWrongRowAndColumn_thenReturnBadRequest(){
        BuyTicketDto reserveTicket = TicketConst.ticketWrongRowAndColumn();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(reserveTicket, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/reserveTicket", HttpMethod.PUT,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testReserveTicketMakeNewTicketForTakenSeat_thenReturnBadRequest(){
        BuyTicketDto reserveTicket = TicketConst.ticketForBuyTakenSeat();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(reserveTicket, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/reserveTicket", HttpMethod.PUT,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(statements = "UPDATE sector SET seats_number = 1 WHERE id = 1")
    public void testReserveTicketMakeNewTicketSectorIsFull_thenReturnBadRequest(){
        BuyTicketDto reserveTicket = TicketConst.validTicketForBuyTest();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<BuyTicketDto> requestEntity = new HttpEntity<>(reserveTicket, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/reserveTicket", HttpMethod.PUT,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testBuyReservedTicket_thenReturnBoughtTicket() {
        ResponseEntity<TicketDto> response = restTemplate.exchange("/api/ticket//buyReservedTicket/" + ReservationConst.OTHER_USER_RESERVATION, HttpMethod.GET,
                createRequestEntity(), TicketDto.class);

        TicketDto result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(result);
        assertEquals(ReservationConst.OTHER_USER_RESERVATION, result.getReservation().getId());
        assertTrue(result.getPurchaseConfirmed());
    }

    @Test
    public void testBuyReservedTicketNotLoggedIn_thenReturnBadRequest(){
        ResponseEntity<String> response = restTemplate.exchange("/api/ticket//buyReservedTicket/" + ReservationConst.OTHER_USER_RESERVATION, HttpMethod.GET,
                createRequestEntityNotLoggedIn(), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("You must log in first!"));
    }

    @Test
    public void testBuyReservedTicketNotValidReservationId_thenReturnNotFound(){
        ResponseEntity<String> response = restTemplate.exchange("/api/ticket//buyReservedTicket/" + ReservationConst.NOT_VALID_ID, HttpMethod.GET,
                createRequestEntity(), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testBuyReservedTicketNotHaveReservation_thenReturnBadRequest(){
        ResponseEntity<String> response = restTemplate.exchange("/api/ticket//buyReservedTicket/" + ReservationConst.VALID_ID, HttpMethod.GET,
                createRequestEntity(), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /*
    When the functionality is enabled, then test.
     */
    @Test
    public void testDeleteTicket_thenReturnOk() {
        // TODO: 11.5.2020 : delete ticket success test.
        /*int sizeBeforeDel = ticketRepository.findAll().size();

        System.out.println("size before del : "+ sizeBeforeDel);

        //ticketRepository.deleteById(6L);

        ResponseEntity<?> response = restTemplate.exchange("/api/ticket/"+sizeBeforeDel , HttpMethod.DELETE,
                createRequestEntity(), (Class<String>) null);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        int sizeAfterDel = ticketRepository.findAll().size();
        assertEquals(sizeBeforeDel - 1, sizeAfterDel);*/
    }

    @Test
    public void testDeleteTicketNotLoggedIn_thenReturnBadRequest(){
        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/5" , HttpMethod.DELETE,
                createRequestEntityNotLoggedIn(), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("You must log in first!"));
    }

    @Test
    public void testDeleteTicketOfOtherUser_thenReturnBadRequest(){
        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/"+ TicketConst.OK_TICKET_ID , HttpMethod.DELETE,
                createRequestEntity(), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Can not delete other user ticket!"));
    }

    @Test
    public void testReportForDayOnLocation_thenReturnReport(){
        String validDate = "2020-02-10";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity requestEntity = new HttpEntity<>(validDate, headers);

        ResponseEntity<TicketReportDto> response = restTemplate.exchange("/api/ticket/reportDayLocation/"+ LocationConst.VALID_LOC_ID, HttpMethod.POST,
                requestEntity, TicketReportDto.class);

        TicketReportDto report = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(report);
        assertEquals(2, report.getSoldTicketNumber());
        assertEquals(500.00, report.getProfit(), 0);
    }

    @Test
    public void testReportForDayOnLocationWrongDateFormat_thenReturnBadRequest(){
        String notValidDate = "20-02";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity requestEntity = new HttpEntity<>(notValidDate, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/reportDayLocation/"+ LocationConst.VALID_LOC_ID, HttpMethod.POST,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testReportForMonthOnLocation_thenReturnReport(){
        String validDate = "2020-02";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity requestEntity = new HttpEntity<>(validDate, headers);

        ResponseEntity<TicketReportDto> response = restTemplate.exchange("/api/ticket/reportMonthLocation/"+ LocationConst.VALID_LOC_ID, HttpMethod.POST,
                requestEntity, TicketReportDto.class);

        TicketReportDto report = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(report);
        assertEquals(5, report.getSoldTicketNumber());
        assertEquals(1450.00, report.getProfit(), 0);
    }

    @Test
    public void testReportForMonthOnLocationWrongDateFormat_thenReturnBadRequest(){
        String notValidDate = "20-02";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity requestEntity = new HttpEntity<>(notValidDate, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/reportMonthLocation/"+ LocationConst.VALID_LOC_ID, HttpMethod.POST,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testReportForYearOnLocation_thenReturnReport(){
        String validDate = "2020";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity requestEntity = new HttpEntity<>(validDate, headers);

        ResponseEntity<TicketReportDto> response = restTemplate.exchange("/api/ticket/reportYearLocation/"+ LocationConst.VALID_LOC_ID, HttpMethod.POST,
                requestEntity, TicketReportDto.class);

        TicketReportDto report = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(report);
        assertEquals(5, report.getSoldTicketNumber());
        assertEquals(1450.00, report.getProfit(), 0);
    }

    @Test
    public void testReportForYearOnLocationWrongDateFormat_thenReturnBadRequest(){
        String notValidDate = "20-02";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity requestEntity = new HttpEntity<>(notValidDate, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/reportYearLocation/"+ LocationConst.VALID_LOC_ID, HttpMethod.POST,
                requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testReportForDayOfManifestation_thenReturnReport(){
        ResponseEntity<TicketReportDto> response = restTemplate.exchange("/api/ticket/reportDayManifestation/"+ ManDaysConst.VALID_ID, HttpMethod.GET,
                createRequestEntity(), TicketReportDto.class);

        TicketReportDto report = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(report);
        assertEquals(3, report.getSoldTicketNumber());
        assertEquals(750.00, report.getProfit(), 0);
    }

    @Test
    public void testReportForDayOfManifestationNotValidId_thenReturnNotFound(){
        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/reportDayManifestation/"+ ManDaysConst.NOT_VALID_ID, HttpMethod.GET,
                createRequestEntity(), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testReportForWholeManifestation_thenReturnReport(){
        ResponseEntity<TicketReportDto> response = restTemplate.exchange("/api/ticket/reportManifestation/"+ ManifestationConst.OK_MAN_ID, HttpMethod.GET,
                createRequestEntity(), TicketReportDto.class);

        TicketReportDto report = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(report);
        assertEquals(5, report.getSoldTicketNumber());
        assertEquals(1450.00, report.getProfit(), 0);
    }

    @Test
    public void testReportForWholeManifestationNotValidId_thenReturnNotFound(){
        ResponseEntity<String> response = restTemplate.exchange("/api/ticket/reportManifestation/"+ ManifestationConst.BAD_MAN_ID, HttpMethod.GET,
                createRequestEntity(), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
