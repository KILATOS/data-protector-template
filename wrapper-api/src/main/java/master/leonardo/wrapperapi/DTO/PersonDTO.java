package master.leonardo.wrapperapi.DTO;

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

}
