package master.leonardo.wrapperapi.DTO;

public class PersonDTOBuilder {
	private PersonDTO person;
	public PersonDTOBuilder() {
		this.person = new PersonDTO();
	}
	public PersonDTO build() {
        return person;
    }
	public PersonDTOBuilder setCreditScore(int creditScore) {
        person.setCreditScore(creditScore);
        return this;
    }
	public PersonDTOBuilder setCountry(String country) {
        person.setCountry(country);
        return this;
    }
	public PersonDTOBuilder setGender(String gender) {
		person.setGender(gender);
        return this;
	}
	public PersonDTOBuilder setAge(int age) {
        person.setAge(age);
        return this;
    }

	public PersonDTOBuilder setTenure(int tenure) {
        person.setTenure(tenure);
        return this;
	}
	public PersonDTOBuilder setBalance(long balance) {
        person.setBalance(balance);
        return this;
    }
	public PersonDTOBuilder setProductsNumber(int productsNumber) {
        person.setProductsNumber(productsNumber);
        return this;
    }
	public PersonDTOBuilder setCreditCard(int creditCard) {
        person.setCreditCard(creditCard);
        return this;
    }
	public PersonDTOBuilder setActiveMember(int activeMember) {
        person.setActiveMember(activeMember);
        return this;
    }
	public PersonDTOBuilder setEstimatedSalary(double estimatedSalary) {
        person.setEstimatedSalary(estimatedSalary);
        return this;
    }
	public PersonDTOBuilder setChurn(int churn) {
		this.person.setChurn(churn);
		return this;
	}

	
}
