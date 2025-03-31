package ass.ass.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "Orders")
public class Order implements Serializable{
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
Long id;
@Temporal(TemporalType.DATE)
@Column(name = "Createdate")
 @DateTimeFormat(pattern = "yyyy-MM-dd")
Date createDate = new Date();
@ManyToOne @JoinColumn(name = "Username")
Account account;
@OneToMany(mappedBy = "order")
List<OrderDetail> orderDetails;
}
