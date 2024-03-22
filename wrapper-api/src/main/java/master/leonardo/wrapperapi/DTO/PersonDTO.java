package master.leonardo.wrapperapi.DTO;

import java.util.Objects;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonDTO {
	@Min(value = 0, message = "credit score should be greater than 0")
	@Max(value = Integer.MAX_VALUE, message = "credit score should be less than " + 0x7fffffff)
    private int creditScore;
	
	@NotBlank(message = "country should not be blank")
	@Pattern(regexp = "[A-Z]{1}[a-z]+", message = "country should contain only letters")
    private String country;
	
	@NotBlank(message = "gender should not be blank")
	@Pattern(regexp = "[Male|Female]{1}", message = "gender should contains only Male or Female")
    private String gender;
	
	@Min(value = 0, message = "age should be greater than 0")
	@Max(value = Integer.MAX_VALUE, message = "credit score should be less than " + 0x7fffffff)
    private int age;
	
	@Min(value = 0, message = "tenure should be greater than 0")
	@Max(value = Integer.MAX_VALUE, message = "credit score should be less than " + 0x7fffffff)
    private int tenure;
	
	@Min(value = Long.MIN_VALUE, message = "balance should be greater than " + Long.MIN_VALUE)
	@Max(value = Long.MAX_VALUE, message = "balance should be less than " + Long.MAX_VALUE)
    private long balance;
	
	@Min(value = 0, message = "products number should be greater than 0")
    @Max(value = Integer.MAX_VALUE, message = "products number should be less than " + 0x7fffffff)
    private int productsNumber;
	
	@Min(value = 0, message = "credit card should be 0 or 1")
    @Max(value = 1, message = "credit card should be 0 or 1")
    private int creditCard;
	
	@Min(value = 0, message = "active member should be 0 or 1")
    @Max(value = 1, message = "active member should be 0 or 1")
    private int activeMember;
	
	@Min(value = 0, message = "estimated salary should be greater than 0")
    private double estimatedSalary;
	
	@Min(value = 0, message = "churn should be 0 or 1")
	@Max(value = 1, message = "churn should be 0 or 1")
    private int churn;
	
	
	@Override
	public String toString() {
		return "[creditScore=" + creditScore + ", country=" + country + ", gender=" + gender + ", age=" + age
				+ ", tenure=" + tenure + ", balance=" + balance + ", productsNumber=" + productsNumber + ", creditCard="
				+ creditCard + ", activeMember=" + activeMember + ", estimatedSalary=" + estimatedSalary + ", churn="
				+ churn + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(activeMember, age, balance, churn, country, creditCard, creditScore, estimatedSalary,
				gender, productsNumber, tenure);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonDTO other = (PersonDTO) obj;
		return activeMember == other.activeMember && age == other.age && balance == other.balance
				&& churn == other.churn && Objects.equals(country, other.country) && creditCard == other.creditCard
				&& creditScore == other.creditScore
				&& Double.doubleToLongBits(estimatedSalary) == Double.doubleToLongBits(other.estimatedSalary)
				&& Objects.equals(gender, other.gender) && productsNumber == other.productsNumber
				&& tenure == other.tenure;
	}
	
    

}
