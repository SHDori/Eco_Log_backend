package Eco_Log.Eco_Log.domain.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class profile {
    @Id
    @GeneratedValue
    @Column(name = "profile_id")
    private Long id;

}
