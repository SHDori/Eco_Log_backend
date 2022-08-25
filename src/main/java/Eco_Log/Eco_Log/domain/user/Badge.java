package Eco_Log.Eco_Log.domain.user;


import javax.persistence.*;

@Entity
public class Badge {

    @Id
    @GeneratedValue
    @Column(name = "badge_id")
    private Long id;

    @OneToOne(mappedBy = "badge",fetch = FetchType.LAZY)
    private User user;

    private boolean badge_1;
    private boolean badge_2;
    private boolean badge_3;
    private boolean badge_4;

}
