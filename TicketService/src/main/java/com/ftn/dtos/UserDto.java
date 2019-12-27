package com.ftn.dtos;

import com.ftn.model.Reservation;
import com.ftn.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserDto {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private RoleDto role;

    private Boolean active;

    private Set<ReservationDto> reservations;

    public UserDto(){}

    public UserDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.role = new RoleDto(user.getRole());
        this.active = user.getActive();

        this.reservations = new HashSet<ReservationDto>();
        for(Reservation r : user.getReservations()){
            ReservationDto rdto = new ReservationDto();
            rdto.setId(r.getId());
            rdto.setActive(r.getActive());
            rdto.setExpDays(r.getExpDays());
            TicketDto ticketDto = new TicketDto();
                ticketDto.setId(r.getTicket().getId());
                ticketDto.setRowNum(r.getTicket().getRowNum());
                ticketDto.setSeatNum(r.getTicket().getSeatNum());
            rdto.setTicket(ticketDto);

            this.reservations.add(rdto);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<ReservationDto> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationDto> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", active=" + active +
                ", reservations=" + reservations +
                '}';
    }
}
