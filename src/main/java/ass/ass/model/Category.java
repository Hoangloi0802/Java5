package ass.ass.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "Categories")
public class Category implements Serializable{
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
Long id;
String name;
@OneToMany(mappedBy = "category")
List<Product> products;
}