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
    
    @Column(name = "hash")
    private String hash;
    
    @Column(name = "signature")
    private String signature;
    
    public EncryptedPerson(String hash, String signature) {
    	this.hash = hash;
    	this.signature = signature;
    }
}
