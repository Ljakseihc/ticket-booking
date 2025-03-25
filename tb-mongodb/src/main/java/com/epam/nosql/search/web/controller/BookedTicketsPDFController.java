package com.epam.nosql.search.web.controller;

import com.epam.nosql.search.model.entity.Ticket;
import com.epam.nosql.search.model.entity.User;
import com.epam.nosql.search.facade.impl.BookingFacadeImpl;
import com.epam.nosql.search.util.PDFUtils;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/tickets/user", consumes = APPLICATION_PDF_VALUE, produces = APPLICATION_PDF_VALUE)
public class BookedTicketsPDFController {

    private final BookingFacadeImpl bookingFacade;

    private final PDFUtils pdfUtils;

    @Autowired
    public BookedTicketsPDFController(BookingFacadeImpl bookingFacade, PDFUtils pdfUtils) {
        this.bookingFacade = bookingFacade;
        this.pdfUtils = pdfUtils;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getBookedTicketsByUserPDF(
            @NotNull(message = "userId is not provided") @PathVariable String userId,
            @NotNull(message = "pageSize is not provided") @RequestParam int pageSize,
            @NotNull(message = "pageNum is not provided") @RequestParam int pageNum) {
        log.info("Showing the tickets by user with id: {}", userId);

        User user = bookingFacade.getUserById(userId);
        List<Ticket> bookedTickets = bookingFacade.getBookedTicketsByUserId(user.getId(), pageSize, pageNum);
        if (bookedTickets.isEmpty()) {
            log.info("Can not to find the tickets by user with id: {}", userId);
            throw new RuntimeException("Can not to find the tickets by user with id: " + userId);
        }

        log.info("The tickets successfully found");

        return createResponseEntityWithPDFDocument(bookedTickets);
    }

    private ResponseEntity<Object> createResponseEntityWithPDFDocument(List<Ticket> bookedTickets) {
        createPDFDocument(bookedTickets);
        InputStreamResource pdfDocument = pdfUtils.getPDFDocument();
        pdfUtils.deletePDFDocument();
        return ResponseEntity.ok(pdfDocument);
    }

    private void createPDFDocument(List<Ticket> bookedTickets) {
        Path path = Paths.get("Booked Tickets.pdf");
        pdfUtils.setTickets(bookedTickets);
        pdfUtils.setPath(path);
        pdfUtils.createPDFFileOfBookedTicketsByUser();
    }
}
