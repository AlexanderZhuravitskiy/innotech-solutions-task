package com.example.innotechsolutionstask.domain;

import com.example.innotechsolutionstask.web.constant.WebConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "train")
public class Train implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "departure_point")
    @NotBlank(message = WebConstant.BLANK_FIELD_MESSAGE)
    private String departurePoint;

    @Column(name = "arrival_point")
    @NotBlank(message = WebConstant.BLANK_FIELD_MESSAGE)
    private String arrivalPoint;

    @Column(name = "departure_time")
    @NotBlank(message = WebConstant.BLANK_FIELD_MESSAGE)
    private String departureTime;

    @Column(name = "arrival_time")
    @NotBlank(message = WebConstant.BLANK_FIELD_MESSAGE)
    private String arrivalTime;

    @Column(name = "date")
    @NotBlank(message = WebConstant.BLANK_FIELD_MESSAGE)
    private String date;

    @Column(name = "price")
    @NotNull(message = WebConstant.BLANK_FIELD_MESSAGE)
    private Integer price;

    @Column(name = "free_places")
    @NotNull(message = WebConstant.BLANK_FIELD_MESSAGE)
    private Integer freePlaces;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "client_train",
            joinColumns = @JoinColumn(name = "train_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private List<Client> users = new ArrayList<>();

    public Train(String departurePoint, String arrivalPoint, String departureTime, String arrivalTime, String date, Integer price, Integer freePlaces) {
        this.departurePoint = departurePoint;
        this.arrivalPoint = arrivalPoint;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.date = date;
        this.price = price;
        this.freePlaces = freePlaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Train train = (Train) o;

        return id.equals(train.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Train{" +
                "id=" + id +
                ", departurePoint='" + departurePoint + '\'' +
                ", arrivalPoint='" + arrivalPoint + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", date='" + date + '\'' +
                ", price=" + price +
                ", freePlaces=" + freePlaces +
                '}';
    }
}
