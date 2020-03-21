package com.ftn.dtos;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ftn.model.Reservation;
import com.ftn.model.User;

public class UserDto {
	
	private Long id;

    private String email;
    
    @NotNull
    @NotEmpty
    private String userName;

	@NotNull
    @NotEmpty
    private String firstName;
	
	@NotNull
    @NotEmpty
    private String lastName;
	
	@NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    private RoleDto role;

    private Boolean active;

    private Set<ReservationDto> reservations;

    public UserDto(){}

    public UserDto(User user){
        this.id = user.getId();
        this.userName = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.matchingPassword = user.getMatchingPassword();
        this.role = new RoleDto(user.getRole());
        this.active = user.getActive();

        this.reservations = new HashSet<ReservationDto>();
        for(Reservation r : user.getReservations()){
            ReservationDto rdto = new ReservationDto();
            rdto.setId(r.getId());
            rdto.setActive(r.getActive());
            rdto.setExpDays(r.getExpDays());
             BuyTicketDto ticketDto = new BuyTicketDto();
            ticketDto.setDayId(r.getTicket().getId());
            
            SeatWithPriceDto wantedSeat = new SeatWithPriceDto();
            wantedSeat.setRow(r.getTicket().getRowNum());
            wantedSeat.setSeatNumber(r.getTicket().getSeatNum());
            wantedSeat.setManSectorId(r.getTicket().getManifestationSector().getId());
            ticketDto.setWantedSeat(wantedSeat);

            this.reservations.add(rdto);
        }
    }

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
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
