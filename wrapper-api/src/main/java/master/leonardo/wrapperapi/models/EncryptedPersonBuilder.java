package master.leonardo.wrapperapi.models;

public class EncryptedPersonBuilder {
	private EncryptedPerson person;

	public EncryptedPersonBuilder(EncryptedPerson person) {
		this.person = person;
	
	}
	
	public EncryptedPersonBuilder setCreditScore(int creditScore) {
		person.setCreditScore(creditScore);
		return this;
	}
	
	public EncryptedPersonBuilder setCountry (String country) {
		person.setCountry(country);
		return this;
	}
	
	public EncryptedPersonBuilder setGender(String gender) {
		person.setGender(gender);
		return this;
	}
	
	public EncryptedPersonBuilder setAge(int age) {
		person.setAge(age);
		return this;
	}
	
	public EncryptedPersonBuilder setTenure(int tenure) {
		person.setTenure(tenure);
		return this;
	}
	
	public EncryptedPersonBuilder setBalance(long balance) {
		person.setBalance(balance);
		return this;
	}
	
	public EncryptedPersonBuilder setProductsNumber(int productsNumber) {
		person.setProductsNumber(productsNumber);
		return this;
	}
	
	public EncryptedPersonBuilder setCreditCard(int creditCard) {
		person.setCreditCard(creditCard);
		return this;
	}
	
	public EncryptedPersonBuilder setActiveMember(int isActiveMember) {
		person.setActiveMember(isActiveMember);
		return this;
	}
	
	public EncryptedPersonBuilder setEstimatedSalary(double estimatedSalary) {
		person.setEstimatedSalary(estimatedSalary);
		return this;
	}
	
	public EncryptedPersonBuilder setChurn(int churn) {
		person.setChurn(churn);
		return this;
	}
	
	public EncryptedPersonBuilder setSignature(String signature) {
		person.setSignature(signature);
		return this;
	}
	public EncryptedPerson build() {
		return person;
	}
}
