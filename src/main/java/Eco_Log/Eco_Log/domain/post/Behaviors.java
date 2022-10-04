package Eco_Log.Eco_Log.domain.post;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Behaviors")
public class Behaviors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "behavoirs_id")
    private Long id;

    // 실천 이름
    private String name;

    @OneToMany(mappedBy = "behaviors")
    private List<PRconnect> pRconnects = new ArrayList<>();

    public Behaviors(String name) {
        this.name = name;
    }
}
