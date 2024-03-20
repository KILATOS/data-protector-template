package master.leonardo.wrapperapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "EncryptedPerson")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EncryptedPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    
    @Column(name = "credit_score")
    private int creditScore;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "age")
    private int age;
    
    @Column(name = "tenure")
    private int tenure;
    
    @Column(name = "balance")
    private long balance;
    
    @Column(name = "products_number")
    private int productsNumber;
    
    @Column(name = "credit_card")
    private int creditCard;
    
    @Column(name = "is_active_member")
    private int activeMember;
    
    @Column(name = "estimated_salary")
    private double estimatedSalary;
    
    @Column(name = "churn")
    private int churn;
    
    @Column(name = "signature")
    private String signature;

	@Override
	public String toString() {
		return "EncryptedPerson [id=" + id + ", creditScore=" + creditScore + ", country=" + country + ", gender="
				+ gender + ", age=" + age + ", tenure=" + tenure + ", balance=" + balance + ", productsNumber="
				+ productsNumber + ", creditCard=" + creditCard + ", activeMember=" + activeMember
				+ ", estimatedSalary=" + estimatedSalary + ", churn=" + churn + ", signature=" + signature + "]";
	}
    
    
    
     
}
