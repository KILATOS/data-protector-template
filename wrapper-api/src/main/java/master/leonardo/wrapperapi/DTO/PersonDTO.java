package master.leonardo.wrapperapi.DTO;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonDTO {
    private int creditScore;
    private String country;
    private String gender;
    private int age;
    private int tenure;
    private long balance;
    private int productsNumber;
    private int creditCard;
    private int activeMember;
    private double estimatedSalary;
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
