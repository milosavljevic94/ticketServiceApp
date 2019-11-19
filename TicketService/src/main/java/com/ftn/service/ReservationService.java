package com.ftn.service;


import com.ftn.model.Reservation;
import com.ftn.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    public List<Reservation> finfAllReservation(){
        return reservationRepository.findAll();
    }

    public Reservation findOneReservation(Long id){
        return reservationRepository.findById(id).orElse(null);
    }

    public void addReservation(Reservation r){
        reservationRepository.save(r);
    }

    public void deleteReservation(Long id){
        reservationRepository.deleteById(id);
    }

    public void deleteAll(){
        reservationRepository.deleteAll();
    }

    public Boolean ifExist(Long id){
        return reservationRepository.existsById(id);
    }
}
